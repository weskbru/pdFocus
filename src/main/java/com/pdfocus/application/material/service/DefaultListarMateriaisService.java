package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.ListarMateriaisUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementação do caso de uso para listar os materiais de uma disciplina.
 */
@Service
public class DefaultListarMateriaisService implements ListarMateriaisUseCase {

    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    public DefaultListarMateriaisService(MaterialRepository materialRepository, UsuarioRepository usuarioRepository) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * Este método foi refatorado para segurança. A identidade do utilizador é agora
     * obtida a partir do contexto de segurança, garantindo que a busca seja
     * restrita ao utilizador autenticado.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Material> executar(UUID disciplinaId) {
        // Obtém o email do utilizador a partir do principal de segurança.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Busca a entidade de domínio do utilizador correspondente.
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilizador autenticado não pôde ser encontrado no banco de dados."));

        // Utiliza o metodo de busca segura do repositório, que filtra tanto pela
        // disciplina quanto pelo utilizador.
        return materialRepository.listarPorDisciplinaEUsuario(disciplinaId, usuario.getId());
    }
}
