package com.hero.models;

import jakarta.persistence.*;

@Entity
public class LigneCommande { // <--- C'était écrit "Commande" ici, d'où l'erreur !
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    public LigneCommande() {}
    public LigneCommande(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit.isEnPromo() ? produit.getPrixPromo() : produit.getPrix();
    }

    public double getTotalLigne() { return quantite * prixUnitaire; }

    // Getters et Setters
    public Long getId() { return id; }
    public Produit getProduit() { return produit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}