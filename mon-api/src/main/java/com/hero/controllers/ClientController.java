package com.hero.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hero.models.Client;
import com.hero.services.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getTousLesClients() {
        return clientService.obtenirTousLesClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientParId(@PathVariable Long id) {
        Optional<Client> client = clientService.obtenirClientParId(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Client> ajouterClient(@RequestBody Client client) {
        Client nouveauClient = clientService.ajouterClient(client);
        return new ResponseEntity<>(nouveauClient, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerClient(@PathVariable Long id) {
        boolean supprime = clientService.supprimerClient(id);
        return supprime ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}