package com.hero.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panier")
public class PanierController {

    @GetMapping("/test")
    public String test() {
        return "PanierController OK";
    }

    @PostMapping("/ajouter")
    public String ajouter(@RequestParam Long produitId, @RequestParam int quantite) {
        return "Produit " + produitId + " ajouté x" + quantite;
    }
}