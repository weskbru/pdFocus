package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenJpaRepository extends JpaRepository<ConfirmationTokenEntity, Long> {

    // Busca pelo Token para validação
    Optional<ConfirmationTokenEntity> findByToken(String token);
}