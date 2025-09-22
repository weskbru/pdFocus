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
 * utilizando o sistema de ficheiros local como destino.
 */
@Component
public class LocalFileStorageAdapter implements MaterialStoragePort {

    private final Path rootLocation;

    /**
     * Constrói o adaptador. O caminho do diretório de uploads é injetado
     * a partir do ficheiro application.properties via anotação @Value.
     *
     * @param storageDirectory O caminho para o diretório de uploads (ex: "uploads").
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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

