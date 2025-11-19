package org.ikitadevs.timerapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.exceptions.FileStorageException;
import org.ikitadevs.timerapp.factories.FileValidationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor

public class LocalStorageService implements FileStorageService {

    @Value("${upload.path}")
    private String uploadPath;

    private final FileValidationFactory fileValidationFactory;


    @Override
    public String storeFile(MultipartFile file, String subfolder, String fileType) {
        FileValidationService validator = fileValidationFactory.getValidator("image");
        validator.validate(file);


        String origFilename = file.getOriginalFilename();
        if (origFilename == null || origFilename.isEmpty()) {
            throw new IllegalArgumentException("Original filename must not be empty!");
        }
        String filename = StringUtils.cleanPath(origFilename);
        int dotIndex = filename.lastIndexOf('.');
        String extension = (dotIndex > 0 && dotIndex < filename.length() - 1)
                ? filename.substring(dotIndex + 1).toLowerCase()
                : "";
        if(extension.isEmpty()) throw new FileStorageException("File extension is missing");
        String newFilename = UUID.randomUUID() + "." + extension;
        Path uploadDirPath = Paths.get(this.uploadPath, subfolder).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create dir: " + uploadDirPath, e);
        }
        Path destinationPath = uploadDirPath.resolve(newFilename).normalize();

        if(!destinationPath.startsWith(uploadDirPath)) throw new RuntimeException("Attempted to store file outside of the upload directory: " + newFilename);

        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + newFilename, e);
        }
        return Paths.get(subfolder, newFilename).toString();
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path uploadDirPath = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path fileToDelete = uploadDirPath.resolve(filePath).normalize();
        if(!fileToDelete.startsWith(uploadDirPath)) throw new IOException("Attempt to delete file outside of the upload directory: " + filePath);
        if (Files.exists(fileToDelete)) {
            Files.delete(fileToDelete);
            log.info("File deleted successfully: {}", filePath);
        } else {
            log.warn("File to delete does not exist: {}", filePath);
        }
    }

}
