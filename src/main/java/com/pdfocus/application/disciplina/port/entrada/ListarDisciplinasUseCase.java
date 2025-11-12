package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.core.models.Disciplina;
import java.util.List;

/**
 * Porta de entrada (caso de uso) responsável por listar todas as disciplinas
 * associadas ao utilizador autenticado.
 * <p>
 * Este caso de uso representa uma operação de leitura pura (read-only),
 * sem efeitos colaterais, garantindo o isolamento dos dados por usuário.
 */
public interface ListarDisciplinasUseCase {

    /**
     * Executa a lógica de negócio para recuperar todas as disciplinas
     * pertencentes ao utilizador autenticado.
     * <p>
     * A identidade do utilizador é obtida implicitamente a partir do contexto
     * de segurança, garantindo que apenas suas próprias disciplinas sejam retornadas.
     *
     * @return Uma lista de {@link Disciplina} pertencentes ao utilizador.
     *         Pode ser vazia caso o utilizador ainda não possua disciplinas.
     */
    List<Disciplina> executar();
}
