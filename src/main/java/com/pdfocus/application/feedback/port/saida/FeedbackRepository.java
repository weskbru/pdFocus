package com.pdfocus.application.feedback.port.saida;

import com.pdfocus.core.models.Feedback;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Porta de saída (Repository) para operações de persistência de Feedback.
 * Segue o mesmo padrão das outras portas de saída (DisciplinaRepository, etc).
 *
 * Responsabilidade: Abstrair o acesso aos dados de Feedback.
 */
public interface FeedbackRepository {

    /**
     * Salva um feedback no repositório.
     * Segue o padrão de nomenclatura dos outros repositories (salvar).
     *
     * @param feedback Entidade de domínio a ser persistida
     * @return Feedback salvo com ID gerado
     */
    Feedback salvar(Feedback feedback);

    /**
     * Busca um feedback pelo ID.
     * Segue o padrão Optional para evitar null pointers.
     *
     * @param id ID do feedback
     * @return Optional contendo o feedback se encontrado
     */
    Optional<Feedback> buscarPorId(Long id);

    /**
     * Verifica se existe algum feedback com a mesma mensagem (para evitar duplicatas).
     * Segue o padrão de métodos de verificação dos outros repositories.
     *
     * @param mensagem Mensagem do feedback
     * @return true se já existe feedback com mesma mensagem
     */
    boolean existeComMensagem(String mensagem);

    List<Feedback> findAll(Sort dataCriacao);
    long contarPorEmailEPeriodo(String email, LocalDateTime inicio, LocalDateTime fim);
}