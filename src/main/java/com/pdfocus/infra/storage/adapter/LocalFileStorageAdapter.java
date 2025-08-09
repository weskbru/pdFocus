package com.pdfocus.infra.storage.adapter;

import com.pdfocus.application.material.port.saida.MaterialStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Injeta o caminho do diretório de armazenamento a partir do application.properties.
     */
    @Value("${storage.local.directory}")
    private String diretorioStorage;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação guarda o ficheiro numa pasta no sistema de ficheiros local.
     * </p>
     */
    @Override
    public String guardar(String nomeFicheiroStorage, InputStream inputStream) {
        try {
            Path diretorioPath = Paths.get(diretorioStorage);
            Files.createDirectories(diretorioPath);
            Path destinoPath = diretorioPath.resolve(nomeFicheiroStorage);
            Files.copy(inputStream, destinoPath, StandardCopyOption.REPLACE_EXISTING);
            return destinoPath.toAbsolutePath().toString();
        } catch (IOException e) {
            // Em caso de erro, lança uma exceção.
            throw new RuntimeException("Falha ao guardar o ficheiro: " + nomeFicheiroStorage, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementação apaga o ficheiro do sistema de ficheiros local.
     * </p>
     */
    @Override
    public void apagar(String nomeFicheiroStorage) {
        try {
            Path ficheiroPath = Paths.get(diretorioStorage).resolve(nomeFicheiroStorage);
            Files.deleteIfExists(ficheiroPath);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao apagar o ficheiro: " + nomeFicheiroStorage, e);
        }
    }
}
