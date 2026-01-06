package com.cooking.recipe.project.infrastructure.mapper;

import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.infrastructure.entity.PhotoEntity;

public class PhotoMapper {

    public static PhotoEntity toEntity(Photo photo) {
        if (photo == null) return null;
        if (photo.getId() != null) {
            return new PhotoEntity(photo.getId(), photo.getUrl());
        }
        return new PhotoEntity(photo.getUrl());
    }

    public static Photo toDomain(PhotoEntity entity) {
        if (entity == null) return null;
        Photo photo = new Photo(entity.getUrl());
        photo.setId(entity.getId());
        return photo;
    }
}
