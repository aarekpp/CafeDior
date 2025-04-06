package com.lemurybiznesu.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    private final Path rootLocation;

    public FileService(@Value("${app.images.path}") String uploadPath) {
        this.rootLocation = Paths.get(uploadPath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public void validateImages(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Invalid image format");
            }
        }
    }

    public String saveFile(MultipartFile file, String targetDir) {
        try {
            Path targetPath = this.rootLocation.resolve(targetDir).normalize();
            if (!targetPath.startsWith(this.rootLocation)) {
                throw new RuntimeException("Invalid path");
            }
            Files.createDirectories(targetPath);
            String newFileName = generateRandomFileName(file);
            Path destinationFile = targetPath.resolve(newFileName);
            file.transferTo(destinationFile.toFile());
            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }

    private String generateRandomFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        String ext = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            ext = originalFilename.substring(dotIndex + 1);
        }
        String randomFileName = UUID.randomUUID().toString();
        if (!ext.isEmpty()) {
            randomFileName += "." + ext;
        }
        return randomFileName;
    }
}

