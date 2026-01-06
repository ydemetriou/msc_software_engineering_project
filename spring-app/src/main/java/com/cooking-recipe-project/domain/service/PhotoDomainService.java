package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Photo;

public class PhotoDomainService {

    public Photo createPhoto(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Photo URL must not be empty");
        }
        return new Photo(url.trim());
    }
}
