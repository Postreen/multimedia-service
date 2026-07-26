package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteFileService implements DeleteFileUseCase {

    private final FileOutputPort fileOutputPort;

    @Override
    public void delete(DeleteFileCommand deleteFileCommand) {
        String fileId = deleteFileCommand.fileId();
        fileOutputPort.delete(fileId);
    }
}
