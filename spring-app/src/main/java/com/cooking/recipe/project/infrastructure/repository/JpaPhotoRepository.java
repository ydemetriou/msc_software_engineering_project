package com.cooking.recipe.project.infrastructure.repository;

import com.cooking.recipe.project.domain.repository.PhotoRepository;
import com.cooking.recipe.project.infrastructure.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPhotoRepository extends JpaRepository<PhotoEntity, Long>, PhotoRepository {
}
