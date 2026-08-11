package com.hero.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hero.dto.CheckoutRequest;
import com.hero.dto.CommandeRequest;
import com.hero.models.Client;
import com.hero.models.Commande;
import com.hero.models.LigneCommande;
import com.hero.models.Produit;
import com.hero.services.ClientService;
import com.hero.services.CommandeService;
import com.hero.services.ProduitService;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;
    private final ClientService clientService;
    private final ProduitService produitService; // <-- AJOUTÉ ICI

    // <-- CONSTRUCTEUR MIS À JOUR AVEC ProduitService
    public CommandeController(CommandeService commandeService, 
                              ClientService clientService,
                              ProduitService produitService) {
        this.commandeService = commandeService;
        this.clientService = clientService;
        this.produitService = produitService;
    }

    // 1. Obtenir toutes les commandes (Admin)
    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeService.obtenirToutesLesCommandes();
    }

    // 1b. Obtenir les commandes d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Commande>> getCommandesParClient(@PathVariable Long clientId) {
        return clientService.obtenirClientParId(clientId)
                .map(client -> ResponseEntity.ok(commandeService.obtenirCommandesParClient(client)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 2. Créer une nouvelle commande (Utilise le DTO)
    @PostMapping("/client/{clientId}")
    public ResponseEntity<Commande> creerCommande(
            @PathVariable Long clientId,
            @RequestBody List<CommandeRequest> requete) { // Utilise le DTO
        
        Optional<Client> clientOpt = clientService.obtenirClientParId(clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Transformer la requête (DTO) en vraies Lignes de commande
            List<LigneCommande> lignes = new ArrayList<>();
            for (CommandeRequest item : requete) {
                Optional<Produit> produitOpt = produitService.obtenirProduitParId(item.getProduitId());
                if (produitOpt.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                lignes.add(new LigneCommande(produitOpt.get(), item.getQuantite()));
            }

            // On crée une commande basique sans adresse/paiement créés ici
            Commande nouvelleCommande = commandeService.creerCommande(
                clientOpt.get(),
                lignes,
                "Adresse client non fournie",
                "Ville client non fournie",
                "00000",
                "France",
                "Aucun"
            );
            return new ResponseEntity<>(nouvelleCommande, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 2b. Commande invité / checkout
    @PostMapping("/checkout")
    public ResponseEntity<Commande> checkoutCommande(
            @RequestBody CheckoutRequest request) {
        if (request.getNom() == null || request.getNom().isBlank() ||
            request.getPrenom() == null || request.getPrenom().isBlank() ||
            request.getEmail() == null || request.getEmail().isBlank() ||
            request.getTelephone() == null || request.getTelephone().isBlank() ||
            request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Client client = clientService.trouverOuCreerClientParEmail(
            request.getNom(), request.getPrenom(), request.getEmail(), request.getTelephone());

        try {
            if (request.getAdresseLivraison() == null || request.getAdresseLivraison().isBlank() ||
                request.getVille() == null || request.getVille().isBlank() ||
                request.getCodePostal() == null || request.getCodePostal().isBlank() ||
                request.getPays() == null || request.getPays().isBlank() ||
                request.getModePaiement() == null || request.getModePaiement().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            List<LigneCommande> lignes = new ArrayList<>();
            for (CommandeRequest item : request.getItems()) {
                Optional<Produit> produitOpt = produitService.obtenirProduitParId(item.getProduitId());
                if (produitOpt.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
                lignes.add(new LigneCommande(produitOpt.get(), item.getQuantite()));
            }

            Commande nouvelleCommande = commandeService.creerCommande(
                client,
                lignes,
                request.getAdresseLivraison(),
                request.getVille(),
                request.getCodePostal(),
                request.getPays(),
                request.getModePaiement()
            );
            return new ResponseEntity<>(nouvelleCommande, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 3. Mettre à jour le statut d'une commande (Admin)
    @PutMapping("/{id}/statut")
    public ResponseEntity<Commande> mettreAJourStatut(
            @PathVariable Long id,
            @RequestBody String nouveauStatut) {
        try {
            Commande commande = commandeService.mettreAJourStatut(id, nouveauStatut);
            return ResponseEntity.ok(commande);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



        @PutMapping("/{id}/payer")
    public ResponseEntity<Commande> payerCommande(@PathVariable Long id) {
        try {
            Commande commande = commandeService.mettreAJourStatut(id, "PAYEE");
            return ResponseEntity.ok(commande);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}