package com.hero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hero.models.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
}