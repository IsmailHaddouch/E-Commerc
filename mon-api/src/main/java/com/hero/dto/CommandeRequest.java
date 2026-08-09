package com.hero.dto;

public class CommandeRequest {
    private Long produitId;
    private int quantite;

    public CommandeRequest() {}

    public Long getProduitId() { return produitId; }
    public void setProduitId(Long produitId) { this.produitId = produitId; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}