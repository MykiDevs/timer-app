package org.ikitadevs.timerapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile multipartFile, String subfolder, String fileType);
    void deleteFile(String filePath) throws IOException;
}
