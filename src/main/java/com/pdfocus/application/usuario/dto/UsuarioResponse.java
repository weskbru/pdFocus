package com.pdfocus.application.usuario.dto;

import com.pdfocus.core.models.Usuario;

import java.util.UUID;

/**
 * DTO para representar a resposta de dados de um usuário de forma segura,
 * omitindo informações sensíveis como o hash da senha.
 *
 * @param id O ID do usuário.
 * @param nome O nome do usuário.
 * @param email O e-mail do usuário.
 */
public record UsuarioResponse(UUID id, String nome, String email) {

    /**
     * Método de fábrica estático para converter um objeto de domínio {@link Usuario}
     * para este DTO.
     * @param usuario O objeto de domínio a ser convertido.
     * @return O DTO de resposta correspondente.
     */
    public static UsuarioResponse fromDomain(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
