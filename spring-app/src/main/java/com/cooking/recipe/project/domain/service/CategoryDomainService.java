package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.infrastructure.entity.CategoryEntity;
import com.cooking.recipe.project.infrastructure.mapper.CategoryMapper;
import com.cooking.recipe.project.infrastructure.repository.JpaCategoryRepository;
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

    public Category create(String name) {
        return new Category(null, name);
    }

    public Category save(Category category) {
        CategoryEntity toSave = CategoryMapper.toEntity(category);
        CategoryEntity saved = categoryRepository.save(toSave);
        return CategoryMapper.toDomain(saved);
    }

    public Category update(Category category) {
        if (category == null || category.getId() == null) return null;
        CategoryEntity updated = categoryRepository.save(CategoryMapper.toEntity(category));
        return CategoryMapper.toDomain(updated);
    }

    public void delete(Long id) {
        if (id != null) categoryRepository.deleteById(id);
    }
}
