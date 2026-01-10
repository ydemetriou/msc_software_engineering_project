package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.infrastructure.entity.PhotoEntity;
import com.cooking.recipe.project.infrastructure.mapper.PhotoMapper;
import com.cooking.recipe.project.infrastructure.repository.JpaPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoDomainService {

    @Autowired
    private JpaPhotoRepository photoRepository;

    public Photo createPhotoFromDB(Long photoId) {
        PhotoEntity entity = photoRepository.findById(photoId).orElse(null);
        if (entity == null) return null;

        return PhotoMapper.toDomain(entity);
    }

    public Photo create(String url,Long recipeId, Long stepId) {
        Photo photo = new Photo();
        photo.setUrl(url);
        photo.setRecipeId(recipeId);
        photo.setStepId(stepId);
        return photo;
    }

    public Photo save(Photo photo) {
        PhotoEntity saved = photoRepository.save(PhotoMapper.toEntity(photo));
        return PhotoMapper.toDomain(saved);
    }

    public Photo update(Photo photo) {
        if (photo == null || photo.getId() == null) return null;
        PhotoEntity saved = photoRepository.save(PhotoMapper.toEntity(photo));
        return PhotoMapper.toDomain(saved);
    }

    public void delete(Long id) {
        if (id != null) photoRepository.deleteById(id);
    }
}
