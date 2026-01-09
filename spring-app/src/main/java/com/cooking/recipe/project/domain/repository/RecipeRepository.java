package com.cooking.recipe.project.domain.repository;

import com.cooking.recipe.project.domain.model.Recipe;
import java.util.Optional;
import java.util.List;

public interface RecipeRepository {
    Recipe save(Recipe recipe);
    Optional<Recipe> findById(Long id);
    List<Recipe> findAll();      // Για να βλέπεις όλες τις συνταγές
    void deleteById(Long id);
}
