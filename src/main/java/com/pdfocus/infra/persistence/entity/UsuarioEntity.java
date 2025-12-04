package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Entidade JPA que representa um usuário no banco de dados.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UsuarioEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = false;

    // --- CONTROLE DE RESUMOS ---

    @Column(name = "resumos_hoje", nullable = false)
    private Integer resumosHoje = 0;

    @Column(name = "data_ultimo_uso", nullable = false)
    private LocalDate dataUltimoUso = LocalDate.now();

    // --- CONTROLE DE FEEDBACKS (NOVOS CAMPOS) ---

    /**
     * Contador de feedbacks enviados no dia atual.
     */
    @Column(name = "feedbacks_hoje", nullable = false)
    private Integer feedbacksHoje = 0;

    /**
     * Data do último feedback enviado.
     * Usada para resetar o contador quando o dia vira.
     */
    @Column(name = "data_ultimo_feedback")
    private LocalDate dataUltimoFeedback = LocalDate.now();

    // --- MÉTODOS DA INTERFACE UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.senhaHash;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }
}