package com.pdfocus.application.feedback.dto;

import com.pdfocus.core.exceptions.ValorInvalidoException;
import com.pdfocus.core.shared.Validador;

/**
 * Representa a estrutura de dados para o recebimento de feedbacks enviados
 * pelo frontend. Esse DTO encapsula as informações essenciais fornecidas pelo
 * utilizador e realiza validações de domínio antes de ser processado pelo caso de uso.
 *
 * <p>Os tipos válidos de feedback são:
 * <ul>
 *   <li><b>BUG</b> – Relato de erro ou mau funcionamento</li>
 *   <li><b>SUGGESTION</b> – Sugestão de melhoria</li>
 *   <li><b>FEATURE</b> – Solicitação de nova funcionalidade</li>
 *   <li><b>OTHER</b> – Qualquer outro tipo de feedback</li>
 * </ul>
 *
 * <p>A validação é feita de forma manual através do método {@link #validar()},
 * seguindo o padrão adotado em toda a aplicação (uso do {@link Validador}).
 */
public class FeedbackRequest {

    /** Tipo do feedback (ex.: BUG, SUGGESTION, FEATURE, OTHER). */
    private String tipo;

    /** Avaliação numérica opcional (1 a 5). */
    private Integer rating;

    /** Mensagem detalhando o feedback fornecido pelo utilizador. */
    private String mensagem;

    /** Endereço de e-mail opcional do utilizador (se fornecido). */
    private String emailUsuario;

    /** Página ou rota da aplicação onde o feedback foi submetido. */
    private String pagina;

    /** Informações sobre o agente do utilizador (navegador, sistema operacional, etc.). */
    private String userAgent;

    /** Construtor padrão exigido para a desserialização JSON. */
    public FeedbackRequest() {}

    /**
     * Construtor auxiliar utilizado em testes ou criação manual.
     *
     * @param tipo tipo do feedback (BUG, SUGGESTION, FEATURE ou OTHER)
     * @param rating avaliação opcional de 1 a 5
     * @param mensagem texto do feedback
     * @param emailUsuario email do utilizador (opcional)
     * @param pagina página onde o feedback foi enviado
     * @param userAgent agente do utilizador (navegador, sistema)
     */
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
     * Executa a validação de domínio sobre os campos do DTO.
     *
     * <p>Regras principais:
     * <ul>
     *   <li>Campos obrigatórios: {@code tipo}, {@code mensagem}, {@code pagina}, {@code userAgent}</li>
     *   <li>O tipo deve ser um dos valores: BUG, SUGGESTION, FEATURE, OTHER</li>
     *   <li>O rating (se informado) deve estar entre 1 e 5</li>
     *   <li>A mensagem deve conter entre 10 e 1000 caracteres</li>
     *   <li>O e-mail (se informado) deve possuir formato válido</li>
     * </ul>
     *
     * @throws ValorInvalidoException se algum campo não atender às regras de domínio
     */
    public void validar() {
        this.tipo = Validador.requireNotEmpty(tipo, "Tipo do feedback");
        this.mensagem = Validador.requireNotEmpty(mensagem, "Mensagem do feedback");
        this.pagina = Validador.requireNotEmpty(pagina, "Página de origem");
        this.userAgent = Validador.requireNotEmpty(userAgent, "User agent");

        if (!tipo.matches("BUG|SUGGESTION|FEATURE|OTHER")) {
            throw new ValorInvalidoException(
                    "Tipo de feedback deve ser: BUG, SUGGESTION, FEATURE ou OTHER"
            );
        }

        if (rating != null && (rating < 1 || rating > 5)) {
            throw new ValorInvalidoException("Rating deve estar entre 1 e 5");
        }

        if (mensagem.length() < 10) {
            throw new ValorInvalidoException("Mensagem deve ter pelo menos 10 caracteres");
        }

        if (mensagem.length() > 1000) {
            throw new ValorInvalidoException("Mensagem deve ter no máximo 1000 caracteres");
        }

        if (emailUsuario != null && !emailUsuario.isEmpty()) {
            this.emailUsuario = Validador.requireNotEmpty(emailUsuario, "Email do usuário");
            if (!emailUsuario.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new ValorInvalidoException("Email deve ser válido");
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
