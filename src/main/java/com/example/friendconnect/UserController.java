package com.example.friendconnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private AmitieRepository amitieRepository;

    @Autowired
    private MessageRepository messageRepository;

    // --- AUTOMATISME (S'exécute sur chaque page) ---
    @ModelAttribute
    public void ajouterNotifications(HttpSession session, Model model) {
        Utilisateur connecte = (Utilisateur) session.getAttribute("utilisateurConnecte");
        if (connecte != null) {
            // On compte uniquement les messages NON LUS pour la pastille rouge
            int nb = messageRepository.countByDestinataireAndLuFalse(connecte);
            model.addAttribute("nbMessages", nb);
        }
    }

    // --- 1. INSCRIPTION ---
    @GetMapping("/inscription")
    public String pageInscription() {
        return "inscription";
    }

    @PostMapping("/inscription")
    public String inscrire(@RequestParam String nom, @RequestParam String prenom,
                           @RequestParam String email, @RequestParam String motDePasse) {
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setMotDePasse(motDePasse);

        System.out.println("Enregistrement de : " + u.getEmail());
        userRepository.save(u);
        return "redirect:/login";
    }

    // --- 2. CONNEXION / DÉCONNEXION ---
    @GetMapping("/login")
    public String pageLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String connecter(@RequestParam String email,
                            @RequestParam String motDePasse,
                            HttpSession session,
                            Model model) {
        Utilisateur u = userRepository.findByEmail(email);
        if (u != null && u.getMotDePasse().equals(motDePasse)) {
            session.setAttribute("utilisateurConnecte", u);
            return "redirect:/membres";
        } else {
            model.addAttribute("erreur", "Email ou mot de passe incorrect");
            return "login";
        }
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- 3. GESTION DES MEMBRES ---
    @GetMapping("/membres")
    public String afficherMembres(HttpSession session,
                                  Model model,
                                  @RequestParam(required = false) String recherche) {
        if (session.getAttribute("utilisateurConnecte") == null) return "redirect:/login";

        List<Utilisateur> liste;
        if (recherche != null && !recherche.isEmpty()) {
            liste = userRepository.findByNomContainingIgnoreCase(recherche);
        } else {
            liste = userRepository.findAll();
        }
        model.addAttribute("membres", liste);
        return "membres";
    }

    @PostMapping("/ajouter-ami")
    public String ajouterAmi(@RequestParam Long amiId, HttpSession session) {
        Utilisateur connecte = (Utilisateur) session.getAttribute("utilisateurConnecte");
        if (connecte == null) return "redirect:/login";

        Utilisateur futurAmi = userRepository.findById(amiId).orElse(null);
        if (futurAmi != null && !connecte.getId().equals(amiId)) {
            Amitie amitie = new Amitie();
            amitie.setDemandeur(connecte);
            amitie.setAmi(futurAmi);
            amitie.setStatut("VALIDE");
            amitieRepository.save(amitie);
        }
        return "redirect:/membres";
    }

    // --- 4. PROFIL & PARAMÈTRES ---
    @GetMapping("/profil")
    public String afficherProfil(HttpSession session, Model model) {
        Utilisateur connecte = (Utilisateur) session.getAttribute("utilisateurConnecte");
        if (connecte == null) return "redirect:/login";

        model.addAttribute("utilisateur", connecte);
        model.addAttribute("mesAmities", amitieRepository.findByDemandeurOrAmi(connecte, connecte));
        return "profil";
    }

    @PostMapping("/modifier-photo")
    public String modifierPhoto(@RequestParam String urlPhoto, HttpSession session) {
        Utilisateur connecte = (Utilisateur) session.getAttribute("utilisateurConnecte");
        if (connecte != null) {
            connecte.setPhotoProfil(urlPhoto);
            userRepository.save(connecte);
            session.setAttribute("utilisateurConnecte", connecte);
        }
        return "redirect:/profil";
    }

    // --- 5. MESSAGERIE ---
    @GetMapping("/messages/{amiId}")
    public String voirConversation(@PathVariable Long amiId, HttpSession session, Model model) {
        Utilisateur connecte = (Utilisateur) session.getAttribute("utilisateurConnecte");
        if (connecte == null) return "redirect:/login";

        // Marquer comme lu les messages reçus de cet ami
        List<Message> nonLus = messageRepository.findByDestinataireAndLuFalse(connecte);
        for (Message m : nonLus) {
            if (m.getExpediteur().getId().equals(amiId)) {
                m.setLu(true);
                messageRepository.save(m);
            }
        }

        Utilisateur ami = userRepository.findById(amiId).orElse(null);
        List<Message> conversation = messageRepository.findByExpediteurAndDestinataireOrExpediteurAndDestinataireOrderByDateEnvoiAsc(
                connecte, ami, ami, connecte
        );

        model.addAttribute("ami", ami);
        model.addAttribute("messages", conversation);
        return "conversation";
    }

    @PostMapping("/envoyer-message")
    public String envoyerMessage(@RequestParam Long destinataireId,
                                 @RequestParam String texte,
                                 HttpSession session) {
        Utilisateur connecte = (Utilisateur) session.getAttribute("utilisateurConnecte");
        if (connecte == null) return "redirect:/login";

        Utilisateur destinataire = userRepository.findById(destinataireId).orElse(null);
        if (destinataire != null && !texte.isEmpty()) {
            Message msg = new Message();
            msg.setExpediteur(connecte);
            msg.setDestinataire(destinataire);
            msg.setContenu(texte);
            msg.setDateEnvoi(LocalDateTime.now());
            msg.setLu(false); // <--- LIGNE INTÉGRÉE ICI
            messageRepository.save(msg);
        }
        return "redirect:/messages/" + destinataireId;
    }
}