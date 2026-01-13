package com.crp.infrastructure.mapper;

import com.crp.domain.model.Category;
import com.crp.domain.model.Ingredient;
import com.crp.domain.model.Photo;
import com.crp.domain.model.Recipe;
import com.crp.domain.model.Step;
import com.crp.domain.model.enums.Difficulty;
import com.crp.infrastructure.entity.PhotoEntity;
import com.crp.infrastructure.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeMapper {

    public static Recipe toDomain(RecipeEntity entity) {
        if (entity == null) return null;

        String name = entity.getName();
        Category category = CategoryMapper.toDomain(entity.getCategory());
        Difficulty difficulty = null;
        String diffStr = entity.getDifficulty();
        if (diffStr != null) {
            try {
                difficulty = Difficulty.valueOf(diffStr);
            } catch (IllegalArgumentException ex) {
                difficulty = null;
            }
        }
        int totalTime = entity.getTotalTime();

        List<Photo> photos = new ArrayList<>();
        if (entity.getPhotos() != null) {
            photos = entity.getPhotos().stream()
                    .map(PhotoMapper::toDomain)
                    .collect(Collectors.toList());
        }

        List<Ingredient> ingredients = new ArrayList<>();
        if (entity.getIngredients() != null) {
            ingredients = entity.getIngredients().stream()
                    .map(IngredientMapper::toDomain)
                    .collect(Collectors.toList());
        }

        List<Step> steps = new ArrayList<>();
        if (entity.getSteps() != null) {
            steps = entity.getSteps().stream()
                    .map(StepMapper::toDomain)
                    .collect(Collectors.toList());
        }

        return new Recipe(
                entity.getId(),
                name,
                category,
                difficulty,
                totalTime,
                photos,
                ingredients,
                steps
        );
    }

    public static RecipeEntity toEntity(Recipe recipe) {
        if (recipe == null) return null;
        RecipeEntity entity = new RecipeEntity();
        if (recipe.getId() != null) {
            entity.setId(recipe.getId());
        }
        entity.setName(recipe.getName());
        entity.setCategory(CategoryMapper.toEntity(recipe.getCategory()));
        entity.setDifficulty(recipe.getDifficulty() != null ? recipe.getDifficulty().name() : null);
        entity.setTotalTime(recipe.getTotalTime());

        // Photos
        if (recipe.getPhotos() != null) {
            List<PhotoEntity> photoEntities = recipe.getPhotos().stream()
                    .map(PhotoMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setPhotos(photoEntities);
            if (photoEntities != null) {
                photoEntities.forEach(pe -> pe.setRecipe(entity));
            }
        }

        // Ingredients
        if (recipe.getIngredients() != null) {
            List<com.crp.infrastructure.entity.IngredientEntity> ingredientEntities = recipe.getIngredients().stream()
                    .map(IngredientMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setIngredients(ingredientEntities);
            if (ingredientEntities != null) {
                ingredientEntities.forEach(ie -> ie.setRecipe(entity));
            }
        }

        // Steps
        if (recipe.getSteps() != null) {
            List<com.crp.infrastructure.entity.StepEntity> stepEntities = recipe.getSteps().stream()
                    .map(StepMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setSteps(stepEntities);
            if (stepEntities != null) {
                stepEntities.forEach(se -> se.setRecipe(entity));
            }
        }

        return entity;
    }
}
