package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode; // Opcional, mas recomendado

import java.util.UUID;

/**
 * Entidade JPA que representa um resumo no banco de dados.
 * Esta classe é mapeada para a tabela "resumos" e está associada
 * a um usuário e a uma {@link DisciplinaEntity}.
 * Utiliza Lombok para a geração de getters, setters e construtores.
 */
@Entity
@Table(name = "resumos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Boa prática para entidades JPA, baseia-se apenas no ID.
public class ResumoEntity {

    /**
     * Identificador único do resumo.
     * Mapeado como a chave primária na tabela do banco de dados.
     */
    @Id
    @Column(name = "id")
    private UUID id;

    /**
     * Identificador único do usuário proprietário deste resumo.
     * Garante que cada resumo esteja associado a um usuário específico.
     * Mapeado para a coluna "usuario_id", que não pode ser nula.
     */
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    /**
     * Título do resumo.
     * Mapeado para uma coluna que não pode ser nula e tem um tamanho máximo de 255 caracteres.
     */
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    /**
     * Conteúdo detalhado do resumo.
     * Mapeado para uma coluna do tipo TEXT para acomodar textos mais longos.
     * Este campo pode ser nulo se o resumo for apenas um título ou referência.
     */
    @Column(name = "conteudo", columnDefinition = "TEXT")
    private String conteudo;

    /**
     * A disciplina à qual este resumo está associado.
     * Representa um relacionamento Muitos-Para-Um (Muitos Resumos podem pertencer a Uma Disciplina).
     * A disciplina é carregada de forma LAZY (preguiçosa) para otimizar a performance,
     * ou seja, os dados da disciplina só são buscados do banco quando explicitamente acessados.
     * A coluna de junção no banco de dados é "disciplina_id" e não pode ser nula.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private DisciplinaEntity disciplina;


}