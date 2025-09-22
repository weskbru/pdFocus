package com.pdfocus.application.material.port.saida;

import org.springframework.core.io.Resource;
import java.io.InputStream;

/**
 * Define o contrato (Porta de Saída) para o sistema de armazenamento físico de ficheiros.
 * Esta interface abstrai a forma como os ficheiros são guardados (localmente, na nuvem, etc.).
 */
public interface MaterialStoragePort {

    /**
     * Guarda o conteúdo de um InputStream no sistema de armazenamento.
     *
     * @param nomeFicheiro O nome único sob o qual o ficheiro será guardado.
     * @param inputStream O fluxo de dados do ficheiro a ser guardado.
     */
    void guardar(String nomeFicheiro, InputStream inputStream);

    /**
     * Apaga um ficheiro do sistema de armazenamento.
     *
     * @param nomeFicheiro O nome único do ficheiro a ser apagado.
     */
    void apagar(String nomeFicheiro);

    /**
     * Carrega um ficheiro do sistema de armazenamento como um recurso (Resource).
     *
     * @param nomeFicheiro O nome único do ficheiro a ser carregado.
     * @return um objeto Resource que representa o ficheiro.
     */
    Resource carregar(String nomeFicheiro);
}