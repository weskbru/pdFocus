package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import com.pdfocus.infra.persistence.mapper.UsuarioMapper;
import com.pdfocus.infra.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa a porta de saída {@link UsuarioRepository}
 * utilizando Spring Data JPA para a persistência de dados de Usuario.
 */
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Usuario salvar(Usuario usuario) {
        // 1. Converte o domínio para entidade (aqui o Mapper DEVE estar levando o resumosHoje)
        UsuarioEntity usuarioEntity = UsuarioMapper.toEntity(usuario);

        // 2. Salva no banco (Insert ou Update)
        UsuarioEntity savedEntity = jpaRepository.save(usuarioEntity);

        // 3. Retorna o domínio atualizado
        return UsuarioMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        Optional<UsuarioEntity> optionalEntity = jpaRepository.findByEmail(email);
        return optionalEntity.map(UsuarioMapper::toDomain);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(UUID id) {
        Optional<UsuarioEntity> optionalEntity = jpaRepository.findById(id);
        return optionalEntity.map(UsuarioMapper::toDomain);
    }
}