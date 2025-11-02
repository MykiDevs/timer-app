package org.ikitadevs.timerapp.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@Qualifier("imageValidationService")
public class ImageValidationService extends FileValidationService {
    private static final long maxSizeInMb = 10;
    private static final List<String> allowedExtensions = Arrays.asList("jpg", "png", "webp", "jpeg");
    private static final List<String> allowedMimetypes = Arrays.asList("image/jpeg", "image/png", "image/jpeg", "image/webp");

    @Override
    public void validate(MultipartFile file) {
        super.validate(file, maxSizeInMb, allowedExtensions, allowedMimetypes);
    }
}