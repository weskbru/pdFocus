package com.pdfocus.infra.storage.adapter;

import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Adaptador que implementa a porta de armazenamento {@link MaterialStoragePort}
 * utilizando o sistema de arquivos local como destino.
 *
 * <p>Todos os ficheiros são armazenados no diretório configurado via
 * {@code application.properties} (propriedade: {@code storage.local.directory}).</p>
 *
 * <p>Este adaptador fornece operações básicas de CRUD para ficheiros:
 * guardar, carregar e apagar.</p>
 */
@Component
public class LocalFileStorageAdapter implements MaterialStoragePort {

    private final Path rootLocation;

    /**
     * Constrói o adaptador e inicializa o diretório de armazenamento.
     *
     * <p>Se o diretório não existir, ele será criado automaticamente.</p>
     *
     * @param storageDirectory O caminho para o diretório de uploads (ex: "uploads").
     * @throws RuntimeException Se o diretório não puder ser criado.
     */
    public LocalFileStorageAdapter(@Value("${storage.local.directory}") String storageDirectory) {
        this.rootLocation = Paths.get(storageDirectory);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretório de armazenamento.", e);
        }
    }

    /**
     * Guarda um ficheiro no sistema de arquivos local.
     *
     * <p>Se já existir um ficheiro com o mesmo nome, ele será substituído.</p>
     *
     * @param nomeFicheiroStorage Nome do ficheiro a ser armazenado.
     * @param inputStream Fluxo de dados do ficheiro.
     * @throws RuntimeException Se ocorrer falha ao guardar o ficheiro.
     */
    @Override
    public void guardar(String nomeFicheiroStorage, InputStream inputStream) {
        try {
            Path destinoPath = this.rootLocation.resolve(nomeFicheiroStorage);
            Files.copy(inputStream, destinoPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao guardar o ficheiro: " + nomeFicheiroStorage, e);
        }
    }

    /**
     * Apaga um ficheiro do sistema de arquivos local.
     *
     * <p>Se o ficheiro não existir, a operação será ignorada.</p>
     *
     * @param nomeFicheiroStorage Nome do ficheiro a ser apagado.
     * @throws RuntimeException Se ocorrer falha ao apagar o ficheiro.
     */
    @Override
    public void apagar(String nomeFicheiroStorage) {
        try {
            Path ficheiroPath = this.rootLocation.resolve(nomeFicheiroStorage);
            Files.deleteIfExists(ficheiroPath);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao apagar o ficheiro: " + nomeFicheiroStorage, e);
        }
    }

    /**
     * Carrega um ficheiro do sistema de arquivos local como um {@link Resource}.
     *
     * @param nomeFicheiro Nome do ficheiro a ser carregado.
     * @return {@link Resource} representando o ficheiro carregado.
     * @throws RuntimeException Se o ficheiro não puder ser lido ou a URL for inválida.
     */
    @Override
    public Resource carregar(String nomeFicheiro) {
        try {
            Path file = this.rootLocation.resolve(nomeFicheiro);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não foi possível ler o ficheiro: " + nomeFicheiro);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao formar a URL para o ficheiro: " + nomeFicheiro, e);
        }
    }
}
