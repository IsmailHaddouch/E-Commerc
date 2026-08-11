package com.hero;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.hero.models.Produit;
import com.hero.repository.ProduitRepository;

@SpringBootApplication
public class MonApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ProduitRepository produitRepository) {
		return args -> {
			if (produitRepository.count() == 0) {
				produitRepository.save(new Produit(
					"Smartphone Galaxy S", 699.99, "Électronique",
					"https://images.pexels.com/photos/404280/pexels-photo-404280.jpeg"
				));
				produitRepository.save(new Produit(
					"Montre connectée Sport", 129.99, "Accessoires",
					"https://images.pexels.com/photos/277394/pexels-photo-277394.jpeg"
				));
				produitRepository.save(new Produit(
					"Chaise scandinave", 89.50, "Maison",
					"https://images.pexels.com/photos/276583/pexels-photo-276583.jpeg"
				));
				produitRepository.save(new Produit(
					"Casque audio Bluetooth", 59.90, "Audio",
					"https://images.pexels.com/photos/3394663/pexels-photo-3394663.jpeg"
				));
			}
		};
	}

}