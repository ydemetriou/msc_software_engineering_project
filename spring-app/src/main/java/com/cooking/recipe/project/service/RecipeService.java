package com.cooking.recipe.project.service;

import com.cooking.recipe.project.entity.Recipe;
import com.cooking.recipe.project.entity.Step;
import com.cooking.recipe.project.entity.Ingredient;
import com.cooking.recipe.project.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // --- ΛΕΙΤΟΥΡΓΙΕΣ CRUD (Π2.1) ---

    // 1. Καταχώρηση ή Ενημέρωση Συνταγής
    @Transactional
    public Recipe saveRecipe(Recipe recipe) {
        // Πρέπει να συνδέσουμε τα παιδιά (Steps, Ingredients) με τον γονέα (Recipe)


        if (recipe.getSteps() != null) {
            for (Step step : recipe.getSteps()) {
                step.setRecipe(recipe); // Σύνδεση Step -> Recipe
            }
        }

        if (recipe.getIngredients() != null) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.setRecipe(recipe); // Σύνδεση Ingredient -> Recipe
            }
        }

        return recipeRepository.save(recipe);
    }

    // 2. Ανάκτηση όλων των συνταγών
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // 3. Ανάκτηση μιας συνταγής με το ID (για την Εκτέλεση - Π2.2)
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
    }

    // 4. Διαγραφή Συνταγής (Π2.1)
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    // --- ΛΕΙΤΟΥΡΓΙΕΣ ΕΚΤΕΛΕΣΗΣ (Π2.2 & Π1.4) ---

    // Υπολογισμός προόδου (Progress Bar)
    // Λογική: (Άθροισμα χρόνου ολοκληρωμένων βημάτων / Συνολικός χρόνος) * 100
    public double calculateProgressPercentage(Long recipeId, int completedMinutes) {
        Recipe recipe = getRecipeById(recipeId);

        if (recipe.getTotalTime() == 0) return 0.0;

        double progress = ((double) completedMinutes / recipe.getTotalTime()) * 100.0;

        // Να μην ξεπερνάει το 100%
        return Math.min(progress, 100.0);
    }
}