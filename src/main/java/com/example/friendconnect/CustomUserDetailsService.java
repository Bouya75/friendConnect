package com.example.friendconnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository repo; // Ton repository pour parler à la base de données

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. On cherche l'utilisateur en base par son email
        Utilisateur utilisateur = repo.findByEmail(email);

        // 2. Si on ne le trouve pas, on lève une erreur
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
        }

        // 3. On transforme ton "Utilisateur" en un utilisateur compris par Spring Security
        String roleNettoye = utilisateur.getRole().replace("ROLE_", "");

        return User.withUsername(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse()) // C'est le mot de passe déjà crypté
                .roles(roleNettoye)
                .build();
    }
}