package com.pdfocus.application.feedback.dto;

import com.pdfocus.core.exceptions.ValorInvalidoException;
import com.pdfocus.core.models.Feedback; // <--- Importante: Adicione este import
import com.pdfocus.core.shared.Validador;

/**
 * Representa a estrutura de dados para o recebimento de feedbacks enviados
 * pelo frontend.
 */
public class FeedbackRequest {

    private String tipo;
    private Integer rating;
    private String mensagem;
    private String emailUsuario;
    private String pagina;
    private String userAgent;

    public FeedbackRequest() {}

    public FeedbackRequest(String tipo, Integer rating, String mensagem, String emailUsuario, String pagina, String userAgent) {
        this.tipo = tipo;
        this.rating = rating;
        this.mensagem = mensagem;
        this.emailUsuario = emailUsuario;
        this.pagina = pagina;
        this.userAgent = userAgent;
    }

    // Getters e Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }

    public String getPagina() { return pagina; }
    public void setPagina(String pagina) { this.pagina = pagina; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    /**
     * Validações manuais de domínio.
     */
    public void validar() {
        // Valida se os campos existem (Isso é bom manter)
        this.tipo = Validador.requireNotEmpty(tipo, "Tipo do feedback");
        this.mensagem = Validador.requireNotEmpty(mensagem, "Mensagem do feedback");
        // this.pagina = Validador.requireNotEmpty(pagina, "Página de origem"); // Se der erro, comente
        // this.userAgent = Validador.requireNotEmpty(userAgent, "User agent"); // Se der erro, comente

        // --- VALIDAÇÃO DE TIPO (MANTIDA) ---
        if (!tipo.matches("BUG|SUGGESTION|FEATURE|OTHER")) {
            throw new ValorInvalidoException("Tipo inválido: " + tipo);
        }

        // --- VALIDAÇÃO DE RATING (AJUSTADA PARA IGNORAR 0) ---
        // Adicionei "&& rating != 0" para proteger caso o front mande 0
        if (rating != null && rating != 0 && (rating < 1 || rating > 5)) {
            throw new ValorInvalidoException("Rating deve estar entre 1 e 5");
        }

        // --- VALIDAÇÃO DE TAMANHO (REMOVIDA TEMPORARIAMENTE) ---
        /*
        if (mensagem.length() < 10) {
            throw new ValorInvalidoException("Mensagem deve ter pelo menos 10 caracteres");
        }
        */

        // --- VALIDAÇÃO DE EMAIL (MANTIDA MAS SEGURA) ---
        if (emailUsuario != null && !emailUsuario.isBlank()) {
            if (!emailUsuario.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new ValorInvalidoException("Email deve ser válido");
            }
        }
    }

    /**
     * [NOVO] Método essencial para converter o DTO em Entidade de Domínio.
     * O Service utiliza este método.
     */
    public Feedback toDomain() {
        return new Feedback(
                this.tipo,
                this.rating,
                this.mensagem,
                this.emailUsuario, // Passa null se for null, o domínio aceita
                this.pagina,
                this.userAgent
        );
    }

    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "tipo='" + tipo + '\'' +
                ", rating=" + rating +
                ", mensagem='" + (mensagem != null ? mensagem.length() + " chars" : "null") + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", pagina='" + pagina + '\'' +
                '}';
    }
}