package com.example.friendconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Pour la connexion (Spring Security)
    Utilisateur findByEmail(String email);

    // Pour la barre de recherche dans la page des membres
    List<Utilisateur> findByNomContainingIgnoreCase(String nom);
}