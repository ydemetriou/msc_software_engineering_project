package com.cooking.recipe.project.infrastructure.mapper;

import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.infrastructure.entity.IngredientEntity;

public class IngredientMapper {

    public static IngredientEntity toEntity(Ingredient ingredient) {
        if (ingredient == null) return null;
        IngredientEntity entity = new IngredientEntity();
        if (ingredient.getId() != null) {
            entity.setId(ingredient.getId());
        }
        entity.setName(ingredient.getName());
        entity.setQuantity(ingredient.getQuantity());
        entity.setUnit(ingredient.getUnit());
        return entity;
    }

    public static Ingredient toDomain(IngredientEntity entity) {
        if (entity == null) return null;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(entity.getId());
        ingredient.setName(entity.getName());
        if (entity.getQuantity() != null) {
            ingredient.setQuantity(entity.getQuantity());
        }
        ingredient.setUnit(entity.getUnit());
        return ingredient;
    }
}
