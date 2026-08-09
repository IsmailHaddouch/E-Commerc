package com.hero.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // AJOUTEZ CET IMPORT
import com.hero.models.Produit;
import com.hero.repository.ProduitRepository;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> obtenirTousLesProduits() {
        return produitRepository.findAll();
    }

    public Optional<Produit> obtenirProduitParId(Long id) {
        return produitRepository.findById(id);
    }

    // AJOUTEZ CETTE ANNOTATION ICI
    @Transactional
    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }
 @Transactional
    public Produit modifierProduit(Long id, Produit produitDetails) {
        return produitRepository.findById(id)
            .map(produitExistant -> {
                produitExistant.setNom(produitDetails.getNom());
                produitExistant.setPrix(produitDetails.getPrix());
                produitExistant.setCategorie(produitDetails.getCategorie()); // AJOUT
                produitExistant.setImageUrl(produitDetails.getImageUrl());   // AJOUT
                return produitRepository.save(produitExistant);
            })
            .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));
    }


    public boolean supprimerProduit(Long id) {
        if (produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Transactional
    public Produit mettreEnPromo(Long id, Double prixPromo) {
        return produitRepository.findById(id)
            .map(produit -> {
                produit.setPrixPromo(prixPromo);
                produit.setEnPromo(true);
                return produitRepository.save(produit);
            })
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
    }

    @Transactional
    public Produit desactiverPromo(Long id) {
        return produitRepository.findById(id)
            .map(produit -> {
                produit.setPrixPromo(null);
                produit.setEnPromo(false);
                return produitRepository.save(produit);
            })
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
    }
}