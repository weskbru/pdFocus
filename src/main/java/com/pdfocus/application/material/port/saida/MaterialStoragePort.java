package com.pdfocus.application.material.port.saida;

import java.io.InputStream;

/**
 * Porta de Saída (Interface de Storage) para operações de armazenamento físico de ficheiros.
 * <p>
 * Define o contrato que a camada de aplicação usa para interagir com a
 * camada de infraestrutura para guardar, recuperar ou apagar ficheiros fisicamente.
 * </p>
 */
public interface MaterialStoragePort {

    /**
     * Guarda o conteúdo de um ficheiro num sistema de armazenamento.
     *
     * @param nomeFicheiroStorage O nome único gerado para o ficheiro no armazenamento (ex: um UUID com a extensão).
     * @param inputStream O fluxo de bytes (conteúdo) do ficheiro a ser guardado.
     * @return Uma string representando a localização ou identificador do ficheiro guardado (ex: o caminho completo ou URL).
     */
    String guardar(String nomeFicheiroStorage, InputStream inputStream);

    /**
     * Apaga um ficheiro físico do sistema de armazenamento.
     *
     * @param nomeFicheiroStorage O nome único do ficheiro a ser apagado.
     */
    void apagar(String nomeFicheiroStorage);

    // Futuramente, poderíamos adicionar um método para recuperar o ficheiro:
    // InputStream carregar(String nomeFicheiroStorage);
}
