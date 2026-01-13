package com.crp.infrastructure.mapper;

import com.crp.domain.model.Category;
import com.crp.infrastructure.entity.CategoryEntity;

public class CategoryMapper {

    public static CategoryEntity toEntity(Category category) {
        if (category == null) return null;
        if (category.getId() != null) {
            return new CategoryEntity(category.getId(), category.getName());
        }
        return new CategoryEntity(category.getName());
    }

    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return new Category(entity.getId(), entity.getName());
    }
}
