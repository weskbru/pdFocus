package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Usuario;
import com.pdfocus.infra.persistence.entity.UsuarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link UsuarioMapper}.
 * Verifica a correção das conversões entre {@link Usuario} (domínio)
 * e {@link UsuarioEntity} (JPA).
 */
@DisplayName("Testes Unitários - UsuarioMapper")
public class UsuarioMapperTest {

    private final UUID id = UUID.randomUUID();
    private final String nome = "João da Silva";
    private final String email = "joao.silva@email.com";
    private final String senhaHash = "$2a$10$abcdefghijklmnopqrstuv"; // Exemplo de hash

    /**
     * Testes para o método {@link UsuarioMapper#toEntity(Usuario)}.
     */
    @Nested
    @DisplayName("Testes para toEntity (Domínio -> JPA)")
    class ToEntityTest {

        @Test
        @DisplayName("Deve converter Usuario de domínio para UsuarioEntity corretamente")
        void deveConverterDominioParaEntity() {
            // Arrange
            Usuario usuarioDominio = new Usuario(id, nome, email, senhaHash, usuarioEntity.getResumosHoje(), usuarioEntity.getDataUltimoUso());

            // Act
            UsuarioEntity usuarioEntity = UsuarioMapper.toEntity(usuarioDominio);

            // Assert
            assertNotNull(usuarioEntity);
            assertEquals(id, usuarioEntity.getId());
            assertEquals(nome, usuarioEntity.getNome());
            assertEquals(email, usuarioEntity.getEmail());
            assertEquals(senhaHash, usuarioEntity.getSenhaHash());
        }

        @Test
        @DisplayName("Deve retornar null quando o Usuario de domínio for nulo")
        void deveRetornarNullQuandoDominioForNull() {
            // Act & Assert
            assertNull(UsuarioMapper.toEntity(null));
        }
    }

    /**
     * Testes para o metodo {@link UsuarioMapper#toDomain(UsuarioEntity)}.
     */
    @Nested
    @DisplayName("Testes para toDomain (JPA -> Domínio)")
    class ToDomainTest {

        @Test
        @DisplayName("Deve converter UsuarioEntity para Usuario de domínio corretamente")
        void deveConverterEntityParaDominio() {
            // Arrange
            UsuarioEntity usuarioEntity = new UsuarioEntity(id, nome, email, senhaHash);

            // Act
            Usuario usuarioDominio = UsuarioMapper.toDomain(usuarioEntity);

            // Assert
            assertNotNull(usuarioDominio);
            assertEquals(id, usuarioDominio.getId());
            assertEquals(nome, usuarioDominio.getNome());
            assertEquals(email, usuarioDominio.getEmail());
            assertEquals(senhaHash, usuarioDominio.getSenhaHash());
        }

        @Test
        @DisplayName("Deve retornar null quando a UsuarioEntity for nula")
        void deveRetornarNullQuandoEntityForNull() {
            // Act & Assert
            assertNull(UsuarioMapper.toDomain(null));
        }
    }

    /**
     * Testes para o método de mapeamento de listas.
     */
    @Nested
    @DisplayName("Testes para toDomainList (Lista JPA -> Lista Domínio)")
    class ToDomainListTest {

        @Test
        @DisplayName("Deve converter lista de UsuarioEntity para lista de Usuario de domínio")
        void deveConverterListaEntityParaListaDominio() {
            // Arrange
            UsuarioEntity entity1 = new UsuarioEntity(id, nome, email, senhaHash);
            List<UsuarioEntity> entities = Collections.singletonList(entity1);

            // Act
            List<Usuario> domainObjects = UsuarioMapper.toDomainList(entities);

            // Assert
            assertNotNull(domainObjects);
            assertEquals(1, domainObjects.size());
            assertEquals(id, domainObjects.get(0).getId());
            assertEquals(nome, domainObjects.get(0).getNome());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando a lista de entrada for nula ou vazia")
        void deveRetornarListaVaziaParaListaNulaOuVazia() {
            // Act & Assert
            assertTrue(UsuarioMapper.toDomainList(null).isEmpty());
            assertTrue(UsuarioMapper.toDomainList(Collections.emptyList()).isEmpty());
        }
    }

}
