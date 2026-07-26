package com.rugid.multimediaservice.adapter.in.rest.validator;

import com.rugid.multimediaservice.adapter.in.exception.InvalidInputException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Set;

@Component
public class FileValidator {

    public void validate(MultipartFile file, Set<String> allowedExtensions) {
        if (file == null || file.isEmpty()) {
            throw new InvalidInputException("File is empty");
        }

        String filename = file.getOriginalFilename();

        if (filename == null || filename.isBlank()) {
            throw new InvalidInputException("Filename is empty");
        }

        String extension = FilenameUtils.getExtension(filename).toLowerCase(Locale.ROOT);

        if (!allowedExtensions.contains(extension)) {
            throw new InvalidInputException("Unsupported file extension");
        }
    }
}
