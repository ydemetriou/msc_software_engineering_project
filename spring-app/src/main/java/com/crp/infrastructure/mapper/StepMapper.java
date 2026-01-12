package com.crp.infrastructure.mapper;

import com.crp.domain.model.Ingredient;
import com.crp.domain.model.Step;
import com.crp.infrastructure.entity.IngredientEntity;
import com.crp.infrastructure.entity.StepEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StepMapper {

    public static StepEntity toEntity(Step step) {
        if (step == null) return null;
        StepEntity entity;
        if (step.getId() != null) {
            entity = new StepEntity(step.getId(), step.getTitle(), step.getDescription(), step.getDuration());
        } else {
            entity = new StepEntity(step.getTitle(), step.getDescription(), step.getDuration());
        }
        // Do not map domain order; persistence order is managed by RecipeEntity @OrderColumn
        if (step.getIngredients() != null) {
            List<IngredientEntity> ies = step.getIngredients().stream()
                    .map(IngredientMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setIngredients(ies);
            ies.forEach(ie -> {
                ie.setStep(entity);
                if (entity.getRecipe() != null) {
                    ie.setRecipe(entity.getRecipe());
                }
            });
        }
        return entity;
    }

    public static Step toDomain(StepEntity entity) {
        if (entity == null) return null;
        Step step = new Step(entity.getTitle(), entity.getDescription(), entity.getDuration());
        step.setId(entity.getId());
        // Do not set order on domain; ordering is by list position in Recipe.steps
        if (entity.getIngredients() != null) {
            List<Ingredient> ings = entity.getIngredients().stream()
                    .map(IngredientMapper::toDomain)
                    .collect(Collectors.toList());
            step.setIngredients(ings);
        } else {
            step.setIngredients(new ArrayList<>());
        }
        return step;
    }
}
