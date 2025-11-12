package com.pdfocus.application.usuario.dto;

import com.pdfocus.core.models.Usuario;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) para representar os dados de um usuário de forma segura.
 *
 * <p>Este DTO omite informações sensíveis, como o hash da senha, e é destinado
 * a respostas de API ou exposição de dados fora da camada de domínio.</p>
 *
 * @param id    O identificador único do usuário.
 * @param nome  O nome completo do usuário.
 * @param email O endereço de e-mail do usuário.
 */
public record UsuarioResponse(UUID id, String nome, String email) {

    /**
     * Converte um objeto de domínio {@link Usuario} para este DTO seguro.
     *
     * @param usuario O objeto de domínio a ser convertido.
     * @return Uma instância de {@link UsuarioResponse} contendo apenas os dados seguros.
     */
    public static UsuarioResponse fromDomain(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
