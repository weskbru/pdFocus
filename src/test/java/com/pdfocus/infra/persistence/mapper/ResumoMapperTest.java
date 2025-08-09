package com.pdfocus.infra.persistence.mapper;

import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.infra.persistence.entity.DisciplinaEntity;
import com.pdfocus.infra.persistence.entity.ResumoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link ResumoMapper}.
 * Verifica a correção das conversões entre {@link Resumo} (domínio)
 * e {@link ResumoEntity} (JPA), incluindo o mapeamento da disciplina associada.
 */
@DisplayName("Testes Unitários - ResumoMapper")
class ResumoMapperTest {

    private Disciplina disciplinaDominio;
    private Resumo resumoDominio;

    private DisciplinaEntity disciplinaEntity;
    private ResumoEntity resumoEntity;

    /**
     * Prepara os dados de teste comuns antes da execução de cada teste.
     */
    @BeforeEach
    void setUp() {
        // --- Setup dos Objetos ---
        UUID disciplinaId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        UUID resumoId = UUID.randomUUID();

        // CORREÇÃO: A criação da Disciplina e da DisciplinaEntity agora inclui o usuarioId.
        disciplinaDominio = new Disciplina(disciplinaId, "Direito Constitucional", "Artigos 1 ao 5 da CF", usuarioId);
        disciplinaEntity = new DisciplinaEntity(disciplinaId, "Direito Constitucional", "Artigos 1 ao 5 da CF", usuarioId);

        resumoDominio = Resumo.criar(
                resumoId,
                usuarioId,
                "Remédios Constitucionais",
                "Habeas Corpus, Mandado de Segurança, etc.",
                disciplinaDominio
        );

        resumoEntity = new ResumoEntity(
                resumoId,
                usuarioId,
                "Remédios Constitucionais",
                "Habeas Corpus, Mandado de Segurança, etc.",
                disciplinaEntity
        );
    }

    /**
     * Testes para o método {@link ResumoMapper#toEntity(Resumo)}.
     */
    @Nested
    @DisplayName("Testes para toEntity (Domínio -> JPA)")
    class ToEntityTest {

        @Test
        @DisplayName("Deve converter Resumo de domínio para ResumoEntity corretamente")
        void deveConverterDominioParaEntityCorretamente() {
            // Act
            ResumoEntity entityConvertida = ResumoMapper.toEntity(resumoDominio);

            // Assert
            assertNotNull(entityConvertida);
            assertEquals(resumoDominio.getId(), entityConvertida.getId());
            assertEquals(resumoDominio.getUsuarioId(), entityConvertida.getUsuarioId());
            assertEquals(resumoDominio.getTitulo(), entityConvertida.getTitulo());
            assertNotNull(entityConvertida.getDisciplina());
            assertEquals(resumoDominio.getDisciplina().getId(), entityConvertida.getDisciplina().getId());
            // Nova verificação para garantir que o usuarioId da disciplina também foi mapeado
            assertEquals(resumoDominio.getDisciplina().getUsuarioId(), entityConvertida.getDisciplina().getUsuarioId());
        }

        @Test
        @DisplayName("Deve retornar null quando o Resumo de domínio for nulo")
        void deveRetornarNullQuandoDominioForNull() {
            assertNull(ResumoMapper.toEntity(null));
        }
    }

    /**
     * Testes para o método {@link ResumoMapper#toDomain(ResumoEntity)}.
     */
    @Nested
    @DisplayName("Testes para toDomain (JPA -> Domínio)")
    class ToDomainTest {

        @Test
        @DisplayName("Deve converter ResumoEntity para Resumo de domínio corretamente")
        void deveConverterEntityParaDominioCorretamente() {
            // Act
            Resumo domainConvertido = ResumoMapper.toDomain(resumoEntity);

            // Assert
            assertNotNull(domainConvertido);
            assertEquals(resumoEntity.getId(), domainConvertido.getId());
            assertEquals(resumoEntity.getUsuarioId(), domainConvertido.getUsuarioId());
            assertEquals(resumoEntity.getTitulo(), domainConvertido.getTitulo());
            assertNotNull(domainConvertido.getDisciplina());
            assertEquals(resumoEntity.getDisciplina().getId(), domainConvertido.getDisciplina().getId());
            // Nova verificação
            assertEquals(resumoEntity.getDisciplina().getUsuarioId(), domainConvertido.getDisciplina().getUsuarioId());
        }

        @Test
        @DisplayName("Deve retornar null quando a ResumoEntity for nula")
        void deveRetornarNullQuandoEntityForNull() {
            assertNull(ResumoMapper.toDomain(null));
        }
    }

    /**
     * Testes para os métodos de mapeamento de listas.
     */
    @Nested
    @DisplayName("Testes para mapeamento de listas")
    class ListMappingTest {

        @Test
        @DisplayName("Deve converter lista de ResumoEntity para lista de Resumo de domínio")
        void deveConverterListaEntityParaListaDominio() {
            // Arrange
            List<ResumoEntity> entities = Collections.singletonList(resumoEntity);
            // Act
            List<Resumo> domainObjects = ResumoMapper.toDomainList(entities);
            // Assert
            assertNotNull(domainObjects);
            assertEquals(1, domainObjects.size());
            assertEquals(resumoEntity.getId(), domainObjects.get(0).getId());
        }
    }
}
