package com.pdfocus.infra.security;

import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária para fornecer acesso fácil às informações do usuário autenticado.
 */
@Component
public class AuthenticationHelper {

    /**
     * Obtém a entidade do usuario que está autenticado na requisição atual.
     *
     * @return A {@link UsuarioEntity} correspondente ao usuario logado.
     * @throws IllegalStateException se não houver um usuario autenticado ou se
     * o principal de autenticação não for do tipo esperado (UsuarioEntity).
     */
    public UsuarioEntity getUsuarioAutenticado() {
        // Busca a autenticação no contexto de segurança do Spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Valida se a autenticação existe e é válida
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UsuarioEntity)) {
            // Lança uma exceção se não houver um usuario logado.
            // Em um endpoint protegido, isso não deveria acontecer se o filtro JWT funcionar corretamente,
            // mas é uma boa verificação de segurança.
            throw new IllegalStateException("Nenhum usuário autenticado encontrado ou o tipo do principal é inesperado.");
        }

        // Faz o "cast" do principal para nossa entidade de usuário e a retorna
        return (UsuarioEntity) authentication.getPrincipal();
    }
}