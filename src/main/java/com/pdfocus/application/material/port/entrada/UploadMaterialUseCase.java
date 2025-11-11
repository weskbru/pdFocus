package com.pdfocus.application.material.port.entrada;

import com.pdfocus.application.material.dto.UploadMaterialCommand;
import com.pdfocus.core.models.Material;

/**
 * Porta de entrada (Input Port) responsável pelo caso de uso de upload
 * de um novo material associado ao utilizador autenticado.
 *
 * <p>
 * Este caso de uso centraliza as regras de negócio relacionadas ao envio,
 * validação e persistência de novos materiais, garantindo que a operação
 * respeite o contexto de segurança e a integridade das entidades de domínio.
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b></p>
 * <ul>
 *   <li>Pertence à camada de <b>aplicação</b> (Application Layer).</li>
 *   <li>Implementada por {@code DefaultUploadMaterialService}.</li>
 *   <li>Consumida pela camada <b>adapters/web</b> (controladores REST, por exemplo).</li>
 *   <li>Depende de portas de saída (Output Ports) como {@code MaterialRepository},
 *       {@code MaterialStoragePort}, {@code UsuarioRepository} e {@code DisciplinaRepository}.</li>
 * </ul>
 *
 * <p>
 * A identidade do utilizador é resolvida internamente via contexto de segurança
 * (ex: {@code SecurityContextHolder}), evitando exposição de dados sensíveis no método.
 * </p>
 */
public interface UploadMaterialUseCase {

    /**
     * Executa a lógica de negócio para o upload de um novo material.
     *
     * <p>
     * A implementação deve:
     * <ul>
     *   <li>Validar se a disciplina informada pertence ao utilizador autenticado;</li>
     *   <li>Gerar um nome único para o ficheiro e armazená-lo fisicamente;</li>
     *   <li>Persistir os metadados no repositório de materiais.</li>
     * </ul>
     * </p>
     *
     * @param command o comando ({@link UploadMaterialCommand}) contendo os dados do ficheiro
     *                e o identificador da disciplina associada.
     * @return o objeto de domínio {@link Material} recém-criado e persistido.
     */
    Material executar(UploadMaterialCommand command);
}
