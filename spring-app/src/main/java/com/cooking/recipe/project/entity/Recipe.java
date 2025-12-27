package com.cooking.recipe.project.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Πηγή 10: Όνομα, κατηγορία, δυσκολία, χρόνος
    private String name;
    private String category;
    private String difficulty;
    private int totalTime; // σε λεπτά

    // Πηγή 10: Φωτογραφίες (Λίστα strings)
    @ElementCollection
    private List<String> photos = new ArrayList<>();

    // Σχέση με Ingredient (1 Recipe έχει πολλά Ingredients)
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    // Σχέση με Step (1 Recipe έχει πολλά Steps)
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepOrder ASC") // Πηγή 13: Τα βήματα είναι σε ακολουθία
    private List<Step> steps = new ArrayList<>();

    // Constructors, Getters, Setters
    public Recipe() {}

    public Recipe(String name, String category, String difficulty, int totalTime) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
    }

    // Helper μέθοδοι για να προσθέτουμε εύκολα (όπως στο Διάγραμμα Κλάσεων)
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    public void addStep(Step step) {
        steps.add(step);
        step.setRecipe(this);
    }

    // Getters and Setters...
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
    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }
}