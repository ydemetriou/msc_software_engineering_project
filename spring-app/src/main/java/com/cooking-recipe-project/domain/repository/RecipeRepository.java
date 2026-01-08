package com.cooking.recipe.project.domain.repository;

import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.domain.model.Step;
import java.util.List;

public interface RecipeRepository {
    Recipe findById(Long id);
    List<Recipe> findAll();
    List<Recipe> findByCategory(Long categoryId);
    List<Recipe> searchByName(String name);
    Recipe save(Recipe recipe);
    void delete(Long id);
    boolean exists(Long id);
}
