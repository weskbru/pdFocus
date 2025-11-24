package com.pdfocus.application.usuario.port.saida;

import com.pdfocus.core.models.Usuario;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta de saída (Repository Port) para operações de persistência
 * relacionadas à entidade de domínio {@link Usuario}.
 *
 * <p>Define o contrato que a camada de aplicação utiliza para interagir
 * com a camada de infraestrutura, permitindo que diferentes implementações
 * de repositório (JPA, JDBC, NoSQL, mocks, etc.) possam ser utilizadas
 * sem alterar a lógica de negócio.</p>
 */
public interface UsuarioRepository {

    /**
     * Persiste um usuário no sistema. Pode ser utilizado tanto para criação
     * quanto para atualização de registros existentes.
     *
     * @param usuario O objeto de domínio {@link Usuario} a ser salvo.
     * @return O usuário persistido, possivelmente com atributos gerados (como ID).
     */
    Usuario salvar(Usuario usuario);

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * <p>Retorna um {@link Optional} contendo o usuário se encontrado,
     * permitindo tratamento elegante de casos onde o usuário não exista.</p>
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return Um {@link Optional} com o {@link Usuario} encontrado, ou vazio caso não exista.
     */
    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorId(UUID id);
}
