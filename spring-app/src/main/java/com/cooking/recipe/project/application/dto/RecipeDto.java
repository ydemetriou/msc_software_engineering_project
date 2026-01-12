package com.cooking.recipe.project.application.dto;

import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.model.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeDto {
    private Long id;
    private String name;
    private String category;
    private String difficulty; // String για το Frontend
    private int totalTime;
    private List<String> photoUrls;
    private List<IngredientDto> ingredients;
    private List<StepDto> steps;

    public RecipeDto() {
    }

    public RecipeDto(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.totalTime = recipe.getTotalTime();

        // 1. ΔΙΟΡΘΩΣΗ: Μετατροπή Enum Difficulty σε String
        if (recipe.getDifficulty() != null) {
            this.difficulty = recipe.getDifficulty().name();
        }

        // Mapping Category
        if (recipe.getCategory() != null) {
            this.category = recipe.getCategory().getName();
        }

        // Mapping Photos
        if (recipe.getPhotos() != null) {
            this.photoUrls = recipe.getPhotos().stream()
                    .map(Photo::getUrl)
                    .collect(Collectors.toList());
        }

        // 2. ΔΙΟΡΘΩΣΗ: Mapping Ingredients (Enum Unit σε String)
        if (recipe.getIngredients() != null) {
            this.ingredients = recipe.getIngredients().stream()
                    .map(ing -> {
                        IngredientDto dto = new IngredientDto();
                        dto.setName(ing.getName());
                        dto.setQuantity(ing.getQuantity());

                        // ΕΔΩ Η ΑΛΛΑΓΗ ΓΙΑ ΤΟ UNIT
                        if (ing.getUnit() != null) {
                            dto.setUnit(ing.getUnit().name());
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        // Mapping Steps
        if (recipe.getSteps() != null) {
            this.steps = recipe.getSteps().stream()
                    .map(step -> {
                        StepDto sDto = new StepDto();
                        sDto.setTitle(step.getTitle());
                        sDto.setDescription(step.getDescription());
                        sDto.setDuration(step.getDuration());

                        // Step Photos
                        if (step.getPhotos() != null) {
                            sDto.setPhotoUrls(step.getPhotos().stream()
                                    .map(Photo::getUrl)
                                    .collect(Collectors.toList()));
                        }

                        // Step Ingredients
                        if (step.getIngredients() != null) {
                            sDto.setIngredients(step.getIngredients().stream()
                                    .map(ing -> {
                                        IngredientDto iDto = new IngredientDto();
                                        iDto.setName(ing.getName());
                                        iDto.setQuantity(ing.getQuantity());
                                        if (ing.getUnit() != null) {
                                            iDto.setUnit(ing.getUnit().name());
                                        }
                                        return iDto;
                                    }).collect(Collectors.toList()));
                        }

                        return sDto;
                    })
                    .collect(Collectors.toList());
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getTotalTime() { return totalTime; }
    public void setTotalTime(int totalTime) { this.totalTime = totalTime; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }

    public List<IngredientDto> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDto> ingredients) { this.ingredients = ingredients; }

    public List<StepDto> getSteps() { return steps; }
    public void setSteps(List<StepDto> steps) { this.steps = steps; }
}