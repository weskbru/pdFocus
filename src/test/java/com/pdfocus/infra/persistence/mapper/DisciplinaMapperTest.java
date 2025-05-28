package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link DisciplinaMapper}.
 * Verifica a correção das conversões entre {@link Disciplina} (domínio)
 * e {@link DisciplinaEntity} (JPA).
 */
@DisplayName("Testes Unitários para DisciplinaMapper")
public class DisciplinaMapperTest {

    private final UUID id = UUID.randomUUID();
    private final String nome = "Matemática Avançada";
    private final String descricao = "Cálculo, Álgebra Linear e Equações Diferenciais";

    /**
     * Testes para o método {@link DisciplinaMapper#toEntity(Disciplina)}.
     */
    @Nested
    @DisplayName("Testes para toEntity (Domínio para Entidade JPA)")
    class ToEntityTest {

        @Test
        @DisplayName("Deve converter Disciplina de domínio para DisciplinaEntity corretamente")
        void deveConverterDominioParaEntityCorretamente() {
            // Arrange
            Disciplina disciplinaDominio = new Disciplina(id, nome, descricao);

            // Act
            DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(disciplinaDominio);

            // Assert
            assertNotNull(disciplinaEntity, "A entidade JPA não deve ser nula.");
            assertEquals(id, disciplinaEntity.getId(), "O ID deve ser o mesmo.");
            assertEquals(nome, disciplinaEntity.getNome(), "O nome deve ser o mesmo.");
            assertEquals(descricao, disciplinaEntity.getDescricao(), "A descrição deve ser a mesma.");

        }

        @Test
        @DisplayName("Deve retornar null quando a Disciplina de domínio for nula")
        void deveRetornarNullEntityQuandoDominioForNull() {
            // Arrange
            Disciplina disciplinaDominio = null;

            // Act
            DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(disciplinaDominio);

            // Assert
            assertNull(disciplinaEntity, "A entidade JPA deve ser nula se o domínio for nulo.");
        }
    }

    /**
     * Testes para o metodo {@link DisciplinaMapper#toDomain(DisciplinaEntity)}.
     */
    @Nested
    @DisplayName("Testes para toDomain (Entidade JPA para Domínio)")
    class ToDomainTest {

        @Test
        @DisplayName("Deve converter DisciplinaEntity para Disciplina de domínio corretamente")
        void deveConverterEntityParaDominioCorretamente() {
            // Arrange
            DisciplinaEntity disciplinaEntity = new DisciplinaEntity(id, nome, descricao);

            // Act
            Disciplina disciplinaDominio = DisciplinaMapper.toDomain(disciplinaEntity);

            // Assert
            assertNotNull(disciplinaDominio, "O objeto de domínio não deve ser nulo.");
            assertEquals(id, disciplinaDominio.getId(), "O ID deve ser o mesmo.");
            assertEquals(nome, disciplinaDominio.getNome(), "O nome deve ser o mesmo.");
            assertEquals(descricao, disciplinaDominio.getDescricao(), "A descrição deve ser a mesma.");
        }


        @Test
        @DisplayName("Deve retornar null quando a DisciplinaEntity for nula")
        void deveRetornarNullDominioQuandoEntityForNull() {
            // Arrange
            DisciplinaEntity disciplinaEntity = null;

            // Act
            Disciplina disciplinaDominio = DisciplinaMapper.toDomain(disciplinaEntity);

            // Assert
            assertNull(disciplinaDominio, "O objeto de domínio deve ser nulo se a entidade JPA for nula.");
        }
    }

    /**
     * Testes para o método {@link DisciplinaMapper#toDomainList(List)}.
     */
    @Nested
    @DisplayName("Testes para toDomainList (Lista de Entidades JPA para Lista de Domínio)")
    class ToDomainListTest {

        @Test
        @DisplayName("Deve converter lista de DisciplinaEntity para lista de Disciplina de domínio")
        void deveConverterListaEntityParaListaDominio() {
            // Arrange
            DisciplinaEntity entity1 = new DisciplinaEntity(UUID.randomUUID(), "Nome1", "Desc1");
            DisciplinaEntity entity2 = new DisciplinaEntity(UUID.randomUUID(), "Nome2", "Desc2");
            List<DisciplinaEntity> entities = Arrays.asList(entity1, entity2);

            // Act
            List<Disciplina> domainObjects = DisciplinaMapper.toDomainList(entities);

            // Assert
            assertNotNull(domainObjects);
            assertEquals(2, domainObjects.size());
            assertEquals(entity1.getId(), domainObjects.get(0).getId());
            assertEquals(entity1.getNome(), domainObjects.get(0).getNome());
            assertEquals(entity2.getId(), domainObjects.get(1).getId());
            assertEquals(entity2.getNome(), domainObjects.get(1).getNome());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando a lista de DisciplinaEntity for vazia")
        void deveRetornarListaVaziaParaListaEntityVazia() {
            // Arrange
            List<DisciplinaEntity> entities = Collections.emptyList();

            // Act
            List<Disciplina> domainObjects = DisciplinaMapper.toDomainList(entities);

            // Assert
            assertNotNull(domainObjects);
            assertTrue(domainObjects.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando a lista de DisciplinaEntity for nula")
        void deveRetornarListaVaziaParaListaEntityNula() {
            // Arrange
            List<DisciplinaEntity> entities = null;

            // Act
            List<Disciplina> domainObjects = DisciplinaMapper.toDomainList(entities);

            // Assert
            assertNotNull(domainObjects);
            assertTrue(domainObjects.isEmpty()); // Conforme implementação atual do mapper
        }

    }

    /**
     * Testes para o método {@link DisciplinaMapper#toEntityList(List)}.
     */
    @Nested
    @DisplayName("Testes para toEntityList (Lista de Domínio para Lista de Entidades JPA)")
    class ToEntityListTest {

        @Test
        @DisplayName("Deve converter lista de Disciplina de domínio para lista de DisciplinaEntity")
        void deveConverterListaDominioParaListaEntity() {
            // Arrange
            Disciplina domain1 = new Disciplina(UUID.randomUUID(), "NomeD1", "DescD1");
            Disciplina domain2 = new Disciplina(UUID.randomUUID(), "NomeD2", "DescD2");
            List<Disciplina> domainObjects = Arrays.asList(domain1, domain2);

            // Act
            List<DisciplinaEntity> entities = DisciplinaMapper.toEntityList(domainObjects);

            // Assert
            assertNotNull(entities);
            assertEquals(2, entities.size());
            assertEquals(domain1.getId(), entities.get(0).getId());
            assertEquals(domain1.getNome(), entities.get(0).getNome());
            assertEquals(domain2.getId(), entities.get(1).getId());
            assertEquals(domain2.getNome(), entities.get(1).getNome());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando a lista de Disciplina de domínio for vazia")
        void deveRetornarListaVaziaParaListaDominioVazia() {
            // Arrange
            List<Disciplina> domainObjects = Collections.emptyList();

            // Act
            List<DisciplinaEntity> entities = DisciplinaMapper.toEntityList(domainObjects);

            // Assert
            assertNotNull(entities);
            assertTrue(entities.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando a lista de Disciplina de domínio for nula")
        void deveRetornarListaVaziaParaListaDominioNula() {
            // Arrange
            List<Disciplina> domainObjects = null;

            // Act
            List<DisciplinaEntity> entities = DisciplinaMapper.toEntityList(domainObjects);

            // Assert
            assertNotNull(entities);
            assertTrue(entities.isEmpty()); // Conforme implementação atual do mapper
        }

    }
}



