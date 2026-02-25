package com.example.friendconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmitieRepository extends JpaRepository<Amitie, Long> {
    // Trouver toutes les amitiés d'un utilisateur spécifique
    List<Amitie> findByDemandeurOrAmi(Utilisateur demandeur, Utilisateur ami);
}