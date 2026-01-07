package com.cooking.recipe.project.domain.model;

import java.util.ArrayList;
import java.util.List;
import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.model.Step;

public class Recipe {
    private Long id;

    private String name;

    private String category;

    private String difficulty;

    private int totalTime;

    private List<String> photos;

    private List<Ingredient> ingredients;

    private List<Step> steps;

    public Recipe(String name, String category, String difficulty, int totalTime) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.photos = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();
    }

    public void addIngredient(Ingredient ing) {
        ingredients.add(ing);
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public void removeIngredient(Ingredient ing) {
        ingredients.remove(ing);
    }

    public void remove(Step step) { steps.remove(step);    }

    public Long getTotalDuration() {
        return steps.stream().mapToLong(Step::getDuration).sum();
    }

    public double calculateProgress(int currentStepTime) {
        Long totalDuration = getTotalDuration();
        return totalDuration == 0 ? 0 : (double) currentStepTime / totalDuration;
    }

    // Getters and setters (προαιρετικά)

}
