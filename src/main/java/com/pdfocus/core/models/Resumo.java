package com.pdfocus.core.models;

import com.pdfocus.core.shared.Validador;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Representa um <strong>resumo de estudo</strong> gerado ou criado manualmente pelo usuário.
 * <p>
 * O {@code Resumo} é uma entidade central no domínio do Pdfocus, encapsulando o conteúdo textual
 * resultante da leitura e processamento de um material (geralmente um PDF). Ele mantém a
 * associação com o usuário autor, a disciplina e, opcionalmente, com o material de origem.
 * </p>
 *
 * <h2>Características</h2>
 * <ul>
 *     <li>Totalmente imutável — todos os campos são {@code final}.</li>
 *     <li>Instanciado apenas através de métodos de fábrica estáticos (padrão de criação controlada).</li>
 *     <li>Contém validações explícitas via {@link Validador} para garantir consistência do domínio.</li>
 *     <li>Serve como agregate root do contexto de “Resumos”.</li>
 * </ul>
 *
 * <h2>Contexto Arquitetural</h2>
 * <p>
 * A entidade é independente de frameworks (sem anotações JPA ou Spring),
 * garantindo portabilidade entre camadas e testabilidade. Sua contraparte
 * persistente é representada pela {@code ResumoEntity} na camada infra.
 * </p>
 *
 * @see com.pdfocus.infra.persistence.entity.ResumoEntity
 * @see com.pdfocus.core.models.Disciplina
 * @see com.pdfocus.core.shared.Validador
 */
public class Resumo {

    /** Identificador único do resumo. */
    private final UUID id;

    /** Identificador do usuário autor do resumo. */
    private final UUID usuarioId;

    /** Título descritivo do resumo. */
    private final String titulo;

    /** Texto completo ou sintetizado do conteúdo do resumo. */
    private final String conteudo;

    /** Disciplina ou área de estudo associada ao resumo. */
    private final Disciplina disciplina;

    /** Identificador opcional do material (PDF) de origem. */
    private final UUID materialId;

    /** Data e hora de criação do resumo (em UTC). */
    private final OffsetDateTime dataCriacao;

    /**
     * Construtor privado que define todos os campos do objeto.
     * <p>Utilizado apenas pelos métodos de fábrica controlados.</p>
     */
    private Resumo(UUID id, UUID usuarioId, String titulo, String conteudo,
                   Disciplina disciplina, UUID materialId, OffsetDateTime dataCriacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.disciplina = disciplina;
        this.materialId = materialId;
        this.dataCriacao = dataCriacao;
    }

    // ---------------------------------------------------------
    // MÉTODOS DE FÁBRICA
    // ---------------------------------------------------------

    /**
     * Cria um novo resumo independente, não associado diretamente a um material.
     *
     * @param id         Identificador único do resumo.
     * @param usuarioId  Identificador do usuário autor.
     * @param titulo     Título do resumo.
     * @param conteudo   Texto do resumo.
     * @param disciplina Disciplina associada.
     * @return uma nova instância válida de {@code Resumo}.
     * @throws IllegalArgumentException se algum campo obrigatório estiver ausente.
     */
    public static Resumo criar(UUID id, UUID usuarioId, String titulo, String conteudo, Disciplina disciplina) {
        Validador.requireNotNull(id, "Id não pode ser nulo");
        Validador.requireNotNull(usuarioId, "Usuário responsável não pode ser nulo");
        Validador.requireNotEmpty(titulo, "Título é obrigatório");
        Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");

        return new Resumo(id, usuarioId, titulo, conteudo, disciplina, null, OffsetDateTime.now());
    }

    /**
     * Cria um resumo vinculado a um material existente (geralmente PDF).
     *
     * @param id         Identificador único do resumo.
     * @param usuarioId  Identificador do usuário autor.
     * @param titulo     Título do resumo.
     * @param conteudo   Texto gerado ou redigido.
     * @param disciplina Disciplina associada.
     * @param materialId Identificador do material de origem.
     * @return uma nova instância válida de {@code Resumo}.
     * @throws IllegalArgumentException se algum campo obrigatório estiver ausente.
     */
    public static Resumo criarDeMaterial(UUID id, UUID usuarioId, String titulo, String conteudo,
                                         Disciplina disciplina, UUID materialId) {
        Validador.requireNotNull(id, "Id não pode ser nulo");
        Validador.requireNotNull(usuarioId, "Usuário responsável não pode ser nulo");
        Validador.requireNotEmpty(titulo, "Título é obrigatório");
        Validador.requireNotEmpty(conteudo, "Conteúdo é obrigatório");
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula");
        Validador.requireNotNull(materialId, "Material não pode ser nulo");

        return new Resumo(id, usuarioId, titulo, conteudo, disciplina, materialId, OffsetDateTime.now());
    }

    /**
     * Reconstrói um resumo a partir de dados persistidos (geralmente do banco).
     * <p>
     * Diferente dos outros métodos de fábrica, este aceita a data original de criação,
     * preservando o histórico da entidade.
     * </p>
     *
     * @param id          Identificador do resumo.
     * @param usuarioId   Identificador do autor.
     * @param titulo      Título do resumo.
     * @param conteudo    Texto do resumo.
     * @param disciplina  Disciplina associada.
     * @param materialId  Material de origem (opcional).
     * @param dataCriacao Data e hora da criação original.
     * @return uma instância reconstruída de {@code Resumo}.
     */
    public static Resumo reconstruir(UUID id, UUID usuarioId, String titulo, String conteudo,
                                     Disciplina disciplina, UUID materialId, OffsetDateTime dataCriacao) {
        Validador.requireNotNull(id, "Id da entidade não pode ser nulo");
        Validador.requireNotNull(usuarioId, "UsuarioId da entidade não pode ser nulo");
        Validador.requireNotNull(titulo, "Título da entidade não pode ser nulo");
        Validador.requireNotNull(disciplina, "Disciplina da entidade não pode ser nula");
        Validador.requireNotNull(dataCriacao, "Data de criação da entidade não pode ser nula");

        return new Resumo(id, usuarioId, titulo, conteudo, disciplina, materialId, dataCriacao);
    }

    // ---------------------------------------------------------
    // MÉTODOS DE ACESSO
    // ---------------------------------------------------------

    /** @return o identificador único do resumo. */
    public UUID getId() {
        return id;
    }

    /** @return o identificador do usuário autor. */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return o título do resumo. */
    public String getTitulo() {
        return titulo;
    }

    /** @return o conteúdo textual do resumo. */
    public String getConteudo() {
        return conteudo;
    }

    /** @return a disciplina ou área de estudo associada. */
    public Disciplina getDisciplina() {
        return disciplina;
    }

    /** @return o identificador do material de origem, se houver. */
    public UUID getMaterialId() {
        return materialId;
    }

    /** @return a data e hora de criação do resumo. */
    public OffsetDateTime getDataCriacao() {
        return dataCriacao;
    }
}
