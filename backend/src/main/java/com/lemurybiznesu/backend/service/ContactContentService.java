package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.request.ImageRequest;
import com.lemurybiznesu.backend.model.dto.response.ImageResponse;
import com.lemurybiznesu.backend.model.entity.ContactContent;
import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.ContactContentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactContentService {
    private final ContactContentRepository contactContentRepository;
    private final FileService fileService;
    private final AuthService authService;

    public ContactContentService(ContactContentRepository contactContentRepository, FileService fileService, AuthService authService) {
        this.contactContentRepository = contactContentRepository;
        this.fileService = fileService;
        this.authService = authService;
    }

    public List<ImageResponse> getContactContents() {
        List<ContactContent> contents =  contactContentRepository.findAllByOrderByDisplayOrderAsc();
        List<ImageResponse> responses = new ArrayList<>();

        for (ContactContent content : contents) {
            ImageResponse imageResponse = new ImageResponse();
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/contact")
                    .path(content.getFileName())
                    .toUriString();

            imageResponse.setId(content.getId().toString());
            imageResponse.setUrl(url);
            imageResponse.setDisplayOrder(content.getDisplayOrder());
            responses.add(imageResponse);
        }

        return responses;
    }

    @Transactional
    public List<ImageResponse> addContactContents(ImageRequest imageRequest, HttpServletRequest request) {
        User user = authService.getCurrentUser(request);
        if (!user.getRole().getName().equals(ERole.MODERATOR)) {
            throw new RuntimeException("Only moderators can create about content");
        }

        if (imageRequest.getFiles().size() != imageRequest.getDisplayOrders().size() || imageRequest.getFiles().size() > 2) {
            throw new RuntimeException("Number of files and orders don't match");
        }

        try{
            fileService.validateImages(imageRequest.getFiles());
            List<ImageResponse> responses = new ArrayList<>();
            for (int i = 0; i < imageRequest.getFiles().size(); i++) {
                MultipartFile file = imageRequest.getFiles().get(i);
                Integer order = imageRequest.getDisplayOrders().get(i);
                String storedFileName = fileService.saveFile(file, "contact/");
                ContactContent contactContent = new ContactContent();
                contactContent.setFileName(storedFileName);
                contactContent.setDisplayOrder(order);
                contactContent.setLastModifiedBy(user);
                contactContent = contactContentRepository.save(contactContent);

                responses.add(new ImageResponse(contactContent.getId().toString(), storedFileName, order));
            }
            return responses;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
