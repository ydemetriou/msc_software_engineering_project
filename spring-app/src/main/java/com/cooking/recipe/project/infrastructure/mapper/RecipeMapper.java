package com.cooking.recipe.project.infrastructure.mapper;

import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.model.Step;
import com.cooking.recipe.project.infrastructure.entity.PhotoEntity;
import com.cooking.recipe.project.infrastructure.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeMapper {

    public static Recipe toDomain(RecipeEntity entity) {
        if (entity == null) return null;

        String name = entity.getName();
        Category category = com.cooking.recipe.project.infrastructure.mapper.CategoryMapper.toDomain(entity.getCategory());
        String difficulty = entity.getDifficulty();
        int totalTime = entity.getTotalTime();

        List<Photo> photos = new ArrayList<>();
        if (entity.getPhotos() != null) {
            photos = entity.getPhotos().stream()
                    .map(com.cooking.recipe.project.infrastructure.mapper.PhotoMapper::toDomain)
                    .collect(Collectors.toList());
        }

        List<Ingredient> ingredients = new ArrayList<>();
        if (entity.getIngredients() != null) {
            ingredients = entity.getIngredients().stream()
                    .map(com.cooking.recipe.project.infrastructure.mapper.IngredientMapper::toDomain)
                    .collect(Collectors.toList());
        }

        List<Step> steps = new ArrayList<>();
        if (entity.getSteps() != null) {
            steps = entity.getSteps().stream()
                    .map(com.cooking.recipe.project.infrastructure.mapper.StepMapper::toDomain)
                    .collect(Collectors.toList());
        }

        return new Recipe(
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
        entity.setCategory(com.cooking.recipe.project.infrastructure.mapper.CategoryMapper.toEntity(recipe.getCategory()));
        entity.setDifficulty(recipe.getDifficulty());
        entity.setTotalTime(recipe.getTotalTime());

        // Photos
        if (recipe.getPhotos() != null) {
            List<PhotoEntity> photoEntities = recipe.getPhotos().stream()
                    .map(com.cooking.recipe.project.infrastructure.mapper.PhotoMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setPhotos(photoEntities);
            if (photoEntities != null) {
                photoEntities.forEach(pe -> pe.setRecipe(entity));
            }
        }

        // Ingredients
        if (recipe.getIngredients() != null) {
            List<com.cooking.recipe.project.infrastructure.entity.IngredientEntity> ingredientEntities = recipe.getIngredients().stream()
                    .map(com.cooking.recipe.project.infrastructure.mapper.IngredientMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setIngredients(ingredientEntities);
            if (ingredientEntities != null) {
                ingredientEntities.forEach(ie -> ie.setRecipe(entity));
            }
        }

        // Steps
        if (recipe.getSteps() != null) {
            List<com.cooking.recipe.project.infrastructure.entity.StepEntity> stepEntities = recipe.getSteps().stream()
                    .map(com.cooking.recipe.project.infrastructure.mapper.StepMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setSteps(stepEntities);
            if (stepEntities != null) {
                stepEntities.forEach(se -> se.setRecipe(entity));
            }
        }

        return entity;
    }
}
