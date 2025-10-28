package org.ikitadevs.timerapp.services;

import org.apache.tika.Tika;
import org.ikitadevs.timerapp.exceptions.FileValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public abstract class FileValidationService {
    private final Tika tika = new Tika();
    public void validate(MultipartFile file, long maxSizeInMb, List<String> allowedExtensions, List<String> allowedMimetypes) {
        if(file.isEmpty()) throw new FileValidationException("File is empty!");
        validateSize(file.getSize(), maxSizeInMb);
        String extension = validateExtension(file.getOriginalFilename(), allowedExtensions);
        validateMimetype(file, allowedMimetypes, extension);
    }

    private void validateSize(long filesize, long maxSizeInMb) {
        long maxSizeInBytes = maxSizeInMb * 1024 * 1024;
        if(filesize > maxSizeInBytes) throw new FileValidationException("File size must be lower than " + maxSizeInBytes);
    }

    private String validateExtension(String filename, List<String> allowedExtensions) throws FileValidationException {
        if(filename == null || !filename.contains(".")) throw new FileValidationException("Recheck your filename!");
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if(!allowedExtensions.contains(fileExtension)) throw new FileValidationException("Extension of file is invalid! Allowed is " + allowedExtensions.toString());
        return fileExtension;
    }

    private void validateMimetype(MultipartFile file, List<String> allowedMimetypes, String extension) {
        try {
            String detectedMymetype = tika.detect(file.getInputStream());
            if(!allowedMimetypes.contains(detectedMymetype)) throw new FileValidationException("Invalid filetype. File is " + detectedMymetype + ", not like " + extension);
        } catch (IOException e) {
            throw new FileValidationException("Could not read file to determine MIME. " + e.getMessage());
        }
    }

    public abstract void validate(MultipartFile file);
}
