package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.mapper.DisciplinaMapper;
import com.pdfocus.infra.persistence.repository.DisciplinaJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // <-- Import necessário
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa a porta de saída {@link DisciplinaRepository}
 * utilizando Spring Data JPA para persistência de dados de Disciplina.
 * <p>
 * Esta classe atua como a "ponte" entre a camada de Aplicação (agnóstica)
 * e a camada de Infraestrutura (concreta, com Spring Data).
 * </p>
 * <p>
 * Foi refatorada para aderir ao Pilar 3 (Boas Práticas), removendo
 * métodos duplicados e implementando a segurança de multi-tenancy.
 * </p>
 *
 * @see com.pdfocus.application.disciplina.port.saida.DisciplinaRepository (Interface/Porta)
 * @see com.pdfocus.infra.persistence.repository.DisciplinaJpaRepository (Repositório JPA)
 * @see com.pdfocus.infra.persistence.mapper.DisciplinaMapper (Mapeador)
 */
@Component
public class DisciplinaRepositoryAdapter implements DisciplinaRepository {

    private final DisciplinaJpaRepository jpaRepository;

    /**
     * Injeta a implementação concreta do Spring Data JPA.
     *
     * @param jpaRepository O repositório Spring Data que lida com a DisciplinaEntity.
     */
    public DisciplinaRepositoryAdapter(DisciplinaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converte o modelo de domínio para entidade, persiste e
     * converte de volta para o domínio.
     * </p>
     */
    @Override
    @Transactional
    public Disciplina salvar(Disciplina disciplina) {
        DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(disciplina);
        DisciplinaEntity savedEntity = jpaRepository.save(disciplinaEntity);
        return DisciplinaMapper.toDomain(savedEntity);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Este é o método 'findById' padrão.
     * </p>
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> findById(UUID id) {
        Optional<DisciplinaEntity> optionalDisciplinaEntity = jpaRepository.findById(id);
        return optionalDisciplinaEntity.map(DisciplinaMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação segura busca a disciplina pelo seu ID e pelo ID do
     * usuário proprietário para garantir a autorização (multi-tenancy).
     * </p>
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> findByIdAndUsuarioId(UUID id, UUID usuarioId) {
        // Delega para o método customizado do DisciplinaJpaRepository
        return jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .map(DisciplinaMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     * <p>
     * A lógica é delegada diretamente para o método 'deleteById', que é
     * fornecido por padrão pelo JpaRepository.
     * </p>
     */
    @Override
    @Transactional
    public void deletarPorId(UUID id) {
        // Validações de segurança (se o usuário pode deletar)
        // devem ser feitas na camada de SERVIÇO (Application)
        // antes de chamar este método.
        jpaRepository.deleteById(id);
    }


    /**
     * {@inheritDoc}
     * <p>
     * Delega para o método customizado do DisciplinaJpaRepository.
     * </p>
     */
    @Override
    public long countByUsuario(Usuario usuario) {
        // Delega para o método customizado do DisciplinaJpaRepository
        return jpaRepository.countByUsuarioId(usuario.getId());
    }

    // --- ESTE É O MÉTODO QUE FALTAVA (O ERRO DO LOG) ---
    /**
     * {@inheritDoc}
     * <p>
     * Implementa o método exigido pela interface para listar todas as
     * disciplinas de um usuário.
     * </p>
     */
    @Override
    @Transactional(readOnly = true)
    public List<Disciplina> listaTodasPorUsuario(UUID usuarioId) {
        // 1. Chama o método do JPA Repository (Passo 1)
        List<DisciplinaEntity> entities = jpaRepository.findAllByUsuarioId(usuarioId);

        // 2. Mapeia a lista de Entidades (JPA) para Modelos (Domínio)
        // (Assumindo que você tem um DisciplinaMapper.toDomainList)
        return DisciplinaMapper.toDomainList(entities);
    }
    // --- FIM DO MÉTODO QUE FALTAVA ---
}