package com.pdfocus.application.usuario.dto;

import com.pdfocus.core.models.Usuario;

/**
 * DTO (Data Transfer Object) que representa os detalhes públicos de um usuário.
 * Usado como resposta para endpoints que retornam informações do usuário logado.
 */
public record UsuarioDetalhesResponse(String nome, String email) {

    /**
     * Método de fábrica (factory method) para criar uma instância deste DTO
     * a partir de um objeto de domínio Usuario.
     *
     * @param usuario O objeto de domínio a ser convertido.
     * @return uma nova instância de UsuarioDetalhesResponse.
     */
    public static UsuarioDetalhesResponse fromDomain(Usuario usuario) {
        return new UsuarioDetalhesResponse(usuario.getNome(), usuario.getEmail());
    }
}
