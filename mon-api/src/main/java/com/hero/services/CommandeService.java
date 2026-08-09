package com.hero.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hero.models.Client;
import com.hero.models.Commande;
import com.hero.models.LigneCommande;
import com.hero.models.Produit;
import com.hero.repository.CommandeRepository;
import com.hero.repository.LigneCommandeRepository;
import com.hero.repository.ProduitRepository;

@Service
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final EmailService emailService; // AJOUTÉ

    // CONSTRUCTEUR UNIQUE (AVEC EmailService)
    public CommandeService(CommandeRepository commandeRepository, 
                           ProduitRepository produitRepository,
                           LigneCommandeRepository ligneCommandeRepository,
                           EmailService emailService) {
        this.commandeRepository = commandeRepository;
        this.produitRepository = produitRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.emailService = emailService;
    }

    public List<Commande> obtenirToutesLesCommandes() {
        return commandeRepository.findAll();
    }

    public List<Commande> obtenirCommandesParClient(Client client) {
        return commandeRepository.findByClient(client);
    }

    @Transactional
    public Commande creerCommande(Client client, List<LigneCommande> lignes) {
        // 1. Vérifier le stock pour chaque produit
        for (LigneCommande ligne : lignes) {
            Produit produit = ligne.getProduit();
            int quantiteDemandee = ligne.getQuantite();
            if (produit.getQuantiteStock() < quantiteDemandee) {
                throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
            }
        }

        // 2. Créer la commande
        Commande commande = new Commande(client, lignes);
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut("EN_ATTENTE");

        // 3. Sauvegarder la commande (les lignes sont sauvegardées automatiquement grâce à cascade)
        Commande commandeSauvegardee = commandeRepository.save(commande);

        // 4. Mettre à jour le stock des produits
        for (LigneCommande ligne : lignes) {
            Produit produit = ligne.getProduit();
            produit.setQuantiteStock(produit.getQuantiteStock() - ligne.getQuantite());
            produitRepository.save(produit); // Mise à jour du stock en base
        }

        return commandeSauvegardee;
    }

    // MÉTHODE UNIQUE (AVEC ENVOI D'EMAIL)
    @Transactional
    public Commande mettreAJourStatut(Long idCommande, String nouveauStatut) {
        return commandeRepository.findById(idCommande)
            .map(commande -> {
                commande.setStatut(nouveauStatut);
                Commande commandeSauvegardee = commandeRepository.save(commande);
                
                // Envoyer un email si le statut devient PAYEE
                if ("PAYEE".equals(nouveauStatut)) {
                    emailService.envoyerEmailConfirmation(
                        commande.getClient().getEmail(),
                        commande.getClient().getNom(),
                        commande.getId(),
                        commande.getMontantTotal()
                    );
                }
                return commandeSauvegardee;
            })
            .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'ID : " + idCommande));
    }
}