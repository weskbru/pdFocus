package com.pdfocus.core.models;

import com.pdfocus.core.shared.Validador;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Representa um material de estudo enviado pelo usuário, como um arquivo PDF.
 * <p>
 * Armazena apenas os metadados essenciais: nome, tipo, tamanho e data de upload.
 * O conteúdo físico do arquivo é gerenciado pela camada de armazenamento (infra).
 * </p>
 *
 * <p><b>[Refatoração N+1]</b>
 * Esta classe foi atualizada para opcionalmente carregar a entidade {@link Disciplina}
 * associada, permitindo que o {@code JOIN FETCH} da camada de persistência
 * seja mapeado diretamente para o domínio.
 * </p>
 *
 * @see com.pdfocus.infra.persistence.entity.MaterialEntity
 * @see Disciplina
 */
public class Material {

    /** Identificador único do material. */
    private final UUID id;

    /** Nome original do arquivo enviado. */
    private final String nomeOriginal;

    /** Nome único usado no sistema de armazenamento. */
    private final String nomeStorage;

    /** Tipo MIME do arquivo (ex: application/pdf). */
    private final String tipoArquivo;

    /** Tamanho do arquivo em bytes. */
    private final long tamanho;

    /** Identificador do usuário que fez o upload. */
    private final UUID usuarioId;

    /** Identificador da disciplina associada ao material. */
    private final UUID disciplinaId;

    /** Data e hora do upload. */
    private final OffsetDateTime dataUpload;

    // --- NOVA ADIÇÃO (ETAPA A) ---
    /**
     * Objeto da disciplina associada.
     * <p>
     * Este campo é {@code final} mas pode ser {@code null}.
     * Ele só é preenchido em consultas otimizadas (com JOIN FETCH),
     * como as usadas no dashboard. Em outros contextos (como uma simples criação),
     * ele permanece nulo.
     * </p>
     */
    private final Disciplina disciplina;
    // --- FIM DA ADIÇÃO ---

    /**
     * Construtor privado. A instanciação é controlada por métodos de fábrica.
     *
     * @param disciplina A disciplina associada (pode ser nula se não for carregada).
     */
    private Material(UUID id, String nomeOriginal, String nomeStorage, String tipoArquivo,
                     long tamanho, UUID usuarioId, UUID disciplinaId, OffsetDateTime dataUpload,
                     Disciplina disciplina) { // <-- Parâmetro adicionado
        this.id = id;
        this.nomeOriginal = nomeOriginal;
        this.nomeStorage = nomeStorage;
        this.tipoArquivo = tipoArquivo;
        this.tamanho = tamanho;
        this.usuarioId = usuarioId;
        this.disciplinaId = disciplinaId;
        this.dataUpload = dataUpload;
        this.disciplina = disciplina; // <-- Atribuição adicionada
    }

    /**
     * Cria um novo material garantindo um estado consistente (para novos uploads).
     *
     * @param id           Identificador único do material.
     * @param nomeOriginal Nome original do arquivo.
     * @param nomeStorage  Nome do arquivo no armazenamento interno.
     * @param tipoArquivo  Tipo MIME do arquivo.
     * @param tamanho      Tamanho em bytes.
     * @param usuarioId    Identificador do usuário dono do material.
     * @param disciplinaId Identificador da disciplina associada.
     * @param dataUpload   Data e hora do upload.
     * @return Uma nova instância válida de {@code Material}.
     * @throws IllegalArgumentException Se algum campo obrigatório estiver ausente ou inválido.
     */
    public static Material criar(UUID id, String nomeOriginal, String nomeStorage, String tipoArquivo,
                                 long tamanho, UUID usuarioId, UUID disciplinaId, OffsetDateTime dataUpload) {

        Validador.requireNotNull(id, "ID do material não pode ser nulo");
        Validador.requireNotEmpty(nomeOriginal, "Nome original do arquivo é obrigatório");
        Validador.requireNotEmpty(nomeStorage, "Nome de armazenamento do arquivo é obrigatório");
        Validador.requireNotEmpty(tipoArquivo, "Tipo do arquivo é obrigatório");
        Validador.requireNotNull(usuarioId, "ID do usuário não pode ser nulo");
        Validador.requireNotNull(disciplinaId, "ID da disciplina não pode ser nulo");
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho do arquivo deve ser maior que zero.");
        }

        // Chama o construtor privado, passando 'null' para a disciplina associada,
        // pois ela não é carregada neste contexto de "criação".
        return new Material(id, nomeOriginal, nomeStorage, tipoArquivo, tamanho, usuarioId, disciplinaId, dataUpload, null); // <-- 'null' adicionado
    }

    // --- NOVO MÉTODO DE FÁBRICA (ETAPA A) ---
    /**
     * Reconstrói um Material a partir dos dados do banco de dados,
     * <b>incluindo</b> a entidade Disciplina pré-carregada (JOIN FETCH).
     * <p>
     * Este método é usado exclusivamente pela camada de Mapeamento (Mapper) para
     * re-hidratar o objeto de domínio sem executar as validações de 'criar'.
     * </p>
     *
     * @param disciplina A entidade Disciplina completa (pré-carregada).
     * @return Uma instância de Material com a disciplina aninhada.
     */
    public static Material reconstituirComDisciplina(
            UUID id, String nomeOriginal, String nomeStorage, String tipoArquivo,
            long tamanho, UUID usuarioId, OffsetDateTime dataUpload,
            Disciplina disciplina) {

        // Validação mínima para garantir a integridade da associação
        Validador.requireNotNull(disciplina, "Disciplina não pode ser nula na reconstituição");
        Validador.requireNotNull(disciplina.getId(), "ID da disciplina não pode ser nulo");

        // Chama o construtor privado, passando a disciplina completa
        return new Material(id, nomeOriginal, nomeStorage, tipoArquivo, tamanho, usuarioId,
                disciplina.getId(), // Garante que o disciplinaId esteja sincronizado
                dataUpload,
                disciplina); // Anexa o objeto completo
    }
    // --- FIM DO NOVO MÉTODO ---


    // --- Getters ---

    public UUID getId() { return id; }
    public String getNomeOriginal() { return nomeOriginal; }
    public String getNomeStorage() { return nomeStorage; }
    public String getTipoArquivo() { return tipoArquivo; }
    public long getTamanho() { return tamanho; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getDisciplinaId() { return disciplinaId; }
    public OffsetDateTime getDataUpload() { return dataUpload; }

    /**
     * Retorna o objeto Disciplina associado, se tiver sido carregado.
     *
     * @return A {@link Disciplina}, ou {@code null} se não tiver sido
     * carregada na consulta (ex: em um contexto não-otimizado).
     */
    public Disciplina getDisciplina() { return disciplina; }

}