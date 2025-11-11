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

@DisplayName("Testes Unitários - ResumoMapper")
class ResumoMapperTest {

    private Disciplina disciplinaDominio;
    private Resumo resumoDominio;

    private DisciplinaEntity disciplinaEntity;
    private ResumoEntity resumoEntity;

    @BeforeEach
    void setUp() {
        // --- Setup dos IDs ---
        UUID disciplinaId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        UUID resumoId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();

        // --- Setup dos Objetos ---

        // (1) Criar o Objeto de Domínio (Modelo) - OK
        disciplinaDominio = new Disciplina(disciplinaId, "Direito Constitucional", "Artigos 1 ao 5 da CF", usuarioId);

        // (2) CORREÇÃO: Criar a Entidade JPA (DisciplinaEntity) - Usar Setters
        disciplinaEntity = new DisciplinaEntity();
        disciplinaEntity.setId(disciplinaId);
        disciplinaEntity.setNome("Direito Constitucional");
        disciplinaEntity.setDescricao("Artigos 1 ao 5 da CF");
        disciplinaEntity.setUsuarioId(usuarioId);
        // --- FIM DA CORREÇÃO ---

        // (3) Criar o Objeto de Domínio (Resumo) - OK
        resumoDominio = Resumo.criar(
                resumoId,
                usuarioId,
                "Remédios Constitucionais",
                "Habeas Corpus, Mandado de Segurança, etc.",
                disciplinaDominio
        );

        // (4) CORREÇÃO: Criar a Entidade JPA (ResumoEntity) - Usar Setters
        resumoEntity = new ResumoEntity();
        resumoEntity.setId(resumoId);
        resumoEntity.setUsuarioId(usuarioId);
        resumoEntity.setTitulo("Remédios Constitucionais");
        resumoEntity.setConteudo("Habeas Corpus, Mandado de Segurança, etc.");
        resumoEntity.setMaterialId(materialId);
        resumoEntity.setDisciplina(disciplinaEntity);
        // --- FIM DA CORREÇÃO ---
    }


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
            assertEquals(resumoDominio.getDisciplina().getUsuarioId(), entityConvertida.getDisciplina().getUsuarioId());
        }

        @Test
        @DisplayName("Deve retornar null quando o Resumo de domínio for nulo")
        void deveRetornarNullQuandoDominioForNull() {
            assertNull(ResumoMapper.toEntity(null));
        }
    }

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
            assertEquals(resumoEntity.getDisciplina().getUsuarioId(), domainConvertido.getDisciplina().getUsuarioId());
        }

        @Test
        @DisplayName("Deve retornar null quando a ResumoEntity for nula")
        void deveRetornarNullQuandoEntityForNull() {
            assertNull(ResumoMapper.toDomain(null));
        }
    }

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