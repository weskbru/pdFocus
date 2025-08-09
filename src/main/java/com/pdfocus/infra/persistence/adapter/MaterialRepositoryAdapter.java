package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.core.exceptions.MaterialNaoEncontradoException; // Será criada em breve
import com.pdfocus.core.models.Material;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.MaterialEntity;
import com.pdfocus.infra.persistence.mapper.MaterialMapper;
import com.pdfocus.infra.persistence.repository.DisciplinaJpaRepository;
import com.pdfocus.infra.persistence.repository.MaterialJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa a porta de saída {@link MaterialRepository}
 * utilizando Spring Data JPA para a persistência de dados de Material.
 */
@Component
public class MaterialRepositoryAdapter implements MaterialRepository {

    private final MaterialJpaRepository jpaRepository;
    private final DisciplinaJpaRepository disciplinaJpaRepository;

    /**
     * Constrói o adaptador com a dependência do repositório Spring Data JPA.
     * @param jpaRepository O repositório para interagir com a tabela de materiais.
     */
    public MaterialRepositoryAdapter(MaterialJpaRepository jpaRepository, DisciplinaJpaRepository disciplinaJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.disciplinaJpaRepository = disciplinaJpaRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>NOTA:</strong> A conversão completa do objeto de domínio {@link Material} para
     * {@link MaterialEntity} requer uma instância de {@code DisciplinaEntity}.
     * A responsabilidade de buscar essa entidade e chamar o mapper correto
     * pertence à camada de serviço (UseCase), não a este adaptador.
     * Este método será finalizado quando o serviço de upload for implementado.
     * </p>
     */
    @Override
    @Transactional
    public Material salvar(Material material) {
        // 1. Busca a DisciplinaEntity necessária para o mapeamento.
        DisciplinaEntity disciplinaEntity = disciplinaJpaRepository.findById(material.getDisciplinaId())
                .orElseThrow(() -> new IllegalStateException("A disciplina associada ao material não foi encontrada. ID:" + material.getDisciplinaId()));

        // 2. Usa o mapper com a DisciplinaEntity buscada.
        MaterialEntity materialEntity = MaterialMapper.toEntity(material, disciplinaEntity);

        // 3. Salva a entidade no banco.
        MaterialEntity savedEntity = jpaRepository.save(materialEntity);

        // 4. Converte de volta para o domínio.
        return MaterialMapper.toDomain(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Material> listarPorDisciplinaEUsuario(UUID disciplinaId, UUID usuarioId) {
        List<MaterialEntity> entities = jpaRepository.findAllByDisciplina_IdAndUsuarioId(disciplinaId, usuarioId);
        return MaterialMapper.toDomainList(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Material> buscarPorIdEUsuario(UUID id, UUID usuarioId) {
        return jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .map(MaterialMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação primeiro busca o material pelo seu ID e pelo ID do
     * usuário proprietário para garantir a autorização. Se não for encontrado,
     * lança uma {@link MaterialNaoEncontradoException}.
     * </p>
     */
    @Override
    @Transactional
    public void deletarPorIdEUsuario(UUID id, UUID usuarioId) {
        // Busca a entidade garantindo que ela pertence ao usuário.
        MaterialEntity materialParaDeletar = jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new MaterialNaoEncontradoException(id)); // Precisamos criar esta exceção

        // Se encontrou, deleta.
        jpaRepository.delete(materialParaDeletar);
    }
}
