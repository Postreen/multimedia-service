package com.rugid.multimediaservice.adapter.in.rest.validator;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Component
public class FilePathValidator {

    private static final String EXTENSION_PATTERN = "^[a-zA-Z0-9]{2,5}$";

    public void validateExtension(String extension) {
        if (extension == null || !extension.matches(EXTENSION_PATTERN)) {
            throw new WrongPathException("Invalid extension format: " + extension);
        }
    }

    public Path validateAndResolvePath(Path storageFolder, String fileId) {
        try {
            var filePath = storageFolder.resolve(fileId);
            var normalizedFilePath = filePath.normalize();

            if (!normalizedFilePath.startsWith(storageFolder.normalize())) {
                throw new WrongPathException("Incorrect fileId: " + fileId);
            }

            if (!Files.exists(normalizedFilePath)) {
                throw new UserFileNotFoundException("File not found: " + fileId);
            }

            if (!Files.isRegularFile(normalizedFilePath)) {
                throw new WrongPathException("Incorrect fileId: " + fileId);
            }

            return normalizedFilePath;
        } catch (InvalidPathException e) {
            throw new WrongPathException("Invalid file id: " + fileId, e);
        }
    }
}
