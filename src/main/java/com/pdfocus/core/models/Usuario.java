package com.pdfocus.core.models;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Representa um <strong>usuário</strong> dentro do domínio central do sistema Pdfocus.
 * <p>
 * A entidade {@code Usuario} encapsula as informações essenciais de autenticação e identificação
 * do usuário, sem conter qualquer lógica de infraestrutura ou persistência.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Manter os dados fundamentais de um usuário: ID, nome, e-mail e senha (hash).</li>
 *     <li>Garantir imutabilidade — uma vez criado, o estado do objeto não pode ser alterado.</li>
 *     <li>Servir como base para o mapeamento de persistência em {@code UsuarioEntity} na camada infra.</li>
 * </ul>
 *
 * <h2>Design</h2>
 * <p>
 * Esta classe é simples e livre de anotações do Spring ou JPA,
 * respeitando o princípio de <em>separação de domínios</em> da Arquitetura Limpa.
 * </p>
 *
 * @see com.pdfocus.infra.persistence.entity.UsuarioEntity
 */
public class Usuario {

    /** Identificador único e imutável do usuário. */
    private final UUID id;

    /** Nome completo do usuário. */
    private final String nome;

    /** Endereço de e-mail utilizado para login e comunicação. */
    private final String email;

    /**
     * Hash criptográfico da senha do usuário.
     * <p><strong>Nunca</strong> deve armazenar a senha em texto puro.</p>
     */
    private final String senhaHash;

    /** Indica se o usuário confirmou seu e-mail e pode fazer login. */
    private boolean ativo;

    /** Quantidade de resumos gerados no dia atual. */
    private Integer resumosHoje;

    /** Data da última vez que o usuário utilizou um recurso limitado. */
    private LocalDate dataUltimoUso;

    // --- Controle de Feedbacks (NOVOS CAMPOS) ---
    private Integer feedbacksHoje;
    private LocalDate dataUltimoFeedback;

    /**
     * Cria uma nova instância de {@code Usuario} com todos os campos definidos.
     *
     * @param id            Identificador único do usuário.
     * @param nome          Nome do usuário.
     * @param email         E-mail utilizado para login.
     * @param senhaHash     Hash da senha (BCrypt ou equivalente).
     * @param resumosHoje
     * @param dataUltimoUso
     */
    public Usuario(UUID id, String nome, String email, String senhaHash, boolean ativo,
                   Integer resumosHoje, LocalDate dataUltimoUso,
                   Integer feedbacksHoje, LocalDate dataUltimoFeedback) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.ativo = ativo;

        // Inicialização segura para evitar NullPointerException
        this.resumosHoje = resumosHoje != null ? resumosHoje : 0;
        this.dataUltimoUso = dataUltimoUso != null ? dataUltimoUso : LocalDate.now();

        this.feedbacksHoje = feedbacksHoje != null ? feedbacksHoje : 0;
        this.dataUltimoFeedback = dataUltimoFeedback != null ? dataUltimoFeedback : LocalDate.now();
    }

    /**
     * Cria uma nova instância de {@code Usuario} sem ID definido.
     * <p>
     * Usado em fluxos de criação, onde o ID será gerado posteriormente
     * pela camada de persistência.
     * </p>
     *
     * @param nome       Nome do usuário.
     * @param email      E-mail utilizado para login.
     * @param senhaHash  Hash da senha (BCrypt ou equivalente).
     */
    public Usuario(String nome, String email, String senhaHash) {
        this.id = null;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.ativo = false;

        // Inicia zerado
        this.resumosHoje = 0;
        this.dataUltimoUso = LocalDate.now();
        this.feedbacksHoje = 0;
        this.dataUltimoFeedback = LocalDate.now();
    }

    // --- Getters e Setters ---

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }

    // Getters/Setters de Resumo
    public Integer getResumosHoje() { return resumosHoje; }
    public void setResumosHoje(Integer resumosHoje) { this.resumosHoje = resumosHoje; }
    public LocalDate getDataUltimoUso() { return dataUltimoUso; }
    public void setDataUltimoUso(LocalDate dataUltimoUso) { this.dataUltimoUso = dataUltimoUso; }

    // Getters/Setters de Feedback (NECESSÁRIOS PARA O SERVICE)
    public Integer getFeedbacksHoje() { return feedbacksHoje; }
    public void setFeedbacksHoje(Integer feedbacksHoje) { this.feedbacksHoje = feedbacksHoje; }

    public LocalDate getDataUltimoFeedback() { return dataUltimoFeedback; }
    public void setDataUltimoFeedback(LocalDate dataUltimoFeedback) { this.dataUltimoFeedback = dataUltimoFeedback; }

}
