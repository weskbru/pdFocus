package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.DownloadMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementação do caso de uso para download de um arquivo de material.
 * Orquestra a validação de segurança e o acesso ao sistema de arquivos.
 */
@Service
public class DefaultDownloadMaterialService implements DownloadMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialStoragePort materialStoragePort;

    public DefaultDownloadMaterialService(MaterialRepository materialRepository,
                                          UsuarioRepository usuarioRepository,
                                          MaterialStoragePort materialStoragePort) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.materialStoragePort = materialStoragePort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public DownloadResult executar(UUID id) {
        // 1. Identifica o usuário logado a partir do contexto de segurança.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não pôde ser encontrado."));

        // 2. Busca os metadados do material, garantindo que ele pertence ao usuário.
        Material material = materialRepository.buscarPorIdEUsuario(id, usuario.getId())
                .orElseThrow(() -> new MaterialNaoEncontradoException(id));

        // 3. Usa a porta de armazenamento para carregar o recurso (arquivo) físico.
        Resource resource = materialStoragePort.carregar(material.getNomeStorage());

        // 4. Retorna o "pacote" completo com o arquivo e seus metadados.
        return new DownloadResult(resource, material);
    }
}