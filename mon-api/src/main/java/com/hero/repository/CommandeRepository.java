package com.hero.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hero.models.Client;
import com.hero.models.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    // Pour retrouver toutes les commandes d'un client spécifique
    List<Commande> findByClient(Client client);
}