package com.pdfocus.application.material.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.application.material.port.entrada.UploadMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.DisciplinaNaoEncontradaException;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementação do caso de uso para o upload de um novo material.
 */
@Service
public class DefaultUploadMaterialService implements UploadMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialStoragePort materialStoragePort;

    public DefaultUploadMaterialService(
            MaterialRepository materialRepository,
            DisciplinaRepository disciplinaRepository,
            UsuarioRepository usuarioRepository,
            MaterialStoragePort materialStoragePort) {
        this.materialRepository = materialRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
        this.materialStoragePort = materialStoragePort;
    }

    /**
     * {@inheritDoc}
     * Este método foi refatorado para segurança. A identidade do utilizador é agora
     * obtida a partir do contexto de segurança, e a posse da disciplina é
     * verificada antes de associar o novo material.
     */
    @Override
    @Transactional
    public Material executar(UploadMaterialCommand command) {
        Objects.requireNonNull(command, "O comando de upload não pode ser nulo.");

        // 1. Obtém o utilizador autenticado de forma segura.
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilizador autenticado não pôde ser encontrado."));

        // 2. Valida se a disciplina associada existe E pertence ao utilizador.
        disciplinaRepository.findByIdAndUsuarioId(command.disciplinaId(), usuario.getId())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(command.disciplinaId()));

        // 3. Gera um nome de ficheiro único para o armazenamento.
        String extensao = StringUtils.getFilenameExtension(command.nomeOriginal());
        String nomeFicheiroStorage = UUID.randomUUID() + "." + extensao;

        // 4. Guarda o ficheiro físico usando a porta de armazenamento.
        materialStoragePort.guardar(nomeFicheiroStorage, command.inputStream());

        // 5. Cria o objeto de domínio Material com os metadados.
        Material novoMaterial = Material.criar(
                UUID.randomUUID(),
                command.nomeOriginal(),
                nomeFicheiroStorage,
                command.tipoArquivo(),
                command.tamanho(),
                usuario.getId(),
                command.disciplinaId(),
                null // A data de upload será gerada pelo @CreationTimestamp
        );

        // 6. Salva os metadados do material no banco de dados.
        return materialRepository.salvar(novoMaterial);
    }
}

