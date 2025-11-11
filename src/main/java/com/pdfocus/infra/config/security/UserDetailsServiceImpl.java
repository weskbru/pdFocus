package com.pdfocus.infra.config.security;

import com.pdfocus.infra.persistence.repository.UsuarioJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por carregar os detalhes de um usuário durante o processo de autenticação.
 *
 * <p>Essa classe é uma implementação da interface {@link UserDetailsService} do Spring Security
 * e atua como uma ponte entre o mecanismo de autenticação do framework e o repositório de
 * persistência {@link UsuarioJpaRepository}, que armazena os dados de credenciais no banco.</p>
 *
 * <p>Durante o fluxo de login, o {@code AuthenticationManager} chama este serviço para localizar
 * um usuário com base no e-mail informado e retornar um objeto {@link UserDetails}, que o Spring
 * utiliza para verificar a senha e as permissões.</p>
 *
 * <p>Pertence à camada <b>infra/config/security</b> dentro da arquitetura Hexagonal, conectando
 * o domínio de usuários ao mecanismo de segurança do Spring.</p>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioJpaRepository usuarioJpaRepository;

    /**
     * Construtor responsável por injetar o repositório de usuários.
     *
     * @param usuarioJpaRepository repositório JPA usado para buscar entidades de usuários.
     */
    public UserDetailsServiceImpl(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    /**
     * Localiza um usuário no banco de dados com base em seu e-mail.
     *
     * <p>O e-mail é tratado como o nome de usuário (<b>username</b>) pelo Spring Security.
     * Caso o usuário não seja encontrado, é lançada uma exceção {@link UsernameNotFoundException},
     * interrompendo o processo de autenticação.</p>
     *
     * <p>Esse método é chamado automaticamente pelo {@code AuthenticationManager} durante
     * o fluxo de autenticação JWT configurado em {@link SecurityConfig}.</p>
     *
     * @param email e-mail do usuário que está tentando se autenticar.
     * @return objeto {@link UserDetails} representando o usuário encontrado.
     * @throws UsernameNotFoundException se o usuário não for encontrado no repositório.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }
}
