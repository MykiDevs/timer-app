package org.ikitadevs.timerapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor

public class LocalStorageService implements FileStorageService {

    @Value("${upload.path}")
    private String uploadPath;

    private String subfolder = "images";

    private final ImageValidationService imageValidationService;



    @Override
    public String storeFile(MultipartFile file) {
        imageValidationService.validate(file);
        File uploadDir = new File(uploadPath + "/" + subfolder);
        if(!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
        String filename = file.getOriginalFilename();
        String extension =  filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        String newFilename = String.format("%s-%d.%s",
                uuid,
                System.currentTimeMillis(),
                extension);
        File destinationFile = new File(uploadPath + "/" + newFilename);
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException("Transferring error!");
        }

        return destinationFile.toString();
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path uploadDirPath = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path fileToDelete = uploadDirPath.resolve(filePath).normalize();
        if(!fileToDelete.startsWith(uploadDirPath)) throw new IOException("Attempt to delete file outside of the upload directory: " + filePath);
    }

}
