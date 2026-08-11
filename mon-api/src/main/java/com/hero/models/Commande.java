package com.hero.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDateTime dateCommande = LocalDateTime.now();
    private double montantTotal;
    private String statut; // "EN_ATTENTE", "PAYEE", "EXPEDIEE", "LIVREE"
    private String adresseLivraison;
    private String ville;
    private String codePostal;
    private String pays;
    private String modePaiement;
    private boolean paiementEffectue = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commande_id")
    private List<LigneCommande> lignes = new ArrayList<>();

    public Commande() {}
    public Commande(Client client, List<LigneCommande> lignes, String adresseLivraison,
                    String ville, String codePostal, String pays, String modePaiement) {
        this.client = client;
        this.lignes = lignes;
        this.montantTotal = lignes.stream().mapToDouble(LigneCommande::getTotalLigne).sum();
        this.statut = "EN_ATTENTE";
        this.adresseLivraison = adresseLivraison;
        this.ville = ville;
        this.codePostal = codePostal;
        this.pays = pays;
        this.modePaiement = modePaiement;
        this.paiementEffectue = false;
    }

    // Getters et Setters simplifiés (à ajouter si nécessaire)
    public Long getId() { return id; }
    public Client getClient() { return client; }
    public double getMontantTotal() { return montantTotal; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public List<LigneCommande> getLignes() { return lignes; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }
   public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }
}