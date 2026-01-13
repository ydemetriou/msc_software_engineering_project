package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.entity.Recipe;
import com.cooking.recipe.project.repository.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class ExecuteRecipeUseCase {

    private final RecipeRepository recipeRepository;

    public ExecuteRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe getRecipeForExecution(Long recipeId) { // Άλλαξα το int σε Long για να ταιριάζει με τη βάση
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public double calculateCurrentProgress(Long recipeId, int completedStepTime) {
        Recipe recipe = getRecipeForExecution(recipeId);
        // Καλουμε τη μέθοδο του Domain Model (όπως στο διάγραμμα)
        return recipe.calculateProgress(completedStepTime);
    }
}