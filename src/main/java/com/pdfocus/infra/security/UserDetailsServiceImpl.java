package com.pdfocus.infra.security;

import com.pdfocus.infra.persistence.repository.UsuarioJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementação da interface {@link UserDetailsService} do Spring Security.
 * <p>
 * Este serviço atua como a ponte entre o mecanismo de autenticação do Spring Security
 * e a forma como os dados de usuário são armazenados em nossa aplicação (através do
 * {@link UsuarioJpaRepository}).
 * </p>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioJpaRepository usuarioJpaRepository;

    /**
     * Constrói o serviço com a dependência do repositório JPA de usuários.
     *
     * @param usuarioJpaRepository O repositório Spring Data JPA para acessar as entidades de usuário.
     */
    public UserDetailsServiceImpl(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    /**
     * Localiza um usuário com base no seu e-mail (que é tratado como o "username" no Spring Security).
     * <p>
     * Este método é chamado pelo {@code AuthenticationManager} do Spring Security durante
     * o processo de autenticação para recuperar os detalhes do usuário.
     * </p>
     *
     * @param email o e-mail do usuário que está tentando se autenticar.
     * @return um objeto {@link UserDetails} (no nosso caso, a própria {@link com.pdfocus.infra.persistence.entity.UsuarioEntity})
     * que o Spring Security pode usar para validação.
     * @throws UsernameNotFoundException se nenhum usuário com o e-mail fornecido for encontrado no banco de dados.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca a UsuarioEntity pelo e-mail.
        // Como a UsuarioEntity implementa UserDetails, o tipo de retorno é compatível.
        return usuarioJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }
}