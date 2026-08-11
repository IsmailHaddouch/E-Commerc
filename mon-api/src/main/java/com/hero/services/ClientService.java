package com.hero.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hero.models.Client;
import com.hero.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        // Créer un admin par défaut si aucun n'existe
        if (clientRepository.findByEmail("admin@ecommerce.com").isEmpty()) {
            Client admin = new Client("Ecommerce", "Admin", "admin@ecommerce.com", "secret", "ADMIN");
            admin.setTelephone("0000000000");
            clientRepository.save(admin);
            System.out.println("Admin créé avec succès ! Email: admin@ecommerce.com, password: secret");
        }
        // Créer un client de démonstration si aucun utilisateur n'existe
        if (clientRepository.findByEmail("client@ecommerce.com").isEmpty()) {
            Client clientDemo = new Client("Client", "Démo", "client@ecommerce.com", "secret", "USER");
            clientDemo.setTelephone("0000000000");
            clientRepository.save(clientDemo);
            System.out.println("Client démo créé avec succès ! Email: client@ecommerce.com, password: secret");
        }
    }

    public List<Client> obtenirTousLesClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> obtenirClientParId(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> obtenirClientParEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Transactional
    public Client ajouterClient(Client client) {
        if (client.getPassword() == null || client.getPassword().isBlank()) {
            client.setPassword("secret");
        }
        if (client.getRole() == null || client.getRole().isBlank()) {
            client.setRole("USER");
        }
        return clientRepository.save(client);
    }

    @Transactional
    public Client trouverOuCreerClientParEmail(String nom, String prenom, String email, String telephone) {
        Optional<Client> clientOpt = clientRepository.findByEmail(email);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setTelephone(telephone);
            return clientRepository.save(client);
        }
        Client nouveauClient = new Client(nom, prenom, email, "secret", "USER");
        nouveauClient.setTelephone(telephone);
        return clientRepository.save(nouveauClient);
    }

    public boolean supprimerClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}