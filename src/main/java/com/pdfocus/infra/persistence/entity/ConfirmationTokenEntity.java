package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_confirmation_tokens")
public class ConfirmationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "usuario_id")
    private UsuarioEntity usuario;

    public ConfirmationTokenEntity() {}

    public ConfirmationTokenEntity(UsuarioEntity usuario) {
        this.usuario = usuario;
        this.token = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(15);
    }

    // Getters
    public Long getId() { return id; }
    public String getToken() { return token; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public UsuarioEntity getUsuario() { return usuario; }

    // Setters (ADICIONADOS AGORA)
    public void setToken(String token) { this.token = token; }

    public void setCreatedAt(LocalDateTime createdAt) { // <--- O QUE FALTAVA
        this.createdAt = createdAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) { // <--- O QUE FALTAVA
        this.expiresAt = expiresAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    public void setUsuario(UsuarioEntity usuario) { this.usuario = usuario; }
}