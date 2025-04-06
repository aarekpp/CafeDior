package com.lemurybiznesu.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    private final String baseDir = "/images/";

    public void validateImages(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Invalid image format");
            }
        }
    }

    public String saveFile(MultipartFile file, String targetDir) {
        String newFileName = generateRandomFileName(file);
        File destinationFile = new File(baseDir + targetDir + File.separator + newFileName);
        destinationFile.getParentFile().mkdirs();
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
        return newFileName;
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

