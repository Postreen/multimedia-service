package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadFileService implements UploadFileUseCase {

    private final FileOutputPort fileOutputPort;

    @Override
    public String upload(UploadFileCommand command) {
        return fileOutputPort.upload(
                command.fileData(),
                command.extension()
        );
    }
}
