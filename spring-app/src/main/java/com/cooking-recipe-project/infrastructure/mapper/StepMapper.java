package com.cooking.recipe.project.infrastructure.mapper;

import com.cooking.recipe.project.domain.model.Step;
import com.cooking.recipe.project.infrastructure.entity.StepEntity;

public class StepMapper {

    public static StepEntity toEntity(Step step) {
        if (step == null) return null;
        if (step.getId() != null) {
            return new StepEntity(step.getId(), step.getTitle(), step.getDescription(), step.getDuration());
        }
        return new StepEntity(step.getTitle(), step.getDescription(), step.getDuration());
    }

    public static Step toDomain(StepEntity entity) {
        if (entity == null) return null;
        Step step = new Step(entity.getTitle(), entity.getDescription(), entity.getDuration());
        step.setId(entity.getId());
        return step;
    }
}
