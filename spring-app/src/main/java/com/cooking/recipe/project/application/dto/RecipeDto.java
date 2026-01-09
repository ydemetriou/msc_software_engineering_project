package com.cooking.recipe.project.application.dto;

import com.cooking.recipe.project.domain.model.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class RecipeDto {
    private Long id;
    private String name;
    private String category;
    private String difficulty;
    private int totalTime;
    private List<String> photoUrls;
    private List<IngredientDto> ingredients;
    private List<StepDto> steps;

    public RecipeDto(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.difficulty = recipe.getDifficulty();
        this.totalTime = recipe.getTotalTime();

        // Mapping Category
        if (recipe.getCategory() != null) {
            this.category = recipe.getCategory().getName();
        }

        // Mapping Photos
        this.photoUrls = new ArrayList<>();
        if (recipe.getPhotos() != null) {
            this.photoUrls = recipe.getPhotos().stream()
                    .map(Photo::getUrl)
                    .collect(Collectors.toList());
        }

        // Mapping Ingredients
        this.ingredients = new ArrayList<>();
        if (recipe.getIngredients() != null) {
            this.ingredients = recipe.getIngredients().stream()
                    .map(i -> new IngredientDto(i.getName(), i.getQuantity(), i.getUnit()))
                    .collect(Collectors.toList());
        }

        // Mapping Steps
        this.steps = new ArrayList<>();
        if (recipe.getSteps() != null) {
            this.steps = recipe.getSteps().stream().map(s -> {
                StepDto sDto = new StepDto();
                sDto.setTitle(s.getTitle());
                sDto.setDescription(s.getDescription());
                sDto.setDuration(s.getDuration());
                // Step Photos mapping (αν υπάρχουν)
                if (s.getPhotos() != null) {
                    sDto.setPhotoUrls(s.getPhotos().stream().map(Photo::getUrl).collect(Collectors.toList()));
                }
                return sDto;
            }).collect(Collectors.toList());
        }
    }

    // Getters & Setters για όλα τα πεδία (χρησιμοποίησε Alt+Insert -> Getter and Setter αν βαριέσαι να γράφεις)
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public int getTotalTime() { return totalTime; }
    public List<String> getPhotoUrls() { return photoUrls; }
    public List<IngredientDto> getIngredients() { return ingredients; }
    public List<StepDto> getSteps() { return steps; }
}