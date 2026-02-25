
package com.example.friendconnect;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String photoProfil; // On ne garde que celle-là !

    // Constructeur vide (obligatoire pour JPA)
    public Utilisateur() {
    }

    // --- ID ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // --- NOM & PRENOM ---
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // --- EMAIL ---
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // --- MOT DE PASSE ---
    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    // --- PHOTO DE PROFIL ---
    public String getPhotoProfil() {
        return photoProfil;
    }
    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }
}