package com.pdfocus.application.usuario.dto;

import com.pdfocus.core.models.Usuario;

/**
 * DTO (Data Transfer Object) que representa os detalhes públicos de um usuário.
 *
 * <p>Este DTO é utilizado em respostas de API para expor informações do
 * usuário logado, garantindo que dados sensíveis, como senha, não sejam
 * incluídos.</p>
 *
 * @param nome  O nome completo do usuário.
 * @param email O endereço de e-mail do usuário.
 */
public record UsuarioDetalhesResponse(String nome, String email) {

    /**
     * Converte um objeto de domínio {@link Usuario} para este DTO seguro.
     *
     * @param usuario O objeto de domínio a ser convertido.
     * @return Uma instância de {@link UsuarioDetalhesResponse} contendo apenas os dados públicos.
     */
    public static UsuarioDetalhesResponse fromDomain(Usuario usuario) {
        return new UsuarioDetalhesResponse(usuario.getNome(), usuario.getEmail());
    }
}
