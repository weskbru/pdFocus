package com.pdfocus.application.disciplina.port.entrada;

import java.util.UUID;

/**
 * Porta de entrada (caso de uso) responsável pela exclusão de uma disciplina.
 * <p>
 * Garante que apenas o utilizador autenticado possa remover uma disciplina
 * que lhe pertence, respeitando as regras de segurança e integridade de domínio.
 */
public interface DeletarDisciplinaUseCase {

    /**
     * Executa o fluxo de exclusão de uma disciplina associada ao utilizador autenticado.
     * <p>
     * A implementação deve:
     * <ul>
     *   <li>Verificar a propriedade da disciplina.</li>
     *   <li>Remover a entidade de forma transacional.</li>
     *   <li>Garantir a exclusão em cascata dos materiais e resumos, se aplicável.</li>
     * </ul>
     *
     * @param id O identificador único ({@link UUID}) da disciplina a ser removida.
     */
    void executar(UUID id);
}
