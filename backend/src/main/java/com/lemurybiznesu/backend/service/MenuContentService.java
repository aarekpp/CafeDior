package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.request.ImageRequest;
import com.lemurybiznesu.backend.model.dto.response.ImageResponse;
import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.MenuContent;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.MenuContentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuContentService {
    private final MenuContentRepository menuContentRepository;
    private final FileService fileService;
    private final AuthService authService;

    public MenuContentService(MenuContentRepository menuContentRepository, FileService fileService, AuthService authService) {
        this.menuContentRepository = menuContentRepository;
        this.fileService = fileService;
        this.authService = authService;
    }

    public List<ImageResponse> getMenuContents() {
        List<MenuContent> contents =  menuContentRepository.findAllByOrderByDisplayOrderAsc();
        List<ImageResponse> responses = new ArrayList<>();

        for (MenuContent content : contents) {
            ImageResponse imageResponse = new ImageResponse();
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/menu")
                    .path(content.getFileName())
                    .toUriString();

            imageResponse.setId(content.getId());
            imageResponse.setUrl(url);
            imageResponse.setDisplayOrder(content.getDisplayOrder());
            responses.add(imageResponse);
        }

        return responses;
    }

    @Transactional
    public List<ImageResponse> addMenuContents(ImageRequest imageRequest, HttpServletRequest request) {
        User user = authService.getCurrentUser(request);
        if (!user.getRole().getName().equals(ERole.MODERATOR)) {
            throw new RuntimeException("Only moderators can create about content");
        }

        if (imageRequest.getFiles().size() != imageRequest.getDisplayOrders().size()) {
            throw new RuntimeException("Number of files and orders don't match");
        }
        try{
            fileService.validateImages(imageRequest.getFiles());
            List<ImageResponse> responses = new ArrayList<>();
            for (int i = 0; i < imageRequest.getFiles().size(); i++) {
                MultipartFile file = imageRequest.getFiles().get(i);
                Integer order = imageRequest.getDisplayOrders().get(i);
                String storedFileName = fileService.saveFile(file, "menu/");
                MenuContent menuContent = new MenuContent();
                menuContent.setFileName(storedFileName);
                menuContent.setDisplayOrder(order);
                menuContent.setLastModifiedBy(user);
                menuContent = menuContentRepository.save(menuContent);

                responses.add(new ImageResponse(menuContent.getId(), storedFileName, order));
            }
            return responses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
