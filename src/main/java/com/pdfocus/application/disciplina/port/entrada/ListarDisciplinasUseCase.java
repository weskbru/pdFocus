package com.pdfocus.application.disciplina.port.entrada;

import com.pdfocus.core.models.Disciplina;
import java.util.List;
import java.util.UUID;

/**
 * Caso de uso para listar as disciplinas de um usuário específico.
 */
public interface ListarDisciplinasUseCase {

    /**
     * Executa a busca de todas as disciplinas do usuário autenticado.
     * A identidade do usuário é obtida implicitamente a partir do contexto
     * de segurança, garantindo o isolamento dos dados.
     *
     * @return Uma lista de {@link Disciplina}. Pode ser vazia se o usuário
     * não tiver disciplinas.
     */
    List<Disciplina> executar();
}