package com.hero.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hero.models.Produit;
import com.hero.services.ProduitService;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<Produit> getTousLesProduits() {
        return produitService.obtenirTousLesProduits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitParId(@PathVariable Long id) {
        Optional<Produit> produit = produitService.obtenirProduitParId(id);
        return produit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produit> ajouterProduit(@RequestBody Produit produit) {
        Produit nouveauProduit = produitService.ajouterProduit(produit);
        return new ResponseEntity<>(nouveauProduit, HttpStatus.CREATED);
    }

   @PutMapping("/{id}")
    public ResponseEntity<Produit> modifierProduit(@PathVariable Long id, @RequestBody Produit produitDetails) {
        try {
            Produit produitModifie = produitService.modifierProduit(id, produitDetails);
            return ResponseEntity.ok(produitModifie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable Long id) {
        boolean supprime = produitService.supprimerProduit(id);
        return supprime ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
 
    }

    @PutMapping("/{id}/promo")
    public ResponseEntity<Produit> activerPromo(@PathVariable Long id, @RequestBody Double prixPromo) {
        try {
            Produit produit = produitService.mettreEnPromo(id, prixPromo);
            return ResponseEntity.ok(produit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/promo")
    public ResponseEntity<Produit> desactiverPromo(@PathVariable Long id) {
        try {
            Produit produit = produitService.desactiverPromo(id);
            return ResponseEntity.ok(produit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }




}