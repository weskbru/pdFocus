package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Material;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.MaterialEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link MaterialMapper}.
 * Verifica a correção das conversões entre {@link Material} (domínio)
 * e {@link MaterialEntity} (JPA).
 */
@DisplayName("Testes Unitários - MaterialMapper")
public class MaterialMapperTest {

    private Material materialDominio;
    private DisciplinaEntity disciplinaEntity;
    private MaterialEntity materialEntity;

    /**
     * Prepara os dados de teste comuns antes da execução de cada teste.
     */
    @BeforeEach
    void setUp() {
        UUID disciplinaId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();

        // --- Setup dos Objetos ---
        // A entidade JPA da disciplina que simulamos ter sido buscada do banco.
        disciplinaEntity = new DisciplinaEntity(disciplinaId, "Engenharia de Software", "Conceitos e práticas", usuarioId);

        // O objeto de domínio Material, que contém apenas o ID da disciplina.
        materialDominio = Material.criar(
                materialId,
                "diagrama_uml.png",
                "a1b2c3d4-e5f6.png",
                "image/png",
                1024L,
                usuarioId,
                disciplinaId
        );

        // A entidade JPA Material, que contém o objeto DisciplinaEntity completo.
        materialEntity = new MaterialEntity(
                materialId,
                "diagrama_uml.png",
                "a1b2c3d4-e5f6.png",
                "image/png",
                1024L,
                usuarioId,
                disciplinaEntity
        );
    }

    /**
     * Testes para o método {@link MaterialMapper#toEntity(Material, DisciplinaEntity)}.
     */
    @Nested
    @DisplayName("Testes para toEntity (Domínio -> JPA)")
    class ToEntityTest {

        @Test
        @DisplayName("Deve converter Material de domínio para MaterialEntity corretamente")
        void deveConverterDominioParaEntity() {
            // Act
            MaterialEntity entityConvertida = MaterialMapper.toEntity(materialDominio, disciplinaEntity);

            // Assert
            assertNotNull(entityConvertida);
            assertEquals(materialDominio.getId(), entityConvertida.getId());
            assertEquals(materialDominio.getNomeOriginal(), entityConvertida.getNomeOriginal());
            assertEquals(materialDominio.getNomeStorage(), entityConvertida.getNomeStorage());
            assertEquals(materialDominio.getTipoArquivo(), entityConvertida.getTipoArquivo());
            assertEquals(materialDominio.getTamanho(), entityConvertida.getTamanho());
            assertEquals(materialDominio.getUsuarioId(), entityConvertida.getUsuarioId());
            assertNotNull(entityConvertida.getDisciplina());
            assertEquals(disciplinaEntity.getId(), entityConvertida.getDisciplina().getId());
        }

        @Test
        @DisplayName("Deve retornar null quando o Material de domínio for nulo")
        void deveRetornarNullQuandoDominioForNull() {
            assertNull(MaterialMapper.toEntity(null, disciplinaEntity));
        }
    }

    /**
     * Testes para o método {@link MaterialMapper#toDomain(MaterialEntity)}.
     */
    @Nested
    @DisplayName("Testes para toDomain (JPA -> Domínio)")
    class ToDomainTest {

        @Test
        @DisplayName("Deve converter MaterialEntity para Material de domínio corretamente")
        void deveConverterEntityParaDominio() {
            // Act
            Material domainConvertido = MaterialMapper.toDomain(materialEntity);

            // Assert
            assertNotNull(domainConvertido);
            assertEquals(materialEntity.getId(), domainConvertido.getId());
            assertEquals(materialEntity.getNomeOriginal(), domainConvertido.getNomeOriginal());
            assertEquals(materialEntity.getNomeStorage(), domainConvertido.getNomeStorage());
            assertEquals(materialEntity.getTipoArquivo(), domainConvertido.getTipoArquivo());
            assertEquals(materialEntity.getTamanho(), domainConvertido.getTamanho());
            assertEquals(materialEntity.getUsuarioId(), domainConvertido.getUsuarioId());
            // Verifica se o ID da disciplina foi extraído corretamente
            assertEquals(materialEntity.getDisciplina().getId(), domainConvertido.getDisciplinaId());
        }

        @Test
        @DisplayName("Deve retornar null quando a MaterialEntity for nula")
        void deveRetornarNullQuandoEntityForNull() {
            assertNull(MaterialMapper.toDomain(null));
        }
    }

    /**
     * Testes para o método de mapeamento de listas.
     */
    @Nested
    @DisplayName("Testes para toDomainList (Lista JPA -> Lista Domínio)")
    class ToDomainListTest {

        @Test
        @DisplayName("Deve converter lista de MaterialEntity para lista de Material de domínio")
        void deveConverterListaEntityParaListaDominio() {
            // Arrange
            List<MaterialEntity> entities = Collections.singletonList(materialEntity);

            // Act
            List<Material> domainObjects = MaterialMapper.toDomainList(entities);

            // Assert
            assertNotNull(domainObjects);
            assertEquals(1, domainObjects.size());
            assertEquals(materialEntity.getId(), domainObjects.get(0).getId());
        }
    }
}
