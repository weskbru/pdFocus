package com.pdfocus.application.material.port.entrada;

import com.pdfocus.core.models.Material;
import org.springframework.core.io.Resource;
import java.util.UUID;

/**
 * Porta de entrada (Input Port) responsável pelo caso de uso de download de materiais.
 *
 * <p>
 * Este caso de uso lida com a recuperação e disponibilização de arquivos enviados
 * pelo usuário autenticado, garantindo que apenas o proprietário do material tenha
 * acesso ao seu conteúdo.
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b></p>
 * <ul>
 *   <li>Pertence à camada de <b>aplicação</b> (Application Layer).</li>
 *   <li>Implementada por {@code DefaultDownloadMaterialService} (ou equivalente).</li>
 *   <li>Consumida pela camada <b>adapters/web</b>, normalmente via controladores REST.</li>
 *   <li>Depende de portas de saída (Output Ports) para buscar os dados e arquivos persistidos.</li>
 * </ul>
 *
 * <p>
 * A responsabilidade de verificação de segurança (autorização e pertencimento do material)
 * é exclusiva da implementação concreta deste caso de uso.
 * </p>
 */
public interface DownloadMaterialUseCase {

    /**
     * DTO interno que encapsula o resultado de uma operação de download,
     * reunindo o arquivo propriamente dito e seus metadados de domínio.
     *
     * <p>
     * Este objeto é retornado pela camada de aplicação e consumido
     * pela camada de apresentação (ex: controladores REST),
     * que transforma o {@link Resource} em uma resposta HTTP adequada.
     * </p>
     *
     * @param resource O recurso físico (arquivo) a ser transmitido.
     * @param material Os metadados associados ao material no domínio.
     */
    record DownloadResult(Resource resource, Material material) {}

    /**
     * Executa a lógica de negócio responsável por localizar e preparar
     * um material específico para download.
     *
     * <p>
     * A implementação deve validar se o material pertence ao usuário logado
     * e garantir que o arquivo exista no armazenamento.
     * </p>
     *
     * @param id o identificador único ({@link UUID}) do material a ser baixado.
     * @return um objeto {@link DownloadResult} contendo o arquivo e seus metadados.
     */
    DownloadResult executar(UUID id);
}
