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
            Client admin = new Client("Admin Ecommerce", "admin@ecommerce.com");
            admin.setRole("ADMIN");
            clientRepository.save(admin);
            System.out.println("Admin créé avec succès ! Email: admin@ecommerce.com");
        }
    }

    public List<Client> obtenirTousLesClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> obtenirClientParId(Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client ajouterClient(Client client) {
        return clientRepository.save(client);
    }

    public boolean supprimerClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}