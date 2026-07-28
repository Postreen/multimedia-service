package com.rugid.multimediaservice.adapter.in.rest.controller;

import com.rugid.multimediaservice.adapter.in.rest.dto.DeleteVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.RetrieveDefaultVideoIdResponse;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoRequest;
import com.rugid.multimediaservice.adapter.in.rest.dto.UploadVideoResponse;
import com.rugid.multimediaservice.adapter.in.rest.validator.FileValidator;
import com.rugid.multimediaservice.adapter.in.rest.validator.JsonDtoValidator;
import com.rugid.multimediaservice.domain.core.exception.FileReadingException;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import com.rugid.multimediaservice.domain.port.in.UploadFileUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoEndpoint {

    private final GetDefaultFileUrlUseCase getDefaultFileUrlUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final JsonDtoValidator<DeleteVideoRequest> deleteVideoRequestValidator;
    private final JsonDtoValidator<UploadVideoRequest> uploadVideoRequestValidator;
    private final FileValidator fileValidator;

    private static final Set<String> VIDEO_EXTENSIONS = Set.of(
            "mp4", "avi", "mov", "mkv"
    );

    @GetMapping("/default")
    public ResponseEntity<RetrieveDefaultVideoIdResponse> getDefaultVideoId() {
        String defaultVideoId = getDefaultFileUrlUseCase.getDefaultVideoId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RetrieveDefaultVideoIdResponse(defaultVideoId));
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadVideoResponse> uploadVideo(
            @ModelAttribute("request") UploadVideoRequest request) {

        uploadVideoRequestValidator.validate(request);
        UploadFileUseCase.UploadFileCommand uploadFileCommand = createUploadVideoCommand(request);
        String videoId = uploadFileUseCase.upload(uploadFileCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UploadVideoResponse(videoId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteVideo(@RequestBody DeleteVideoRequest request) {
        deleteVideoRequestValidator.validate(request);

        DeleteFileUseCase.DeleteFileCommand deleteVideoCommand = createDeleteVideoCommand(request);
        deleteFileUseCase.delete(deleteVideoCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    private UploadFileUseCase.UploadFileCommand createUploadVideoCommand(UploadVideoRequest request) {
        MultipartFile videoFile  = request.video();

        fileValidator.validate(videoFile , VIDEO_EXTENSIONS);

        try {
            return new UploadFileUseCase.UploadFileCommand(
                    videoFile .getBytes(),
                    FilenameUtils.getExtension(videoFile .getOriginalFilename())
            );
        } catch (IOException e) {
            throw new FileReadingException(e);
        }
    }

    private DeleteFileUseCase.DeleteFileCommand createDeleteVideoCommand(DeleteVideoRequest request) {
        return new DeleteFileUseCase.DeleteFileCommand(request.videoId());
    }
}
