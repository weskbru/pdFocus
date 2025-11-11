package com.pdfocus.application.material.port.saida;

import org.springframework.core.io.Resource;
import java.io.InputStream;

/**
 * Porta de Saída (Output Port) responsável pelo armazenamento físico dos ficheiros de {@code Material}.
 *
 * <p>
 * Esta interface define o contrato entre a camada de Aplicação e o mecanismo de armazenamento subjacente
 * (ex: sistema de arquivos local, Amazon S3, Google Cloud Storage, etc.).
 * </p>
 *
 * <p>
 * O objetivo é manter o domínio e os casos de uso independentes da tecnologia de armazenamento,
 * promovendo testabilidade e facilidade de substituição do backend físico.
 * </p>
 *
 * <p><b>Contexto Arquitetural:</b> Parte da fronteira <i>Application → Infrastructure</i> no modelo Hexagonal.</p>
 */
public interface MaterialStoragePort {

    /**
     * Persiste um ficheiro binário no sistema de armazenamento.
     *
     * @param nomeFicheiro O identificador único sob o qual o ficheiro será armazenado.
     * @param inputStream O fluxo de bytes representando o conteúdo do ficheiro.
     * @throws RuntimeException se ocorrer um erro de I/O durante o processo de gravação.
     */
    void guardar(String nomeFicheiro, InputStream inputStream);

    /**
     * Remove um ficheiro previamente armazenado.
     *
     * @param nomeFicheiro O identificador único do ficheiro a ser removido.
     * @throws RuntimeException se o ficheiro não existir ou não puder ser apagado.
     */
    void apagar(String nomeFicheiro);

    /**
     * Recupera um ficheiro do sistema de armazenamento como um {@link Resource}.
     *
     * @param nomeFicheiro O identificador único do ficheiro a ser carregado.
     * @return Um {@link Resource} representando o ficheiro recuperado.
     * @throws RuntimeException se o ficheiro não for encontrado ou estiver inacessível.
     */
    Resource carregar(String nomeFicheiro);
}
