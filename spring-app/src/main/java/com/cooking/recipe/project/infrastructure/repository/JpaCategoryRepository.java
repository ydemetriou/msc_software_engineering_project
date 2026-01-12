package com.cooking.recipe.project.infrastructure.repository;

import com.cooking.recipe.project.domain.repository.CategoryRepository;
import com.cooking.recipe.project.infrastructure.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long>, CategoryRepository {
    Optional<CategoryEntity> findByName(String name);}
