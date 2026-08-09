package com.hero.services;

import com.hero.models.*;
import com.hero.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

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
}