package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.usuario.port.saida.ConfirmationTokenRepository;
import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.ConfirmationTokenEntity;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import com.pdfocus.infra.persistence.mapper.UsuarioMapper;
import com.pdfocus.infra.persistence.repository.ConfirmationTokenJpaRepository;
import com.pdfocus.infra.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ConfirmationTokenRepositoryAdapter implements ConfirmationTokenRepository {

    private final ConfirmationTokenJpaRepository tokenJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioMapper usuarioMapper;

    public ConfirmationTokenRepositoryAdapter(
            ConfirmationTokenJpaRepository tokenJpaRepository,
            UsuarioJpaRepository usuarioJpaRepository,
            UsuarioMapper usuarioMapper) {
        this.tokenJpaRepository = tokenJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Transactional
    public void salvarToken(Usuario usuario, String token, LocalDateTime expiraEm) {
        // 1. Buscamos a entidade do usuário no banco para garantir que ela existe e está gerenciada
        UsuarioEntity usuarioEntity = usuarioJpaRepository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ao criar token"));

        // 2. Criamos a entidade do Token
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setCreatedAt(LocalDateTime.now());
        tokenEntity.setExpiresAt(expiraEm);
        tokenEntity.setUsuario(usuarioEntity);

        // 3. Salvamos
        tokenJpaRepository.save(tokenEntity);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorToken(String token) {
        return tokenJpaRepository.findByToken(token)
                .map(tokenEntity -> usuarioMapper.toDomain(tokenEntity.getUsuario()));
    }

    @Override
    public boolean isTokenValido(String token) {
        System.out.println("🔍 [DEBUG] Validando token recebido: [" + token + "]");

        Optional<ConfirmationTokenEntity> optToken = tokenJpaRepository.findByToken(token);

        if (optToken.isEmpty()) {
            System.out.println("❌ [DEBUG] Token NÃO encontrado no banco de dados.");
            return false;
        }

        ConfirmationTokenEntity t = optToken.get();
        LocalDateTime agora = LocalDateTime.now();

        boolean naoExpirou = agora.isBefore(t.getExpiresAt());
        boolean naoFoiUsado = t.getConfirmedAt() == null;

        System.out.println("📊 [DEBUG] Detalhes do Token:");
        System.out.println("   - Criado em:   " + t.getCreatedAt());
        System.out.println("   - Expira em:   " + t.getExpiresAt());
        System.out.println("   - Hora Agora:  " + agora);
        System.out.println("   - Usado em:    " + t.getConfirmedAt());
        System.out.println("   -> Não Expirou? " + naoExpirou);
        System.out.println("   -> Não Usado?   " + naoFoiUsado);

        return naoExpirou && naoFoiUsado;
    }

    @Override
    @Transactional
    public void confirmarToken(String token) {
        tokenJpaRepository.findByToken(token).ifPresent(tokenEntity -> {
            tokenEntity.setConfirmedAt(LocalDateTime.now());
            tokenJpaRepository.save(tokenEntity);
        });
    }
}