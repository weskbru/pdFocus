package com.pdfocus.application.usuario.service;

import com.pdfocus.application.usuario.port.entrada.BuscarUsuarioLogadoUseCase;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementação padrão do caso de uso {@link BuscarUsuarioLogadoUseCase}.
 *
 * <p>Este serviço é responsável por recuperar o usuário atualmente autenticado
 * no sistema, utilizando o {@link SecurityContextHolder} do Spring Security.</p>
 *
 * <p>Ele atua como uma "usina elétrica", centralizando a lógica necessária
 * para determinar a identidade do usuário logado e buscar seus dados completos
 * no repositório.</p>
 */
@Service
public class DefaultBuscarUsuarioLogadoService implements BuscarUsuarioLogadoUseCase {

    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor do serviço.
     *
     * @param usuarioRepository Repositório para recuperação de usuários.
     */
    public DefaultBuscarUsuarioLogadoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Executa o caso de uso de busca do usuário logado.
     *
     * <p>O método realiza os seguintes passos:
     * <ol>
     *     <li>Obtém o principal do contexto de segurança do Spring Security;</li>
     *     <li>Determina o username (e-mail) do usuário autenticado;</li>
     *     <li>Recupera o {@link Usuario} completo no repositório.</li>
     * </ol></p>
     *
     * @return O {@link Usuario} atualmente autenticado.
     * @throws IllegalStateException Se o usuário autenticado não for encontrado no banco de dados.
     */
    @Override
    public Usuario executar() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return usuarioRepository.buscarPorEmail(username)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado no banco de dados."));
    }
}
