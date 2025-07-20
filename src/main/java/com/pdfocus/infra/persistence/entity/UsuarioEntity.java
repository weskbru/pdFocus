package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.util.UUID;

/**
 * Entidade JPA que representa um usuário no banco de dados.
 * Mapeada para a tabela "usuarios".
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UsuarioEntity {

    /**
     * Identificador único do usuário (Chave Primária).
     */
    @Id
    private UUID id;

    /**
     * Nome completo do usuário.
     */
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * E-mail do usuário, usado para login. Deve ser único.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Hash da senha do usuário. Nunca armazene a senha em texto puro.
     * O tamanho da coluna é aumentado para acomodar o hash gerado.
     */
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;
}
