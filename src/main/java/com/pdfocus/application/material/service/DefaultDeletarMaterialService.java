package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.DeletarMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementação do caso de uso para apagar um material existente.
 * Garante que apenas o utilizador proprietário possa realizar a operação.
 */
@Service
public class DefaultDeletarMaterialService implements DeletarMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    public DefaultDeletarMaterialService(MaterialRepository materialRepository, UsuarioRepository usuarioRepository) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     * Este método foi refatorado para segurança. Ele primeiro busca o utilizador
     * logado e, em seguida, delega a operação de deleção para o repositório,
     * que por sua vez garante a posse do material.
     */
    @Override
    @Transactional
    public void executar(UUID id) {
        // Obtém o email do utilizador a partir do principal de segurança.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Busca a entidade de domínio do utilizador correspondente.
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilizador autenticado não pôde ser encontrado na base de dados."));

        // Utiliza o método de deleção segura do repositório, que filtra tanto pelo
        // ID do material quanto pelo ID do utilizador proprietário.
        materialRepository.deletarPorIdEUsuario(id, usuario.getId());
    }
}
