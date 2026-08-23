package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.rugid.multimediaservice.adapter.in.rest.dto.DeleteImageRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.RetrieveDefaultImageIdResponse;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadImageRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadImageResponse;
import com.rugid.multimediaservice.adapter.in.rest.validator.FileValidator;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.core.exception.FileReadingException;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.in.DownloadFileUseCase;
import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageEndpoint {

    private final DownloadFileUseCase downloadFileUseCase;
    private final GetDefaultFileUrlUseCase getDefaultFileUrlUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final JsonDtoValidator<UploadImageRequest> uploadImageRequestValidator;
    private final JsonDtoValidator<DeleteImageRequest> deleteImageRequestValidator;
    private final FileValidator fileValidator;

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    @GetMapping
    public ResponseEntity<Resource> downloadImage(@RequestParam(name = "imageId") String imageId) {
        InputStreamResource image = downloadFileUseCase.download(imageId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/octet-stream"))
                .body(image);
    }

    @GetMapping("/default")
    public ResponseEntity<RetrieveDefaultImageIdResponse> getDefaultImageId() {
        String defaultImageId = getDefaultFileUrlUseCase.getDefaultImageId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RetrieveDefaultImageIdResponse(defaultImageId));
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadImageResponse> uploadImage(@ModelAttribute("request") UploadImageRequest request) {
        uploadImageRequestValidator.validate(request);

        UploadFileUseCase.UploadFileCommand uploadFileCommand = createUploadImageCommand(request);
        String imageId = uploadFileUseCase.upload(uploadFileCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UploadImageResponse(imageId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestBody DeleteImageRequest request) {
        deleteImageRequestValidator.validate(request);

        DeleteFileUseCase.DeleteFileCommand deleteImageCommand = createDeleteImageCommand(request);
        deleteFileUseCase.delete(deleteImageCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    private UploadFileUseCase.UploadFileCommand createUploadImageCommand(UploadImageRequest request) {
        MultipartFile image = request.image();

        fileValidator.validate(image, IMAGE_EXTENSIONS);

        try {
            return new UploadFileUseCase.UploadFileCommand(
                    image.getBytes(),
                    FilenameUtils.getExtension(image.getOriginalFilename())
            );
        } catch (IOException e) {
            throw new FileReadingException(e);
        }
    }

    private DeleteFileUseCase.DeleteFileCommand createDeleteImageCommand(DeleteImageRequest request) {
        return new DeleteFileUseCase.DeleteFileCommand(request.imageId());
    }
}
