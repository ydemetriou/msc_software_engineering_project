package com.crp.infrastructure.mapper;

import com.crp.domain.model.Photo;
import com.crp.infrastructure.entity.PhotoEntity;

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
