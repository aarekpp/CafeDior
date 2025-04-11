package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.request.ImageUpdateRequest;
import com.lemurybiznesu.backend.model.dto.request.ImagesRequest;
import com.lemurybiznesu.backend.model.entity.EImageContent;
import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.ImageContent;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.ImageContentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ImageContentService {
    private final ImageContentRepository imageContentRepository;
    private final FileService fileService;
    private final AuthService authService;

    public ImageContentService(ImageContentRepository imageContentRepository, FileService fileService, AuthService authService) {
        this.imageContentRepository = imageContentRepository;
        this.fileService = fileService;
        this.authService = authService;
    }

    public List<ImageContent> getAllImages() {
        return new ArrayList<>();
    }

    @Transactional
    public List<ImageContent> addImagesContent(ImagesRequest imagesRequest, HttpServletRequest request) {
        if(imagesRequest.getFiles().size() != 1 || imagesRequest.getSections().size() != 1) {
            throw new RuntimeException("Size of sections do not match");
        }

        User user = authService.getCurrentUser(request);
        if(!user.getRole().getName().equals(ERole.MODERATOR)){
            throw new RuntimeException("Only moderators can add images");
        }

        List<ImageContent> result = new ArrayList<>();
        List<String> savedFiles = new ArrayList<>();
        Set<EImageContent> processedSections = new HashSet<>();

        for(int i = 0; i < imagesRequest.getFiles().size(); i++) {
            MultipartFile file = imagesRequest.getFiles().get(i);
            EImageContent section = EImageContent.valueOf(imagesRequest.getSections().get(i));

            if(!processedSections.add(section)) {
                throw new RuntimeException("Duplicate section");
            }
            if(imageContentRepository.existsByImageContentType(section)){
                throw new RuntimeException("Duplicate section");
            }

            fileService.validateFile(file);
            String filename = fileService.saveFile(file);
            savedFiles.add(filename);

            ImageContent imageContent = new ImageContent();
            imageContent.setFilename(filename);
            imageContent.setOriginalFilename(file.getOriginalFilename());
            imageContent.setImageContentType(section);
            imageContent.setLastModifiedBy(user);

            try{
                result.add(imageContentRepository.save(imageContent));
            }catch (Exception e){
                savedFiles.forEach(fileService::deleteFileSilently);
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    @Transactional
    public ImageContent changeImageContent(Long id, ImageUpdateRequest imageUpdateRequest, HttpServletRequest request) {
        if(imageUpdateRequest.getFile().isEmpty() || imageUpdateRequest.getType() == null){
            throw new RuntimeException("Invalid image update data");
        }

        User user = authService.getCurrentUser(request);
        if(!user.getRole().getName().equals(ERole.MODERATOR)){
            throw new RuntimeException("Only moderators can add images");
        }

        ImageContent existingImage = imageContentRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));

        String newFilename = null;
        try {
            fileService.validateFile(imageUpdateRequest.getFile());
            newFilename = fileService.saveFile(imageUpdateRequest.getFile());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error during saving new file", e);
        }

        try {
            String oldFilename = existingImage.getFilename();
            existingImage.setFilename(newFilename);
            existingImage.setOriginalFilename(imageUpdateRequest.getFile().getOriginalFilename());
            existingImage.setImageContentType(imageUpdateRequest.getType());
            existingImage.setLastModifiedBy(user);

            imageContentRepository.save(existingImage);

            fileService.deleteFile(oldFilename);

            return existingImage;
        } catch (Exception e) {
            if (newFilename != null) {
                fileService.deleteFile(newFilename);
            }
            throw new RuntimeException("Update error", e);
        }
    }

    @Transactional
    public void deleteImageContent(Long id, HttpServletRequest request) {
        User user = authService.getCurrentUser(request);
        if (!user.getRole().getName().equals(ERole.MODERATOR)) {
            throw new RuntimeException("Only moderators can add images");
        }

        ImageContent image = imageContentRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));

        String filename = image.getFilename();


        try {
            imageContentRepository.delete(image);
            fileService.deleteFileSilently(filename);
        } catch (Exception e) {
            throw new RuntimeException("Delete error", e);
        }
    }
}
