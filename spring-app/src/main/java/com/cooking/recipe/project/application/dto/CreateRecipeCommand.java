package com.cooking.recipe.project.application.dto;

import java.util.List;

// Αυτή η κλάση περιέχει ΟΛΑ τα δεδομένα που στέλνει ο χρήστης για να φτιάξει συνταγή
public class CreateRecipeCommand {
    private String name;
    private String category;
    private String difficulty;
    private int totalTime;
    // Εδώ μπορείς να βάλεις και λίστες για Ingredients/Steps αν θες να τα στέλνεις όλα μαζί
    // private List<IngredientDto> ingredients;

    // Constructors, Getters, Setters
    public CreateRecipeCommand() {}

    public CreateRecipeCommand(String name, String category, String difficulty, int totalTime) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getTotalTime() { return totalTime; }
    public void setTotalTime(int totalTime) { this.totalTime = totalTime; }
}