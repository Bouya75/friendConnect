package com.example.friendconnect;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FriendconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendconnectApplication.class, args);
    }

    // 1. AJOUTE CETTE MÉTHODE ICI : On crée l'outil en premier
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. TA MÉTHODE EXISTANTE (Laisse-la en dessous)
    @Bean
    CommandLineRunner start(UtilisateurRepository repo, PasswordEncoder encoder) {
        return args -> {
            String emailAdmin = "admin@friendconnect.com";
            // ... le reste de ton code pour créer l'admin s'il n'existe pas
        };
    }
}