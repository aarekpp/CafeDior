package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.request.AboutContentRequest;
import com.lemurybiznesu.backend.model.entity.AboutContent;
import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.AboutContentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AboutContentService {
    private final AboutContentRepository aboutContentRepository;
    private final AuthService authService;

    public AboutContentService(AboutContentRepository aboutContentRepository, AuthService authService) {
        this.aboutContentRepository = aboutContentRepository;
        this.authService = authService;
    }

    public Optional<AboutContent> getAboutContent() {
        return aboutContentRepository.findFirstByOrderByCreatedAtAsc();
    }

    @Transactional
    public AboutContent createAboutContent(AboutContentRequest aboutContentRequest, HttpServletRequest request) {
        User user = authService.getCurrentUser(request);
        if(!user.getRole().getName().equals(ERole.MODERATOR)) {
            throw new RuntimeException("Only moderators can create about content");
        }

        if(getAboutContent().isPresent()){
            throw new RuntimeException("About content already exists");
        }

        AboutContent aboutContent = new AboutContent();
        aboutContent.setText(aboutContentRequest.getText());
        aboutContent.setLastModifiedBy(user);

        try{
            return aboutContentRepository.save(aboutContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AboutContent updateAboutContent(AboutContentRequest aboutContentRequest, HttpServletRequest request) {
        User user = authService.getCurrentUser(request);
        if(!user.getRole().getName().equals(ERole.MODERATOR)) {
            throw new RuntimeException("Only moderators can create about content");
        }

        Optional<AboutContent> aboutContent = getAboutContent();
        if(aboutContent.isEmpty()){
            throw new RuntimeException("About content already exists");
        }

        AboutContent contentToUpdate = aboutContent.get();
        contentToUpdate.setText(aboutContentRequest.getText());
        contentToUpdate.setLastModifiedBy(user);

        try{
            return aboutContentRepository.save(contentToUpdate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean deleteAboutContent(HttpServletRequest request) {
        User user = authService.getCurrentUser(request);
        if(!user.getRole().getName().equals(ERole.MODERATOR)) {
            throw new RuntimeException("Only moderators can create about content");
        }

        Optional<AboutContent> aboutContent = getAboutContent();
        if(aboutContent.isEmpty()){
            throw new RuntimeException("About content already exists");
        }

        AboutContent contentToDelete = aboutContent.get();

        try{
            aboutContentRepository.delete(contentToDelete);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
