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

            Commande nouvelleCommande = commandeService.creerCommande(clientOpt.get(), lignes);
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