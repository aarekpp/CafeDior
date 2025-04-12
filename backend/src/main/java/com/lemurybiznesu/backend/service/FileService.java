package com.lemurybiznesu.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    @Value("${app.images.path}")
    private String imagesPath;

    public void validateFile(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Incorrect image type");
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            String filename = generateFilename(file);
            Path path = Paths.get(imagesPath, filename);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Error during file save: ", e);
        }
    }

    public void deleteFile(String filename) {
        try {
            Path path = Paths.get(imagesPath, filename);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Error during file delete: ", e);
        }
    }

    public void deleteFileSilently(String filename) {
        try {
            Path path = Paths.get(imagesPath, filename);
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            throw new RuntimeException("Error during file delete: ", ignored);
        }
    }

    private String generateFilename(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            int lastDotIndex = originalFilename.lastIndexOf('.');
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex + 1);
            }
        }

        return UUID.randomUUID() + (!extension.isEmpty() ? "." + extension : "");
    }
}
