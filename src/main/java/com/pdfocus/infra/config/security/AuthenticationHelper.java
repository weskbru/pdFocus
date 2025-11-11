package com.pdfocus.infra.config.security;

import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utilitário de acesso ao contexto de autenticação do Spring Security.
 * <p>
 * Esta classe encapsula a lógica de recuperação do usuário autenticado a partir
 * do {@link SecurityContextHolder}, garantindo um ponto único de acesso à identidade
 * do usuário dentro da aplicação.
 * </p>
 * <p>
 * Ela é amplamente utilizada pelos controladores e casos de uso que precisam
 * associar operações (como criação, leitura ou exclusão de recursos) ao
 * usuário logado, sem expor detalhes da infraestrutura de segurança.
 * </p>
 */
@Component
public class AuthenticationHelper {

    /**
     * Recupera o {@link UsuarioEntity} correspondente ao usuário atualmente autenticado.
     * <p>
     * Este método assume que o processo de autenticação via filtro JWT já ocorreu
     * e que o principal armazenado no contexto é uma instância válida de {@link UsuarioEntity}.
     * </p>
     *
     * @return A entidade {@link UsuarioEntity} do usuário autenticado.
     * @throws IllegalStateException se:
     * <ul>
     *   <li>Não houver autenticação ativa no contexto;</li>
     *   <li>O principal não for uma instância de {@link UsuarioEntity};</li>
     *   <li>A autenticação não for considerada válida.</li>
     * </ul>
     */
    public UsuarioEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                !(authentication.getPrincipal() instanceof UsuarioEntity)) {
            throw new IllegalStateException(
                    "Nenhum usuário autenticado encontrado ou o tipo do principal é inesperado."
            );
        }

        return (UsuarioEntity) authentication.getPrincipal();
    }
}
