package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.DownloadFileUseCase;
import com.rugid.multimediaservice.domain.port.out.FileOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadFileService implements DownloadFileUseCase {

    private final FileOutputPort fileOutputPort;

    @Override
    public InputStreamResource download(String fileId) {
        return fileOutputPort.download(fileId);
    }
}
