package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Material;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.MaterialEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe utilitária responsável por mapear (converter) objetos entre
 * a entidade de domínio {@link Material} e a entidade JPA {@link MaterialEntity}.
 * Esta classe é um componente central da camada de infraestrutura, garantindo
 * que o nosso núcleo de domínio permaneça desacoplado dos detalhes de persistência.
 */
public final class MaterialMapper {

    /**
     * Construtor privado para impedir a instanciação, pois esta é uma classe utilitária
     * com métodos estáticos.
     */
    private MaterialMapper() {
        // Impede a instanciação.
    }

    /**
     * Converte um objeto de domínio {@link Material} para uma entidade JPA {@link MaterialEntity}.
     * Este método requer a {@link DisciplinaEntity} associada para garantir a consistência
     * da relação no banco de dados.
     *
     * @param material O objeto de domínio a ser convertido.
     * @param disciplinaEntity A entidade JPA da disciplina à qual o material pertence.
     * @return A {@link MaterialEntity} correspondente, pronta para ser persistida.
     */
    public static MaterialEntity toEntity(Material material, DisciplinaEntity disciplinaEntity) {
        if (material == null) {
            return null;
        }

        // 1. Criamos uma instância vazia da entidade (usando o construtor que o Lombok @NoArgsConstructor cria).
        MaterialEntity entity = new MaterialEntity();

        // 2. Definimos cada campo usando seus setters (que o Lombok @Setter cria).
        entity.setId(material.getId());
        entity.setNomeOriginal(material.getNomeOriginal());
        entity.setNomeStorage(material.getNomeStorage());
        entity.setTipoArquivo(material.getTipoArquivo());
        entity.setTamanho(material.getTamanho());
        entity.setUsuarioId(material.getUsuarioId());
        entity.setDisciplina(disciplinaEntity); // Associa a entidade da disciplina

        return entity;
    }

    /**
     * Converte uma entidade JPA {@link MaterialEntity} de volta para um objeto de domínio {@link Material}.
     * Este método é crucial para ler dados do banco e usá-los na lógica de negócio.
     *
     * @param materialEntity A entidade JPA a ser convertida.
     * @return O objeto de domínio {@link Material} correspondente.
     */
    public static Material toDomain(MaterialEntity materialEntity) {
        if (materialEntity == null) {
            return null;
        }

        UUID disciplinaId = (materialEntity.getDisciplina() != null) ? materialEntity.getDisciplina().getId() : null;

        // Agora, lemos o campo 'dataUpload' da entidade e o passamos para o método
        // de fábrica 'criar()' do nosso modelo de domínio, satisfazendo o novo contrato.
        return Material.criar(
                materialEntity.getId(),
                materialEntity.getNomeOriginal(),
                materialEntity.getNomeStorage(),
                materialEntity.getTipoArquivo(),
                materialEntity.getTamanho(),
                materialEntity.getUsuarioId(),
                disciplinaId,
                materialEntity.getDataUpload()
        );
    }

    /**
     * Converte uma lista de entidades JPA {@link MaterialEntity} para uma lista de objetos de domínio {@link Material}.
     *
     * @param entities A lista de entidades a ser convertida.
     * @return Uma lista de {@link Material}, ou uma lista vazia se a entrada for nula ou vazia.
     */
    public static List<Material> toDomainList(List<MaterialEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(MaterialMapper::toDomain)
                .collect(Collectors.toList());
    }
}
