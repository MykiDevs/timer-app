package org.ikitadevs.timerapp.factories;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.services.FileValidationService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j

public class FileValidationFactory {
    private final Map<String, FileValidationService> validators;


    @PostConstruct
    public void init() {
        log.info("--- FileValidationFactory Initialized ---");
        log.info("Available validators found: {}", validators.keySet());
    }
    public FileValidationService getValidator(String type) {
        System.out.println(validators.toString());
        String validatorName = type + "ValidationService";
        return Optional.ofNullable(validators.get(validatorName))
                .orElseThrow(() -> new IllegalArgumentException("No validator found for type: " + type));
    }
}