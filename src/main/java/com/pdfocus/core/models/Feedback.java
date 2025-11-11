package com.pdfocus.core.models;

import com.pdfocus.core.exceptions.ValorInvalidoException;
import com.pdfocus.core.shared.Validador;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio que representa um Feedback enviado por um usuário.
 * Segue o mesmo padrão das outras entidades (Disciplina, Usuario, etc).
 */
public class Feedback {
    private Long id;
    private final String tipo; // BUG, SUGESTION, FEATURE, OTHER
    private final Integer rating; // 1-5 (opcional)
    private final String mensagem;
    private final String emailUsuario; // opcional
    private final String pagina;
    private final String userAgent;
    private final LocalDateTime dataCriacao;

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

    // Construtor para reconstituição do banco
    public Feedback(Long id, String tipo, Integer rating, String mensagem, String emailUsuario, String pagina, String userAgent, LocalDateTime dataCriacao) {
        this.id = id;
        this.tipo = tipo;
        this.rating = rating;
        this.mensagem = mensagem;
        this.emailUsuario = emailUsuario;
        this.pagina = pagina;
        this.userAgent = userAgent;
        this.dataCriacao = dataCriacao;
    }

    private void validarCamposObrigatorios(String tipo, String mensagem, String pagina, String userAgent) {
        // Usa requireNotEmpty do Validador (que já valida null e vazio)
        Validador.requireNotEmpty(tipo, "tipo do feedback");
        Validador.requireNotEmpty(mensagem, "mensagem do feedback");
        Validador.requireNotEmpty(pagina, "página de origem");
        Validador.requireNotEmpty(userAgent, "user agent");

        // Validar tipo permitido
        if (!tipo.toUpperCase().matches("BUG|SUGGESTION|FEATURE|OTHER")) {
            throw new ValorInvalidoException("Tipo de feedback deve ser: BUG, SUGGESTION, FEATURE ou OTHER");
        }
    }

    private void validarRating(Integer rating) {
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new ValorInvalidoException("Rating deve estar entre 1 e 5");
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public Integer getRating() { return rating; }
    public String getMensagem() { return mensagem; }
    public String getEmailUsuario() { return emailUsuario; }
    public String getPagina() { return pagina; }
    public String getUserAgent() { return userAgent; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

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