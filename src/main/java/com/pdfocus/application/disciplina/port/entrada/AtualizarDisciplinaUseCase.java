package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.application.disciplina.dto.AtualizarDisciplinaCommand;
import com.pdfocus.core.models.Disciplina;
import java.util.UUID;

/**
 * Porta de entrada (caso de uso) responsável por atualizar os dados de uma disciplina existente.
 * <p>
 * Este contrato define a operação de negócio para modificar os atributos de uma disciplina
 * pertencente ao utilizador autenticado, garantindo isolamento entre camadas de aplicação e domínio.
 */
public interface AtualizarDisciplinaUseCase {

    /**
     * Executa o fluxo de atualização de uma disciplina pertencente ao utilizador logado.
     * <p>
     * A implementação deve garantir:
     * <ul>
     *   <li>Verificação de propriedade (a disciplina pertence ao utilizador autenticado).</li>
     *   <li>Validação de dados conforme as regras do domínio.</li>
     *   <li>Persistência e retorno da entidade atualizada.</li>
     * </ul>
     *
     * @param id      O identificador único da disciplina a ser atualizada.
     * @param command O comando contendo os novos dados da disciplina.
     * @return A entidade {@link Disciplina} após a atualização.
     */
    Disciplina executar(UUID id, AtualizarDisciplinaCommand command);
}
