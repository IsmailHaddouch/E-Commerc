package com.hero.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code; // Ex: "PROMO10"
    private double reduction; // Ex: 10.0 (pour 10%)
    private LocalDate dateExpiration;

    public Coupon() {}
    public Coupon(String code, double reduction, LocalDate dateExpiration) {
        this.code = code;
        this.reduction = reduction;
        this.dateExpiration = dateExpiration;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public double getReduction() { return reduction; }
    public void setReduction(double reduction) { this.reduction = reduction; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }
}