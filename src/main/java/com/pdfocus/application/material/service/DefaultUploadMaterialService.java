package com.pdfocus.application.material.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.application.material.port.entrada.UploadMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
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
 * Implementação padrão do caso de uso {@link UploadMaterialUseCase},
 * responsável por gerenciar o upload de novos materiais de estudo
 * associados a uma disciplina pertencente ao usuário autenticado.
 *
 * <p>
 * Esta classe segue o princípio de isolamento de contexto:
 * o upload é sempre realizado no escopo do usuário logado,
 * garantindo integridade e segurança de dados.
 * </p>
 *
 * <p><b>Fluxo resumido:</b></p>
 * <ol>
 *   <li>Obtém o usuário autenticado via {@link SecurityContextHolder}.</li>
 *   <li>Valida a existência e a posse da disciplina informada.</li>
 *   <li>Gera um nome único para o arquivo de armazenamento.</li>
 *   <li>Salva o arquivo físico por meio da porta {@link MaterialStoragePort}.</li>
 *   <li>Cria e persiste o domínio {@link Material} com os metadados.</li>
 * </ol>
 *
 * <p>
 * <b>Anotações técnicas:</b> O método {@link #executar(UploadMaterialCommand)} é transacional,
 * pois envolve persistência tanto no armazenamento físico quanto no repositório de dados.
 * </p>
 */
@Service
public class DefaultUploadMaterialService implements UploadMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialStoragePort materialStoragePort;

    /**
     * Construtor principal para injeção das dependências necessárias.
     *
     * @param materialRepository Repositório responsável pela persistência de materiais.
     * @param disciplinaRepository Repositório responsável pela validação da disciplina.
     * @param usuarioRepository Repositório para consulta de informações do usuário autenticado.
     * @param materialStoragePort Porta de saída para persistência física dos arquivos.
     */
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
     *
     * <p>
     * Este método realiza o upload seguro de um novo material,
     * associando-o à disciplina do usuário autenticado.
     * Ele valida a posse da disciplina e garante a geração de um
     * nome de arquivo único antes do armazenamento físico.
     * </p>
     *
     * @param command Comando contendo os dados do material e o fluxo de arquivo.
     * @return O objeto {@link Material} persistido no banco de dados.
     * @throws IllegalStateException se o usuário autenticado não for encontrado.
     * @throws DisciplinaNaoEncontradaException se a disciplina não existir ou não pertencer ao usuário autenticado.
     * @throws NullPointerException se o comando fornecido for {@code null}.
     */
    @Override
    @Transactional
    public Material executar(UploadMaterialCommand command) {
        Objects.requireNonNull(command, "O comando de upload não pode ser nulo.");

        // 1. Obtém o utilizador autenticado.
        String email = ((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();

        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        "Usuário autenticado não pôde ser encontrado."));

        // 2. Valida se a disciplina existe e pertence ao usuário autenticado.
        disciplinaRepository.findByIdAndUsuarioId(command.disciplinaId(), usuario.getId())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(command.disciplinaId()));

        // 3. Gera um nome de ficheiro único para o armazenamento físico.
        String extensao = StringUtils.getFilenameExtension(command.nomeOriginal());
        String nomeFicheiroStorage = UUID.randomUUID() + "." + extensao;

        // 4. Guarda o ficheiro no repositório de armazenamento.
        materialStoragePort.guardar(nomeFicheiroStorage, command.inputStream());

        // 5. Cria o objeto de domínio Material com metadados consistentes.
        Material novoMaterial = Material.criar(
                UUID.randomUUID(),
                command.nomeOriginal(),
                nomeFicheiroStorage,
                command.tipoArquivo(),
                command.tamanho(),
                usuario.getId(),
                command.disciplinaId(),
                null // A data será preenchida automaticamente via @CreationTimestamp
        );

        // 6. Persiste os metadados no repositório de dados.
        return materialRepository.salvar(novoMaterial);
    }
}
