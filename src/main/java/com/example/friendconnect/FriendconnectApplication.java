package com.example.friendconnect;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FriendconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendconnectApplication.class, args);
    }

    /**
     * Ce bloc crée un compte ADMIN automatiquement au démarrage
     * si l'email n'existe pas encore dans la base de données.
     */
    @Bean
    CommandLineRunner start(UtilisateurRepository repo, PasswordEncoder encoder) {
        return args -> {
            String emailAdmin = "admin@friendconnect.com";

            if (repo.findByEmail(emailAdmin) == null) {
                Utilisateur admin = new Utilisateur();
                admin.setPrenom("Admin");
                admin.setNom("System");
                admin.setEmail(emailAdmin);
                // Le mot de passe sera crypté : admin123
                admin.setMotDePasse(encoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");

                repo.save(admin);
                System.out.println("✅ Compte ADMIN créé avec succès : " + emailAdmin);
            } else {
                System.out.println("ℹ️ Le compte ADMIN existe déjà.");
            }
        };
    }
}