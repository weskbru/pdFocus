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

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository, UsuarioMapper usuarioMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Transactional
    public Usuario salvar(Usuario usuario) {
        // 1. Converte o domínio para entidade (aqui o Mapper DEVE estar levando o resumosHoje)
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);

        // 2. Salva no banco (Insert ou Update)
        UsuarioEntity savedEntity = usuarioJpaRepository.save(usuarioEntity);

        // 3. Retorna o domínio atualizado
        return usuarioMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioJpaRepository.findByEmail(email)
                .map(usuarioMapper::toDomain);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(UUID id) {
           return usuarioJpaRepository.findById(id).map(usuarioMapper::toDomain);
        }
}