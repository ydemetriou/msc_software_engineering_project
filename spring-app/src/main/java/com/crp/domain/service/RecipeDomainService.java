package com.crp.domain.service;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.crp.domain.model.Recipe;
import com.crp.domain.model.Category;
import com.crp.domain.model.Photo;
import com.crp.domain.model.Step;
import com.crp.domain.model.Ingredient;
import com.crp.infrastructure.entity.RecipeEntity;
import com.crp.infrastructure.repository.JpaRecipeRepository;
import com.crp.infrastructure.mapper.RecipeMapper;
import com.crp.domain.model.enums.Difficulty;

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
    public Recipe create(String name, Category category, Difficulty difficulty, int totalTime,
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
    public List<Recipe> findAll() {
        return recipeRepository.findAll().stream()
                .map(RecipeMapper::toDomain)
                .collect(Collectors.toList());
    }
}
