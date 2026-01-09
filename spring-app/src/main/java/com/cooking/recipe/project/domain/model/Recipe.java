package com.cooking.recipe.project.domain.model;

import java.util.List;

public class Recipe {
    private Long id;

    private String name;

    private Category category;

    private String difficulty;

    private int totalTime;

    private List<Photo> photos;

    private List<Ingredient> ingredients;

    private List<Step> steps;

    public Recipe(
            String name,
            Category category,
            String difficulty,
            int totalTime,
            List<Photo> photos,
            List<Ingredient> ingredients,
            List<Step> steps)
    {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.photos = photos;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public void addIngredient(Ingredient ing) {
        ingredients.add(ing);
    }
    public void removeIngredient(Ingredient ing) {
        ingredients.remove(ing);
    }

    public void addStep(Step step) {
        steps.add(step);
    }
    public void removeStep(Step step) { steps.remove(step);    }

    public void addPhoto(Photo photo) {photos.add(photo);    }
    public void removePhoto(Photo photo) { photos.remove(photo); }

    public Long getTotalDuration() {
        return steps.stream().mapToLong(Step::getDuration).sum();
    }

    public double calculateProgress(int currentStepTime) {
        Long totalDuration = getTotalDuration();
        return totalDuration == 0 ? 0 : (double) currentStepTime / totalDuration;
    }

    // Getters and setters (προαιρετικά)

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;    }

    public String getName() {return name;    }
    public void setName(String name) {this.name = name;}

    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category = category;}

    public String getDifficulty() {return difficulty;}
    public void setDifficulty(String difficulty) {this.difficulty = difficulty;}

    public int getTotalTime() {return totalTime;}
    public void setTotalTime(int totalTime) {this.totalTime = totalTime;}

    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }
}
