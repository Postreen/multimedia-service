package com.rugid.multimediaservice.domain.core.service;

import com.rugid.multimediaservice.domain.port.in.GetDefaultFileUrlUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetDefaultFileUrlService implements GetDefaultFileUrlUseCase {

    private static final String DEFAULT_IMAGE_ID = "default-image";
    private static final String DEFAULT_VIDEO_ID = "default-video";

    @Override
    public String getDefaultImageId() {
        return DEFAULT_IMAGE_ID;
    }

    @Override
    public String getDefaultVideoId() {
        return DEFAULT_VIDEO_ID;
    }
}
