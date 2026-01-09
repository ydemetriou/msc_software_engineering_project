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
        Category category = CategoryMapper.toDomain(entity.getCategory());
        String difficulty = entity.getDifficulty();
        int totalTime = entity.getTotalTime();

        List<Photo> photos = new ArrayList<>();
        if (entity.getPhotos() != null) {
            photos = entity.getPhotos().stream()
                    .map(PhotoMapper::toDomain)
                    .collect(Collectors.toList());
        }

        // Ingredients mapping is omitted because IngredientEntity is not present in the project
        List<Ingredient> ingredients = new ArrayList<>();

        List<Step> steps = new ArrayList<>();
        if (entity.getSteps() != null) {
            steps = entity.getSteps().stream()
                    .map(StepMapper::toDomain)
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
}
