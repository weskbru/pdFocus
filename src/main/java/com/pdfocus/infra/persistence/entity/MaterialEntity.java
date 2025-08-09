package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entidade JPA que representa um material de estudo (arquivo) no banco de dados.
 * Mapeada para a tabela "materiais".
 */
@Entity
@Table(name = "materiais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MaterialEntity {

    /**
     * O identificador único do material (Chave Primária).
     */
    @Id
    private UUID id;

    /**
     * O nome original do arquivo, como enviado pelo usuário (ex: "apostila_aula_1.pdf").
     */
    @Column(name = "nome_original", nullable = false)
    private String nomeOriginal;

    /**
     * O nome único gerado para o arquivo no sistema de armazenamento.
     * Isso evita conflitos de nomes e adiciona uma camada de segurança.
     * (ex: "a1b2c3d4-e5f6-7890-1234-567890abcdef.pdf").
     */
    @Column(name = "nome_storage", nullable = false, unique = true)
    private String nomeStorage;

    /**
     * O tipo MIME do arquivo (ex: "application/pdf", "image/png").
     */
    @Column(name = "tipo_arquivo", nullable = false)
    private String tipoArquivo;

    /**
     * O tamanho do arquivo em bytes.
     */
    @Column(name = "tamanho", nullable = false)
    private long tamanho;

    /**
     * O identificador único do usuário proprietário deste material.
     */
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    /**
     * A disciplina à qual este material está associado.
     * Representa um relacionamento Muitos-Para-Um (Muitos Materiais podem pertencer a Uma Disciplina).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private DisciplinaEntity disciplina;

}
