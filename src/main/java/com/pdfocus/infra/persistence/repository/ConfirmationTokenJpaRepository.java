package com.pdfocus.infra.persistence.repository;

import com.pdfocus.infra.persistence.entity.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repare que usamos <ConfirmationTokenEntity, Long> porque o ID da sua entidade é Long
public interface ConfirmationTokenJpaRepository extends JpaRepository<ConfirmationTokenEntity, Long> {

    // O Spring Data cria o SQL automaticamente baseado no nome deste método
    Optional<ConfirmationTokenEntity> findByToken(String token);
}