package com.cooking.recipe.project.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "steps")
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private int duration; // in minutes
    private int stepOrder;

    // Existing: Step Photos
    @ElementCollection
    private List<String> stepPhotos = new ArrayList<>();

    // --- NEW: Step Ingredients (Requirement 6) ---
    @ElementCollection
    private List<String> stepIngredients = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    public Step() {}

    public Step(String title, String description, int duration, int stepOrder) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.stepOrder = stepOrder;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getStepOrder() { return stepOrder; }
    public void setStepOrder(int stepOrder) { this.stepOrder = stepOrder; }

    public List<String> getStepPhotos() { return stepPhotos; }
    public void setStepPhotos(List<String> stepPhotos) { this.stepPhotos = stepPhotos; }

    // --- Getters/Setters for StepIngredients ---
    public List<String> getStepIngredients() { return stepIngredients; }
    public void setStepIngredients(List<String> stepIngredients) { this.stepIngredients = stepIngredients; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}