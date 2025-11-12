package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.ValorInvalidoException;
import com.pdfocus.core.shared.Validador;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa um <strong>feedback</strong> enviado por um usuário do sistema.
 * <p>
 * A entidade {@code Feedback} captura comentários, sugestões ou relatos de erro
 * feitos diretamente na aplicação, permitindo rastrear a experiência do usuário.
 * </p>
 *
 * <p>
 * Todos os campos obrigatórios são validados no momento da criação.
 * O feedback é imutável, exceto pelo campo {@code id}, que é definido
 * pela camada de persistência.
 * </p>
 *
 * @see com.pdfocus.infra.persistence.entity.FeedbackEntity
 */
public class Feedback {

    /** Identificador único do feedback. */
    private Long id;

    /** Tipo do feedback: BUG, SUGGESTION, FEATURE ou OTHER. */
    private final String tipo;

    /** Avaliação opcional do usuário (1 a 5). */
    private final Integer rating;

    /** Mensagem textual do feedback. */
    private final String mensagem;

    /** E-mail do usuário (opcional). */
    private final String emailUsuario;

    /** Página ou rota de onde o feedback foi enviado. */
    private final String pagina;

    /** Informações do agente de navegação (browser, SO, etc.). */
    private final String userAgent;

    /** Data e hora em que o feedback foi criado. */
    private final LocalDateTime dataCriacao;

    /**
     * Cria um novo {@code Feedback} com validação completa.
     *
     * @param tipo          Tipo do feedback (BUG, SUGGESTION, FEATURE, OTHER).
     * @param rating        Avaliação opcional (1–5).
     * @param mensagem      Texto da mensagem.
     * @param emailUsuario  E-mail do autor (opcional).
     * @param pagina        Página de origem.
     * @param userAgent     Agente de navegação.
     * @throws ValorInvalidoException se algum campo tiver valor inválido.
     */
    public Feedback(String tipo, Integer rating, String mensagem, String emailUsuario, String pagina, String userAgent) {
        validarCamposObrigatorios(tipo, mensagem, pagina, userAgent);
        validarRating(rating);

        this.tipo = tipo.toUpperCase();
        this.rating = rating;
        this.mensagem = mensagem.trim();
        this.emailUsuario = emailUsuario != null ? emailUsuario.trim() : null;
        this.pagina = pagina;
        this.userAgent = userAgent;
        this.dataCriacao = LocalDateTime.now();
    }

    /**
     * Construtor usado para reconstituição de dados persistidos.
     * <p>
     * Diferente do construtor principal, não executa validações de domínio.
     * </p>
     */
    public Feedback(Long id, String tipo, Integer rating, String mensagem,
                    String emailUsuario, String pagina, String userAgent, LocalDateTime dataCriacao) {
        this.id = id;
        this.tipo = tipo;
        this.rating = rating;
        this.mensagem = mensagem;
        this.emailUsuario = emailUsuario;
        this.pagina = pagina;
        this.userAgent = userAgent;
        this.dataCriacao = dataCriacao;
    }

    // --- Validações internas ---

    private void validarCamposObrigatorios(String tipo, String mensagem, String pagina, String userAgent) {
        Validador.requireNotEmpty(tipo, "tipo do feedback");
        Validador.requireNotEmpty(mensagem, "mensagem do feedback");
        Validador.requireNotEmpty(pagina, "página de origem");
        Validador.requireNotEmpty(userAgent, "user agent");

        if (!tipo.toUpperCase().matches("BUG|SUGGESTION|FEATURE|OTHER")) {
            throw new ValorInvalidoException("Tipo de feedback deve ser: BUG, SUGGESTION, FEATURE ou OTHER");
        }
    }

    private void validarRating(Integer rating) {
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new ValorInvalidoException("Rating deve estar entre 1 e 5");
        }
    }

    // --- Getters ---

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public Integer getRating() { return rating; }
    public String getMensagem() { return mensagem; }
    public String getEmailUsuario() { return emailUsuario; }
    public String getPagina() { return pagina; }
    public String getUserAgent() { return userAgent; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    // --- Métodos utilitários ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", rating=" + rating +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
