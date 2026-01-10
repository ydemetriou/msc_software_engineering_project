package com.cooking.recipe.project.domain.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.domain.model.Step;
import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.infrastructure.entity.RecipeEntity;
import com.cooking.recipe.project.infrastructure.repository.JpaRecipeRepository;
import com.cooking.recipe.project.infrastructure.mapper.RecipeMapper;

import java.util.List;

@Service
public class RecipeDomainService {

    @Autowired
    private JpaRecipeRepository recipeRepository;

    public Recipe createRecipeFromDB(Long recipeId) {
        RecipeEntity entity = recipeRepository.findById(recipeId).orElse(null);
        if (entity == null) return null;

        return RecipeMapper.toDomain(entity);
    }

    // --- New API ---
    public Recipe create(String name, Category category, String difficulty, int totalTime,
                         List<Photo> photos, List<Ingredient> ingredients, List<Step> steps) {
        return new Recipe(name, category, difficulty, totalTime, photos, ingredients, steps);
    }

    public Recipe save(Recipe recipe) {
        RecipeEntity saved = recipeRepository.save(RecipeMapper.toEntity(recipe));
        return RecipeMapper.toDomain(saved);
    }

    public Recipe update(Recipe recipe) {
        if (recipe == null || recipe.getId() == null) return null;
        RecipeEntity saved = recipeRepository.save(RecipeMapper.toEntity(recipe));
        return RecipeMapper.toDomain(saved);
    }

    public void delete(Long id) {
        if (id != null) recipeRepository.deleteById(id);
    }
}
