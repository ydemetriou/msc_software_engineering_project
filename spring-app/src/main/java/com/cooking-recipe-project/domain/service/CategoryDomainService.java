package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Category;

public class CategoryDomainService {

    public Category createCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be empty");
        }
        return new Category(name.trim());
    }
}
