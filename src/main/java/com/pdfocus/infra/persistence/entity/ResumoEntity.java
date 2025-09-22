package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(of = "id")
public class ResumoEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "conteudo", columnDefinition = "TEXT")
    private String conteudo;

    /**
     * NOVO CAMPO: Identificador do material (PDF) associado a este resumo.
     * Permite rastrear qual PDF deu origem ao resumo.
     * Pode ser nulo para resumos manuais que não foram baseados em PDF.
     */
    @Column(name = "material_id")
    private UUID materialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private DisciplinaEntity disciplina;
}