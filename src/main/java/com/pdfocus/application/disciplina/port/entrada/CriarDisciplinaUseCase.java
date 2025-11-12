package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.application.disciplina.dto.CriarDisciplinaCommand;
import com.pdfocus.core.models.Disciplina;

/**
 * Porta de entrada (caso de uso) responsável pela criação de uma nova {@link Disciplina}.
 * <p>
 * Define o contrato de negócio que encapsula as regras para criar uma disciplina
 * associada ao utilizador autenticado, garantindo isolamento entre aplicação e domínio.
 */
public interface CriarDisciplinaUseCase {

    /**
     * Executa o fluxo de criação e persistência de uma nova disciplina.
     * <p>
     * A implementação deve:
     * <ul>
     *   <li>Validar os dados do comando recebido.</li>
     *   <li>Associar a disciplina ao utilizador autenticado (via contexto de segurança).</li>
     *   <li>Persistir a nova entidade de domínio.</li>
     * </ul>
     *
     * @param command O comando contendo os dados necessários para criar a disciplina.
     * @return A entidade {@link Disciplina} criada e persistida.
     */
    Disciplina executar(CriarDisciplinaCommand command);
}
