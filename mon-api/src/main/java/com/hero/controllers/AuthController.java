package com.hero.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hero.dto.LoginRequest;
import com.hero.dto.RegisterRequest;
import com.hero.models.Client;
import com.hero.services.ClientService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ClientService clientService;

    public AuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<Client> register(@RequestBody RegisterRequest request) {
        if (request.getNom() == null || request.getNom().isBlank() ||
            request.getPrenom() == null || request.getPrenom().isBlank() ||
            request.getEmail() == null || request.getEmail().isBlank() ||
            request.getMotDePasse() == null || request.getMotDePasse().isBlank() ||
            request.getTelephone() == null || request.getTelephone().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (clientService.obtenirClientParEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Client nouveauClient = new Client(request.getNom(), request.getPrenom(), request.getEmail(), request.getMotDePasse(), "USER");
        nouveauClient.setTelephone(request.getTelephone());
        Client clientCree = clientService.ajouterClient(nouveauClient);
        return new ResponseEntity<>(clientCree, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Client> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank() ||
            request.getMotDePasse() == null || request.getMotDePasse().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return clientService.obtenirClientParEmail(request.getEmail())
                .filter(client -> client.getPassword().equals(request.getMotDePasse()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
