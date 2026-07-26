package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.domain.core.exception.*;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

@Component
public class SystemFileStorageAdapter implements FileOutputPort {

    private final Path storageFolder;

    public SystemFileStorageAdapter(
            @Value("${rugid.images.folder-path}") String storageFolder
    ) {
        this.storageFolder = Path.of(storageFolder)
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public String upload(byte[] data, String extension) {
        String filename = generateFilename(data);

        Path path = storageFolder.resolve(
                filename + "." + extension
        );

        saveFile(path, data);

        return path.getFileName().toString();
    }

    @Override
    public InputStreamResource download(String fileId) {
        Path path = resolveSafePath(fileId);

        return new InputStreamResource(
                new ByteArrayInputStream(readFile(path))
        );
    }

    @Override
    public void delete(String fileId) {
        deleteFile(resolveSafePath(fileId));
    }


    private byte[] readFile(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (NoSuchFileException e) {
            throw new NoSuchFileRuntimeException(e);
        } catch (IOException e) {
            throw new FileReadingException(e);
        }
    }


    private void saveFile(Path path, byte[] data) {
        try {
            initFolder();
            Files.write(path, data);
        } catch (IOException e) {
            throw new FileWritingException();
        }
    }


    private void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            throw new NoSuchFileRuntimeException(e);
        } catch (IOException e) {
            throw new FileDeletingException(e);
        }
    }


    private void initFolder() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            throw new FolderInitializationException(e);
        }
    }


    private Path resolveSafePath(String fileId) {

        if (fileId.contains("/") ||
                fileId.contains("\\") ||
                fileId.contains("..")) {

            throw new WrongPathException();
        }

        Path path = storageFolder
                .resolve(fileId)
                .normalize();

        if (!path.startsWith(storageFolder)) {
            throw new WrongPathException();
        }

        return path;
    }

    private String generateFileId(Path filename) {
        return filename.getFileName().toString();
    }

    private Path generateFilePath(String filename, String extension) {
        return storageFolder.resolve(filename + "." + extension);
    }

    private String generateFilename(byte[] fileData) {
        return generateFileHash(fileData);
    }

    private String generateFileHash(byte[] file) {
        return DigestUtils.md5DigestAsHex(file);
    }
}
