package com.rugid.multimediaservice.domain.port.in;

public interface UploadFileUseCase {

    String upload(UploadFileCommand command);

    record UploadFileCommand(
            byte[] fileData,
            String extension
    ) {
    }
}
