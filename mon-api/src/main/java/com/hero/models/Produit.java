package com.hero.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private double prix;
    private String categorie;
    private String imageUrl; // Déclaré au bon endroit
    private String caracteristiques; // Description flexible des tailles, couleurs, volumes, etc.

    // NOUVEAUX CHAMPS POUR LES PROMOS
    private Double prixPromo; // Si null, pas de promo
    private boolean enPromo = false; // Par défaut, pas en promo

    // NOUVEAU CHAMP POUR LE STOCK
    private int quantiteStock = 10; 

    public Produit() {}
    
    // Constructeur avec TOUS les champs (sauf ID car auto-généré)
    public Produit(String nom, double prix, String categorie, String imageUrl) {
        this.nom = nom;
        this.prix = prix;
        this.categorie = categorie;
        this.imageUrl = imageUrl;
    }

    public Produit(String nom, double prix, String categorie, String imageUrl, String caracteristiques) {
        this.nom = nom;
        this.prix = prix;
        this.categorie = categorie;
        this.imageUrl = imageUrl;
        this.caracteristiques = caracteristiques;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCaracteristiques() { return caracteristiques; }
    public void setCaracteristiques(String caracteristiques) { this.caracteristiques = caracteristiques; }

    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }
    
    public Double getPrixPromo() { return prixPromo; }
    public void setPrixPromo(Double prixPromo) { this.prixPromo = prixPromo; }
    public boolean isEnPromo() { return enPromo; }
    public void setEnPromo(boolean enPromo) { this.enPromo = enPromo; }
}