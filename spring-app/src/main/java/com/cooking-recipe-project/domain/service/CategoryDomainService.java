package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.infrastructure.entity.CategoryEntity;
import com.cooking.recipe.project.infrastructure.repository.JpaCategoryRepository;
import com.cooking.recipe.project.infrastructure.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryDomainService {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    public Category createCategory(Long categoryId) {
        CategoryEntity entity = categoryRepository.findById(categoryId).orElse(null);
        if (entity == null) return null;

        return CategoryMapper.toDomain(entity);
    }
}
