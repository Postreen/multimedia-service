package com.rugid.multimediaservice.domain.port.in;

public interface UploadFileUseCase {

    String uploadImage(UploadFileCommand uploadFileCommand);

    record UploadFileCommand(

            byte[] imageData,

            String extension
    ) {
    }
}
