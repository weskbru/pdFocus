package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link DisciplinaMapper}.
 * Verifica a correção das conversões entre {@link Disciplina} (domínio)
 * e {@link DisciplinaEntity} (JPA).
 */
@DisplayName("Testes Unitários - DisciplinaMapper")
public class DisciplinaMapperTest {

    private final UUID id = UUID.randomUUID();
    private final UUID usuarioId = UUID.randomUUID();
    private final String nome = "Matemática Avançada";
    private final String descricao = "Cálculo, Álgebra Linear e Equações Diferenciais";

    /**
     * Testes para o método {@link DisciplinaMapper#toEntity(Disciplina)}.
     * (Este teste estava correto)
     */
    @Nested
    @DisplayName("Testes para toEntity (Domínio -> JPA)")
    class ToEntityTest {

        @Test
        @DisplayName("Deve converter Disciplina de domínio para DisciplinaEntity corretamente")
        void deveConverterDominioParaEntityCorretamente() {
            // Arrange
            // A criação da Disciplina de domínio (modelo) está correta.
            Disciplina disciplinaDominio = new Disciplina(id, nome, descricao, usuarioId);

            // Act
            DisciplinaEntity disciplinaEntity = DisciplinaMapper.toEntity(disciplinaDominio);

            // Assert
            assertNotNull(disciplinaEntity);
            assertEquals(id, disciplinaEntity.getId());
            assertEquals(nome, disciplinaEntity.getNome());
            assertEquals(descricao, disciplinaEntity.getDescricao());
            assertEquals(usuarioId, disciplinaEntity.getUsuarioId());
        }

        @Test
        @DisplayName("Deve retornar null quando a Disciplina de domínio for nula")
        void deveRetornarNullEntityQuandoDominioForNull() {
            // Act & Assert
            assertNull(DisciplinaMapper.toEntity(null));
        }
    }

    /**
     * Testes para o método {@link DisciplinaMapper#toDomain(DisciplinaEntity)}.
     */
    @Nested
    @DisplayName("Testes para toDomain (JPA -> Domínio)")
    class ToDomainTest {

        @Test
        @DisplayName("Deve converter DisciplinaEntity para Disciplina de domínio corretamente")
        void deveConverterEntityParaDominioCorretamente() {
            // Arrange
            // --- CORREÇÃO (Padrão Setters) ---
            // 1. Cria a entidade JPA com o construtor vazio (obrigatório pelo JPA)
            DisciplinaEntity disciplinaEntity = new DisciplinaEntity();
            // 2. Usa setters para construir o objeto de teste
            disciplinaEntity.setId(id);
            disciplinaEntity.setNome(nome);
            disciplinaEntity.setDescricao(descricao);
            disciplinaEntity.setUsuarioId(usuarioId);
            // --- FIM DA CORREÇÃO ---

            // Act
            Disciplina disciplinaDominio = DisciplinaMapper.toDomain(disciplinaEntity);

            // Assert
            assertNotNull(disciplinaDominio);
            assertEquals(id, disciplinaDominio.getId());
            assertEquals(nome, disciplinaDominio.getNome());
            assertEquals(descricao, disciplinaDominio.getDescricao());
            assertEquals(usuarioId, disciplinaDominio.getUsuarioId());
        }

        @Test
        @DisplayName("Deve retornar null quando a DisciplinaEntity for nula")
        void deveRetornarNullDominioQuandoEntityForNull() {
            // Act & Assert
            assertNull(DisciplinaMapper.toDomain(null));
        }
    }

    /**
     * Testes para o método {@link DisciplinaMapper#toDomainList(List)}.
     */
    @Nested
    @DisplayName("Testes para toDomainList (Lista JPA -> Lista Domínio)")
    class ToDomainListTest {

        @Test
        @DisplayName("Deve converter lista de DisciplinaEntity para lista de Disciplina de domínio")
        void deveConverterListaEntityParaListaDominio() {
            // Arrange
            // --- CORREÇÃO (Padrão Setters) ---
            DisciplinaEntity entity1 = new DisciplinaEntity();
            entity1.setId(UUID.randomUUID());
            entity1.setNome("Nome1");
            entity1.setDescricao("Desc1");
            entity1.setUsuarioId(usuarioId);

            DisciplinaEntity entity2 = new DisciplinaEntity();
            entity2.setId(UUID.randomUUID());
            entity2.setNome("Nome2");
            entity2.setDescricao("Desc2");
            entity2.setUsuarioId(usuarioId);
            // --- FIM DA CORREÇÃO ---

            List<DisciplinaEntity> entities = Arrays.asList(entity1, entity2);

            // Act
            List<Disciplina> domainObjects = DisciplinaMapper.toDomainList(entities);

            // Assert
            assertNotNull(domainObjects);
            assertEquals(2, domainObjects.size());
            assertEquals(entity1.getId(), domainObjects.get(0).getId());
            assertEquals(entity1.getUsuarioId(), domainObjects.get(0).getUsuarioId());
            assertEquals(entity2.getId(), domainObjects.get(1).getId());
            assertEquals(entity2.getUsuarioId(), domainObjects.get(1).getUsuarioId());
        }
    }
}