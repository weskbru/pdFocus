package com.pdfocus.core.models;


import java.util.UUID;

/**
 * Representa um usuário no domínio da aplicação.
 * <p>
 * Esta é uma classe de modelo "pura", contendo apenas o estado e as
 * informações essenciais de um usuário. É projetada para ser imutável
 * após a sua criação, com todos os campos marcados como {@code final}.
 * </p>
 */
public class Usuario {
    private final UUID id;
    private final String nome;
    private final String email;

    /**
     * O hash da senha do usuário.
     * <strong>Importante:</strong> Este campo NUNCA deve conter a senha em texto puro,
     * apenas a sua versão criptografada (hash).
     */
    private final String senhaHash;

    /**
     * Constrói uma nova instância de Usuário.
     * <p>
     * Nota: Este construtor não possui validações internas. A responsabilidade de
     * fornecer dados válidos (não nulos, e-mail em formato correto, etc.)
     * recai sobre a camada de serviço que o invoca antes de sua criação.
     * </p>
     *
     * @param id O ID único para o usuário.
     * @param nome O nome do usuário.
     * @param email O e-mail do usuário.
     * @param senhaHash O hash da senha do usuário.
     */
    public Usuario( UUID id, String nome, String email, String senhaHash) {
        this.id = id ;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

}
