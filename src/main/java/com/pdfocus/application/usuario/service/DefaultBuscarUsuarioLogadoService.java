package com.pdfocus.application.usuario.service;

import com.pdfocus.application.usuario.port.entrada.BuscarUsuarioLogadoUseCase;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementação padrão do caso de uso para buscar o usuário logado.
 * Esta é a nossa "usina elétrica", contendo a lógica real da operação.
 */
@Service
public class DefaultBuscarUsuarioLogadoService implements BuscarUsuarioLogadoUseCase {

    private final UsuarioRepository usuarioRepository;

    public DefaultBuscarUsuarioLogadoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação usa o SecurityContextHolder do Spring Security para obter
     * a identidade do usuário que fez a requisição.
     * </p>
     */
    @Override
    public Usuario executar() {
        // 1. Acessamos o "painel de controle" do Spring Security para ver quem está logado.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            // O Spring Security nos entrega um objeto UserDetails com as informações.
            username = ((UserDetails) principal).getUsername();
        } else {
            // Fallback caso o objeto principal seja apenas uma String.
            username = principal.toString();
        }

        // 2. Com o username (que no nosso sistema é o e-mail), buscamos o usuário completo no banco.
        return usuarioRepository.buscarPorEmail(username)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado no banco de dados."));
    }
}
