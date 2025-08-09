package com.pdfocus.application.usuario.port.saida;

import com.pdfocus.core.models.Usuario;

import java.util.Optional;

/**
 * Porta de Saída (Interface de Repositório) para operações de persistência
 * relacionadas à entidade de domínio {@link Usuario}.
 * <p>
 * Define o contrato que a camada de aplicação usa para interagir com a
 * camada de infraestrutura para persistir e recuperar dados de usuários.
 * </p>
 */
public interface UsuarioRepository {

    /**
     * Salva (cria ou atualiza) um usuário.
     *
     * @param usuario O objeto de domínio {@link Usuario} a ser salvo.
     * @return O usuário salvo.
     */
    Usuario salvar(Usuario usuario);

    /**
     * Busca um usuario pelo seu endereço de e-mail.
     *
     * @param email O e-mail a ser buscado.
     * @return um {@link Optional} contendo o {@link Usuario} se encontrado, ou vazio caso contrário.
     */
    Optional<Usuario> buscarPorEmail(String email);
}
