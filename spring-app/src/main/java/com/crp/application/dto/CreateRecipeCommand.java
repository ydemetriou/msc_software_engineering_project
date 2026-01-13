package com.crp.application.dto;

import java.util.List;

public class CreateRecipeCommand {
    private String name;
    private String category;    // ΝΕΟ: Κατηγορία (string)
    private String difficulty;
    private int totalTime;
    private List<String> photoUrls; // ΝΕΟ: Φωτογραφίες συνταγής
    private List<IngredientDto> ingredients;
    private List<StepDto> steps;

    public CreateRecipeCommand() {}

    // Getters & Setters
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