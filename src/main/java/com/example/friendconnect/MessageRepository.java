package com.example.friendconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Cette ligne permet de récupérer tous les messages reçus par quelqu'un
    List<Message> findByDestinataire(Utilisateur destinataire);

    // Ta méthode actuelle pour la conversation complète
    List<Message> findByExpediteurAndDestinataireOrExpediteurAndDestinataireOrderByDateEnvoiAsc(
            Utilisateur e1, Utilisateur d1, Utilisateur e2, Utilisateur d2);

// On cherche les messages par destinataire ET où lu est faux
int countByDestinataireAndLuFalse(Utilisateur destinataire);

// On récupère aussi la liste des messages non lus pour un destinataire précis
List<Message> findByDestinataireAndLuFalse(Utilisateur destinataire);
}