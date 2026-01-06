package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Step;

public class StepDomainService {

    public Step createStep(String title, String description, Long duration) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Step title must not be empty");
        }
        if (duration != null && duration < 0) {
            throw new IllegalArgumentException("Step duration must be non-negative");
        }
        return new Step(title.trim(), description, duration);
    }
}
