package com.cooking.recipe.project.infrastructure.mapper;

import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.model.enums.Unit;
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
        entity.setUnit(ingredient.getUnit() != null ? ingredient.getUnit().name() : null);
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
        String unitStr = entity.getUnit();
        if (unitStr != null) {
            try {
                ingredient.setUnit(Unit.valueOf(unitStr));
            } catch (IllegalArgumentException ex) {
                ingredient.setUnit(null); // fallback if persisted string is unexpected
            }
        } else {
            ingredient.setUnit(null);
        }
        return ingredient;
    }
}
