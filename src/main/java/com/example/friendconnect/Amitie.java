package com.example.friendconnect;

import jakarta.persistence.*;

@Entity
public class Amitie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Utilisateur demandeur; // Celui qui envoie la demande

    @ManyToOne
    private Utilisateur ami; // Celui qui reçoit la demande

    private String statut; // "EN_ATTENTE" ou "VALIDE"

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getDemandeur() { return demandeur; }
    public void
    setDemandeur(Utilisateur demandeur) { this.demandeur = demandeur; }

    public Utilisateur getAmi() { return ami; }
    public void setAmi(Utilisateur ami) { this.ami = ami; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}