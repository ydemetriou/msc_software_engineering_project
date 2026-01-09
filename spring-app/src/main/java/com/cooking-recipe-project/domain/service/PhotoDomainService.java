package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.infrastructure.entity.PhotoEntity;
import com.cooking.recipe.project.infrastructure.repository.JpaPhotoRepository;
import com.cooking.recipe.project.infrastructure.mapper.PhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoDomainService {

    @Autowired
    private JpaPhotoRepository photoRepository;

    public Photo createPhoto(Long photoId) {
        PhotoEntity entity = photoRepository.findById(photoId).orElse(null);
        if (entity == null) return null;

        return PhotoMapper.toDomain(entity);
    }
}
