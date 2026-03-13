package com.example.friendconnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UtilisateurController {

    // --- 1. INJECTION DES DÉPENDANCES (Noms mis à jour) ---
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AmitieRepository amitieRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- 2. NOTIFICATIONS AUTOMATIQUES ---
    @ModelAttribute
    public void ajouterNotifications(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            Utilisateur connecte = utilisateurRepository.findByEmail(auth.getName());
            if (connecte != null) {
                int nb = messageRepository.countByDestinataireAndLuFalse(connecte);
                model.addAttribute("nbMessages", nb);
                model.addAttribute("utilisateurConnecteId", connecte.getId());
                model.addAttribute("utilisateurConnecte", connecte);
            }
        }
    }

    // --- 3. INSCRIPTION & CONNEXION ---
    @GetMapping("/register")
    public String pageInscription() {
        return "register";
    }

    @PostMapping("/register")
    public String inscrire(@RequestParam String nom, @RequestParam String prenom,
                           @RequestParam String email, @RequestParam String motDePasse) {
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setMotDePasse(passwordEncoder.encode(motDePasse)); // Cryptage
        u.setRole("ROLE_USER"); // Rôle par défaut

        utilisateurRepository.save(u);
        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String pageLogin() {
        return "login";
    }

    // --- 4. GESTION DES MEMBRES ---
    @GetMapping("/membres")
    public String afficherMembres(Model model, @RequestParam(required = false) String recherche) {
        List<Utilisateur> liste;
        if (recherche != null && !recherche.isEmpty()) {
            liste = utilisateurRepository.findByNomContainingIgnoreCase(recherche);
        } else {
            liste = utilisateurRepository.findAll();
        }
        model.addAttribute("utilisateurs", liste);
        return "membres";
    }

    // --- 5. PROFIL ---
    @GetMapping("/profil")
    public String afficherProfil(Authentication auth, Model model) {
        Utilisateur connecte = utilisateurRepository.findByEmail(auth.getName());
        model.addAttribute("utilisateur", connecte);
        model.addAttribute("mesAmities", amitieRepository.findByDemandeurOrAmi(connecte, connecte));
        return "profil";
    }

    @PostMapping("/modifier-photo")
    public String modifierPhoto(@RequestParam String urlPhoto, Authentication auth) {
        Utilisateur connecte = utilisateurRepository.findByEmail(auth.getName());
        if (connecte != null) {
            connecte.setPhotoProfil(urlPhoto);
            utilisateurRepository.save(connecte);
        }
        return "redirect:/profil";
    }

    // --- 6. MESSAGERIE ---
    @GetMapping("/messages/{amiId}")
    public String voirConversation(@PathVariable Long amiId, Authentication auth, Model model) {
        Utilisateur connecte = utilisateurRepository.findByEmail(auth.getName());
        Utilisateur ami = utilisateurRepository.findById(amiId).orElse(null);

        // Marquer comme lu
        List<Message> nonLus = messageRepository.findByDestinataireAndLuFalse(connecte);
        for (Message m : nonLus) {
            if (m.getExpediteur().getId().equals(amiId)) {
                m.setLu(true);
                messageRepository.save(m);
            }
        }

        List<Message> conversation = messageRepository.findByExpediteurAndDestinataireOrExpediteurAndDestinataireOrderByDateEnvoiAsc(
                connecte, ami, ami, connecte
        );

        model.addAttribute("ami", ami);
        model.addAttribute("messages", conversation);
        model.addAttribute("utilisateur", connecte);
        return "conversation";
    }

    @PostMapping("/envoyer-message")
    public String envoyerMessage(@RequestParam Long destinataireId, @RequestParam String texte, Authentication auth) {
        Utilisateur connecte = utilisateurRepository.findByEmail(auth.getName());
        Utilisateur destinataire = utilisateurRepository.findById(destinataireId).orElse(null);

        if (destinataire != null && !texte.isEmpty()) {
            Message msg = new Message();
            msg.setExpediteur(connecte);
            msg.setDestinataire(destinataire);
            msg.setContenu(texte);
            msg.setDateEnvoi(LocalDateTime.now());
            msg.setLu(false);
            messageRepository.save(msg);
        }
        return "redirect:/messages/" + destinataireId;
    }
}