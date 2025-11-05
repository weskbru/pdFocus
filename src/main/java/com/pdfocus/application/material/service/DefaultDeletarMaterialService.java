package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.DeletarMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.material.MaterialNaoEncontradoException;
import com.pdfocus.core.models.Material;
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
    private final MaterialStoragePort materialStoragePort;

    public DefaultDeletarMaterialService(MaterialRepository materialRepository,
                                         UsuarioRepository usuarioRepository,
                                         MaterialStoragePort materialStoragePort) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.materialStoragePort = materialStoragePort;
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
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilizador autenticado não pôde ser encontrado."));


        Material material = materialRepository.buscarPorIdEUsuario(id, usuario.getId())
                .orElseThrow(() -> new MaterialNaoEncontradoException(id));

        // Ordem correta: Primeiro apaga o arquivo físico, depois o registro do banco
        materialStoragePort.apagar(material.getNomeStorage());
        materialRepository.deletarPorIdEUsuario(id, usuario.getId());
    }
}
