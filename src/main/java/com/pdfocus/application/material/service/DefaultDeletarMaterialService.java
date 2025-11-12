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
 * Serviço responsável por implementar o caso de uso {@link DeletarMaterialUseCase},
 * que realiza a exclusão segura de um material pertencente ao usuário autenticado.
 *
 * <p>Este serviço garante que apenas o proprietário do material tenha permissão
 * para removê-lo, evitando que usuários diferentes possam manipular arquivos
 * ou registros alheios.</p>
 *
 * <p><b>Ordem de exclusão segura:</b> O processo segue uma sequência rigorosa
 * para evitar inconsistências entre o armazenamento físico e o banco de dados:
 * <ol>
 *     <li>Valida a identidade do usuário autenticado.</li>
 *     <li>Verifica a posse do material antes da exclusão.</li>
 *     <li>Remove o arquivo físico do armazenamento (S3, FileSystem etc.).</li>
 *     <li>Exclui o registro correspondente no banco de dados.</li>
 * </ol>
 * </p>
 *
 * <p><b>Design:</b> Essa abordagem desacopla responsabilidades
 * através de portas e adaptadores, mantendo a lógica de negócio independente
 * da tecnologia de persistência ou armazenamento usada.</p>
 */
@Service
public class DefaultDeletarMaterialService implements DeletarMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialStoragePort materialStoragePort;

    /**
     * Cria uma nova instância do serviço de deleção de materiais.
     *
     * @param materialRepository Repositório responsável pelo acesso aos dados de materiais.
     * @param usuarioRepository  Repositório responsável pela recuperação de informações do usuário.
     * @param materialStoragePort Porta responsável por interagir com o armazenamento físico dos arquivos.
     */
    public DefaultDeletarMaterialService(MaterialRepository materialRepository,
                                         UsuarioRepository usuarioRepository,
                                         MaterialStoragePort materialStoragePort) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.materialStoragePort = materialStoragePort;
    }

    /**
     * Executa a exclusão de um material identificado pelo seu {@link UUID}.
     *
     * <p>O método garante que:
     * <ul>
     *     <li>O material pertence ao usuário autenticado;</li>
     *     <li>O arquivo físico é removido antes da exclusão lógica no banco;</li>
     *     <li>Erros de consistência são prevenidos por meio de transação.</li>
     * </ul>
     * </p>
     *
     * @param id Identificador único do material a ser excluído.
     * @throws IllegalStateException se o usuário autenticado não for encontrado.
     * @throws MaterialNaoEncontradoException se o material não existir ou não pertencer ao usuário.
     */
    @Override
    @Transactional
    public void executar(UUID id) {
        // 1. Recupera o usuário autenticado a partir do contexto de segurança
        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não pôde ser encontrado."));

        // 2. Valida a posse e existência do material
        Material material = materialRepository.buscarPorIdEUsuario(id, usuario.getId())
                .orElseThrow(() -> new MaterialNaoEncontradoException(id));

        // 3. Exclui o arquivo físico e o registro lógico, nesta ordem
        materialStoragePort.apagar(material.getNomeStorage());
        materialRepository.deletarPorIdEUsuario(id, usuario.getId());
    }
}
