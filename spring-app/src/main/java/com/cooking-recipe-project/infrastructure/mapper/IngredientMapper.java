package com.cooking.recipe.project.infrastructure.mapper;

import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.infrastructure.entity.IngredientEntity;

public class IngredientMapper {

    public static IngredientEntity toEntity(Ingredient ingredient) {
        if (ingredient == null) return null;
        if (ingredient.getId() != null) {
            return new IngredientEntity(ingredient.getId(), ingredient.getName());
        }
        return new IngredientEntity(ingredient.getName());
    }

    public static Ingredient toDomain(IngredientEntity entity) {
        if (entity == null) return null;
        return new Ingredient(entity.getId(), entity.getName());
    }
}
