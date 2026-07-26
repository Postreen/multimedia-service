package com.rugid.multimediaservice.domain.port.in;

import org.springframework.core.io.InputStreamResource;

public interface DownloadFileUseCase {

    InputStreamResource download(String fileId);
}
