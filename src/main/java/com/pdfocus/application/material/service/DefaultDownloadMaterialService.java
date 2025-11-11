package com.pdfocus.application.material.service;

import com.pdfocus.application.material.port.entrada.DownloadMaterialUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.material.MaterialNaoEncontradoException;
import com.pdfocus.core.models.Material;
import com.pdfocus.core.models.Usuario;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Serviço responsável por implementar o caso de uso {@link DownloadMaterialUseCase},
 * que realiza o processo de download seguro de materiais pertencentes ao usuário autenticado.
 *
 * <p>Esta classe atua como uma orquestradora entre as diferentes portas da aplicação:
 * <ul>
 *     <li><b>{@link MaterialRepository}</b> → obtém os metadados e valida a posse do material.</li>
 *     <li><b>{@link MaterialStoragePort}</b> → carrega o arquivo físico a partir do sistema de armazenamento.</li>
 *     <li><b>{@link UsuarioRepository}</b> → recupera o contexto do usuário autenticado.</li>
 * </ul>
 * </p>
 *
 * <p><b>Segurança:</b> Garante que apenas o proprietário do material possa fazer o download.
 * Caso o material não pertença ao usuário, é lançada uma exceção de acesso não autorizado.
 * </p>
 *
 * <p><b>Design:</b> Essa abordagem respeita o princípio de separação de responsabilidades:
 * a lógica de autenticação, a persistência e o acesso ao storage são tratados por componentes distintos,
 * integrados via portas e adaptadores.
 * </p>
 */
@Service
public class DefaultDownloadMaterialService implements DownloadMaterialUseCase {

    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialStoragePort materialStoragePort;

    /**
     * Cria uma nova instância do serviço de download de materiais.
     *
     * @param materialRepository Repositório responsável pelo acesso aos metadados dos materiais.
     * @param usuarioRepository Repositório responsável pela recuperação de informações do usuário autenticado.
     * @param materialStoragePort Porta responsável por interagir com o sistema de armazenamento de arquivos.
     */
    public DefaultDownloadMaterialService(MaterialRepository materialRepository,
                                          UsuarioRepository usuarioRepository,
                                          MaterialStoragePort materialStoragePort) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.materialStoragePort = materialStoragePort;
    }

    /**
     * Executa o processo de download de um material, garantindo autenticação e integridade de acesso.
     *
     * <p>O fluxo segue os seguintes passos:
     * <ol>
     *     <li>Identifica o usuário autenticado via {@link SecurityContextHolder}.</li>
     *     <li>Valida a existência e a posse do material.</li>
     *     <li>Recupera o arquivo físico do armazenamento.</li>
     *     <li>Retorna um {@link DownloadResult} contendo o arquivo e seus metadados.</li>
     * </ol>
     * </p>
     *
     * @param id O identificador único do material a ser baixado.
     * @return Um {@link DownloadResult} contendo o {@link Resource} (arquivo físico) e o {@link Material} (metadados).
     * @throws IllegalStateException se o usuário autenticado não for encontrado no sistema.
     * @throws MaterialNaoEncontradoException se o material não existir ou não pertencer ao usuário autenticado.
     */
    @Override
    @Transactional(readOnly = true)
    public DownloadResult executar(UUID id) {
        // 1. Identifica o usuário logado de forma segura
        String email = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não pôde ser encontrado."));

        // 2. Valida a posse e existência do material
        Material material = materialRepository.buscarPorIdEUsuario(id, usuario.getId())
                .orElseThrow(() -> new MaterialNaoEncontradoException(id));

        // 3. Carrega o arquivo físico do armazenamento
        Resource resource = materialStoragePort.carregar(material.getNomeStorage());

        // 4. Retorna o pacote completo de download (arquivo + metadados)
        return new DownloadResult(resource, material);
    }
}
