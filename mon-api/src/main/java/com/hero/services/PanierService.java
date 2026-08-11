package com.hero.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hero.models.Client;
import com.hero.models.Panier;
import com.hero.models.PanierItem;
import com.hero.models.Produit;
import com.hero.repository.PanierItemRepository;
import com.hero.repository.PanierRepository;
import com.hero.repository.ProduitRepository;

@Service
public class PanierService {
    private final PanierRepository panierRepository;
    private final PanierItemRepository panierItemRepository;
    private final ProduitRepository produitRepository;

    public PanierService(PanierRepository panierRepository, 
                         PanierItemRepository panierItemRepository,
                         ProduitRepository produitRepository) {
        this.panierRepository = panierRepository;
        this.panierItemRepository = panierItemRepository;
        this.produitRepository = produitRepository;
    }

    public Optional<Panier> getPanierClient(Client client) {
        return panierRepository.findByClient(client);
    }

    @Transactional
    public Panier ajouterAuPanier(Client client, Long produitId, int quantite) {
        Produit produit = produitRepository.findById(produitId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        Panier panier = panierRepository.findByClient(client)
            .orElseGet(() -> panierRepository.save(new Panier(client)));

        Optional<PanierItem> itemExistant = panier.getItems().stream()
            .filter(item -> item.getProduit().getId().equals(produitId))
            .findFirst();

        if (itemExistant.isPresent()) {
            itemExistant.get().setQuantite(itemExistant.get().getQuantite() + quantite);
        } else {
            panier.getItems().add(new PanierItem(produit, quantite));
        }

        return panierRepository.save(panier);
    }

    @Transactional
    public Panier retirerDuPanier(Client client, Long panierItemId) {
        Panier panier = panierRepository.findByClient(client)
            .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        boolean supprime = panier.getItems().removeIf(item -> item.getId().equals(panierItemId));
        if (!supprime) {
            throw new RuntimeException("Article introuvable dans le panier");
        }

        return panierRepository.save(panier);
    }
}