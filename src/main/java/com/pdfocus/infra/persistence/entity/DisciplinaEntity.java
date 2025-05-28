package com.pdfocus.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "disciplinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaEntity {

    /**
     * Identificador único da disciplina.
     * Mapeado como a chave primária na tabela do banco de dados.
     */
    @Id
    private UUID id;

    /**
     * Nome da disciplina.
     * Mapeado para uma coluna que não pode ser nula e tem um tamanho máximo de 255 caracteres.
     */
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    /**
     * Descrição detalhada da disciplina.
     * Mapeado para uma coluna do tipo TEXT para acomodar descrições mais longas.
     * Pode ser nulo.
     */
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

}