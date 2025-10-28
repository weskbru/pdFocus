package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade JPA para representar um Feedback no banco de dados.
 * Segue o mesmo padrão das outras entities (DisciplinaEntity, UsuarioEntity, etc).
 * Ajustado para PostgreSQL.
 */
@Entity
@Table(name = "feedbacks", indexes = {
        @Index(name = "idx_feedbacks_tipo", columnList = "tipo"),
        @Index(name = "idx_feedbacks_data_criacao", columnList = "data_criacao"),
        @Index(name = "idx_feedbacks_email", columnList = "email_usuario")
})
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo; // BUG, SUGGESTION, FEATURE, OTHER

    @Column(name = "rating")
    private Integer rating; // 1-5 (opcional)

    @Column(name = "mensagem", nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "email_usuario", length = 255)
    private String emailUsuario;

    @Column(name = "pagina", nullable = false, length = 500)
    private String pagina;

    @Column(name = "user_agent", nullable = false, columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public FeedbackEntity() {}

    /**
     * Construtor com parâmetros para criação manual.
     */
    public FeedbackEntity(String tipo, Integer rating, String mensagem, String emailUsuario,
                          String pagina, String userAgent, LocalDateTime dataCriacao) {
        this.tipo = tipo;
        this.rating = rating;
        this.mensagem = mensagem;
        this.emailUsuario = emailUsuario;
        this.pagina = pagina;
        this.userAgent = userAgent;
        this.dataCriacao = dataCriacao;
    }

    /**
     * Método chamado antes de persistir para setar a data de criação.
     */
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    @Override
    public String toString() {
        return "FeedbackEntity{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", rating=" + rating +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}