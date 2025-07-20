package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import com.pdfocus.infra.persistence.mapper.UsuarioMapper;
import com.pdfocus.infra.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        UsuarioEntity usuarioEntity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity savedEntity = jpaRepository.save(usuarioEntity);
        return UsuarioMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        Optional<UsuarioEntity> optionalEntity = jpaRepository.findByEmail(email);
        return optionalEntity.map(UsuarioMapper::toDomain);
    }
}