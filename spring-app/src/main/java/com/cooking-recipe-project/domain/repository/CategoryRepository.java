package com.cooking.recipe.project.domain.repository;

import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.domain.model.Step;
import java.util.List;

public interface CategoryRepository {
    Category findById(Long id);
    List<Category> findAll();
    Category findByName(String name);
    Category save(Category category);
    void delete(Long id);
}
