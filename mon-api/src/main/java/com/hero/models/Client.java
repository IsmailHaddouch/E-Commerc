package com.hero.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    private String role; // AJOUTEZ CE CHAMP

    public Client() {}
    public Client(String nom, String email) {
        this.nom = nom;
        this.email = email;
        this.role = "USER"; // Par défaut, tout le monde est USER
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }     // AJOUT
    public void setRole(String role) { this.role = role; } // AJOUT
}