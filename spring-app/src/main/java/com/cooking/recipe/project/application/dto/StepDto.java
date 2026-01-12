package com.cooking.recipe.project.application.dto;

import java.util.List;

public class StepDto {
    private String title;
    private String description;
    private Long duration;
    private List<String> photoUrls;         // ΝΕΟ: Φωτογραφίες βήματος
    private List<IngredientDto> ingredients; // ΝΕΟ: Υλικά βήματος

    public StepDto() {}

    // Getters & Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getDuration() { return duration; }
    public void setDuration(Long duration) { this.duration = duration; }
    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }
    public List<IngredientDto> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDto> ingredients) { this.ingredients = ingredients; }
}