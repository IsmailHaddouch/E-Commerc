package com.hero.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hero.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // C'EST CETTE LIGNE QUI MANQUAIT !
    Optional<Client> findByEmail(String email);
}