package com.example.friendconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // N'oublie pas l'import ici aussi !

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);

    // Cette ligne doit être écrite EXACTEMENT comme ça :
    List<Utilisateur> findByNomContainingIgnoreCase(String nom);
}