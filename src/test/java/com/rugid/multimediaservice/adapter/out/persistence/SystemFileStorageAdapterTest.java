package com.rugid.multimediaservice.adapter.out.persistence;

import com.rugid.multimediaservice.domain.core.exception.FileWritingException;
import com.rugid.multimediaservice.domain.core.exception.NoSuchFileRuntimeException;
import com.rugid.multimediaservice.domain.core.exception.WrongPathException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SystemFileStorageAdapterTest {
    @TempDir(cleanup = CleanupMode.ALWAYS)
    private static File tempDir;
    private final String storageFolderPath = tempDir.getPath();

    private SystemFileStorageAdapter fileStorageAdapter;

    @BeforeEach
    void setUp() {
        fileStorageAdapter = new SystemFileStorageAdapter(storageFolderPath);
    }

    @Test
    void testUpload_whenSuccessfullyUploadsFile() {
        var testData = "Test".getBytes();
        var extension = "txt";

        var fileId = fileStorageAdapter.upload(testData, extension);

        assertNotNull(fileId);
    }

    @Test
    void testUpload_CheckUniqueFileId() {
        var testData = "test data 1".getBytes();
        var testData1 = "test data 2".getBytes();
        var extension = "jpg";

        String fileId = fileStorageAdapter.upload(testData, extension);
        String fileId2 = fileStorageAdapter.upload(testData1, extension);

        assertNotEquals(fileId, fileId2);
    }

    @Test
    void testDownload_whenValidData_thenDownloadedFile() throws IOException {
        var testData = "test data".getBytes();
        var extension = "jpg";
        String fileId = fileStorageAdapter.upload(testData, extension);

        InputStreamResource resource = fileStorageAdapter.download(fileId);

        assertArrayEquals(resource.getContentAsByteArray(), testData);
    }

    @Test
    void testDownload_whenFileNotFound_thenReturnedThrow() {
        Throwable ex = assertThrows(NoSuchFileRuntimeException.class, () -> {
            fileStorageAdapter.download("null");
        });

        assertEquals("File not found", ex.getMessage());
    }


    @Test
    void testDelete_whenValidData_thenDeletedFile() {
        var testData = "test data".getBytes();
        var extension = "jpg";
        String fileId = fileStorageAdapter.upload(testData, extension);

        fileStorageAdapter.delete(fileId);
    }

    @Test
    void testDelete_whenFileNotFound_thenReturnedThrow() {
        Throwable ex = assertThrows(NoSuchFileRuntimeException.class,
                () -> fileStorageAdapter.delete("null"));

        assertEquals("File not found", ex.getMessage());
    }

    @Test
    void testDownload_whenFileIdIsNull_thenThrowWrongPathException() {
        assertThrows(WrongPathException.class,
                () -> fileStorageAdapter.download(null));
    }

    @Test
    void testDownload_whenFileIdIsBlank_thenThrowWrongPathException() {
        assertThrows(WrongPathException.class,
                () -> fileStorageAdapter.download(" "));
    }

    @Test
    void testDelete_whenFileIdIsNull_thenThrowWrongPathException() {
        assertThrows(WrongPathException.class,
                () -> fileStorageAdapter.delete(null));
    }
}