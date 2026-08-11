package com.hero.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hero.models.Client;
import com.hero.models.Panier;
import com.hero.services.ClientService;
import com.hero.services.PanierService;

@RestController
@RequestMapping("/api/panier")
public class PanierController {
    private final PanierService panierService;
    private final ClientService clientService;

    public PanierController(PanierService panierService, ClientService clientService) {
        this.panierService = panierService;
        this.clientService = clientService;
        System.out.println("✅ PanierController (complet) chargé !");
    }

    // GET : Récupérer le panier d'un client
    @GetMapping("/{clientId}")
    public ResponseEntity<Panier> getPanier(@PathVariable Long clientId) {
        Client client = clientService.obtenirClientParId(clientId).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();
        return panierService.getPanierClient(client)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.ok(new Panier(client)));
    }

    // POST : Ajouter un produit au panier
    @PostMapping("/{clientId}/ajouter")
    public ResponseEntity<Panier> ajouterAuPanier(
            @PathVariable Long clientId,
            @RequestParam Long produitId,
            @RequestParam int quantite) {
        
        Client client = clientService.obtenirClientParId(clientId).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        Panier panier = panierService.ajouterAuPanier(client, produitId, quantite);
        return ResponseEntity.ok(panier);
    }

    // DELETE : Retirer un article du panier
    @DeleteMapping("/{clientId}/items/{itemId}")
    public ResponseEntity<Panier> supprimerArticleDuPanier(
            @PathVariable Long clientId,
            @PathVariable Long itemId) {
        Client client = clientService.obtenirClientParId(clientId).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        try {
            Panier panier = panierService.retirerDuPanier(client, itemId);
            return ResponseEntity.ok(panier);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}