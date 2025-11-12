package com.pdfocus.application.resumo.service;

import com.pdfocus.application.resumo.port.entrada.ObterResumoPorIdUseCase;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.core.models.Resumo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso {@link ObterResumoPorIdUseCase}.
 * <p>
 * Este serviço é responsável por recuperar um resumo específico pertencente a um usuário,
 * garantindo o isolamento e a segurança de acesso no nível do domínio.
 * </p>
 *
 * <p>
 * O serviço atua como uma simples orquestração: valida os parâmetros de entrada
 * e delega a busca à porta de saída {@link ResumoRepository}, que contém
 * a lógica de consulta combinando o ID do resumo e o ID do usuário.
 * </p>
 */
@Service
public class DefaultObterResumoPorIdService implements ObterResumoPorIdUseCase {

    private final ResumoRepository resumoRepository;

    /**
     * Cria uma nova instância do serviço para busca de resumos.
     *
     * @param resumoRepository A porta de saída responsável pela persistência e consulta de resumos.
     */
    public DefaultObterResumoPorIdService(ResumoRepository resumoRepository) {
        this.resumoRepository = Objects.requireNonNull(resumoRepository, "ResumoRepository não pode ser nulo.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * O método é executado em modo somente leitura para garantir otimização de transações.
     * </p>
     *
     * @param id O identificador único do resumo.
     * @param usuarioId O identificador do usuário proprietário do resumo.
     * @return Um {@link Optional} contendo o {@link Resumo} se encontrado e pertencente ao usuário;
     *         caso contrário, retorna um Optional vazio.
     * @throws IllegalArgumentException se {@code id} ou {@code usuarioId} forem nulos.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Resumo> executar(UUID id, UUID usuarioId) {
        Objects.requireNonNull(id, "O ID do resumo não pode ser nulo.");
        Objects.requireNonNull(usuarioId, "O ID do usuário não pode ser nulo.");

        return resumoRepository.buscarPorIdEUsuario(id, usuarioId);
    }
}
