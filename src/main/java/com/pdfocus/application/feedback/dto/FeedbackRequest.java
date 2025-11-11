package com.pdfocus.application.feedback.dto;

import com.pdfocus.core.exceptions.ValorInvalidoException;
import com.pdfocus.core.shared.Validador;

/**
 * DTO para receber dados de feedback do frontend.
 * Segue o mesmo padrão dos outros Command DTOs com validação manual usando seu Validador.
 */
public class FeedbackRequest {

    private String tipo;
    private Integer rating;
    private String mensagem;
    private String emailUsuario;
    private String pagina;
    private String userAgent;

    // Construtor padrão para deserialização JSON
    public FeedbackRequest() {}

    // Construtor com parâmetros para testes
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
     * Método de validação manual seguindo o padrão do seu projeto.
     * Usa exatamente os mesmos métodos do Validador existente.
     */
    public void validar() {
        // Usa requireNotEmpty do seu Validador (que já valida null e vazio)
        this.tipo = Validador.requireNotEmpty(tipo, "Tipo do feedback");
        this.mensagem = Validador.requireNotEmpty(mensagem, "Mensagem do feedback");
        this.pagina = Validador.requireNotEmpty(pagina, "Página de origem");
        this.userAgent = Validador.requireNotEmpty(userAgent, "User agent");

        // Validar tipo permitido (validação específica do domínio)
        if (!tipo.matches("BUG|SUGGESTION|FEATURE|OTHER")) {
            throw new ValorInvalidoException(
                    "Tipo de feedback deve ser: BUG, SUGGESTION, FEATURE ou OTHER"
            );
        }

        // Validar rating se fornecido
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new ValorInvalidoException(
                    "Rating deve estar entre 1 e 5"
            );
        }

        // Validar tamanho da mensagem
        if (mensagem.length() < 10) {
            throw new ValorInvalidoException(
                    "Mensagem deve ter pelo menos 10 caracteres"
            );
        }

        if (mensagem.length() > 1000) {
            throw new ValorInvalidoException(
                    "Mensagem deve ter no máximo 1000 caracteres"
            );
        }

        // Validar email se fornecido (usando requireNotEmpty para null/vazio)
        if (emailUsuario != null && !emailUsuario.isEmpty()) {
            this.emailUsuario = Validador.requireNotEmpty(emailUsuario, "Email do usuário");

            // Validação básica de formato de email
            if (!emailUsuario.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new ValorInvalidoException(
                        "Email deve ser válido"
                );
            }
        }
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