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
    private UtilisateurRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. On cherche l'utilisateur dans la base par son email
        Utilisateur utilisateur = userRepository.findByEmail(email);

        // 2. Si on ne le trouve pas, on lance une erreur
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
        }

        // 3. On transforme notre "Utilisateur" en un "User" compréhensible par Spring Security
        return User.withUsername(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().replace("ROLE_", "")) // On enlève ROLE_ car Spring l'ajoute tout seul
                .build();
    }
}