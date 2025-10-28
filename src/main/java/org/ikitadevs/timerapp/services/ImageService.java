package org.ikitadevs.timerapp.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final FileStorageService fileStorageService;

}
