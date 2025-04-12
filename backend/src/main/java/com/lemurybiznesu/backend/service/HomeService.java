package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.response.HomeDataResponse;
import com.lemurybiznesu.backend.model.dto.response.ImageResponse;
import com.lemurybiznesu.backend.model.entity.AboutContent;
import com.lemurybiznesu.backend.model.entity.ImageContent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {
    private final AboutContentService aboutContentService;
    private final ImageContentService imageContentService;

    public HomeService(AboutContentService aboutContentService, ImageContentService imageContentService) {
        this.aboutContentService = aboutContentService;
        this.imageContentService = imageContentService;
    }

    public HomeDataResponse getHomeData() {
        HomeDataResponse homeDataResponse = new HomeDataResponse();
        Optional<AboutContent> aboutContent = aboutContentService.getAboutContent();

        if (aboutContent.isPresent()) {
            homeDataResponse.setAboutContent(aboutContent.get().getText());
        }else{
            homeDataResponse.setAboutContent(null);

        }

        List<ImageResponse> imageResponses = new ArrayList<>();
        List<ImageContent> imageContents = imageContentService.getAllImages();

        for (ImageContent imageContent : imageContents) {
            imageResponses.add(ImageResponse.fromEntity(imageContent));
        }

        homeDataResponse.setImages(imageResponses);

        return homeDataResponse;
    }
}
