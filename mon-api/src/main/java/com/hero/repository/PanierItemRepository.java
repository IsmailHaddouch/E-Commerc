package com.hero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hero.models.PanierItem;

@Repository
public interface PanierItemRepository extends JpaRepository<PanierItem, Long> {
}