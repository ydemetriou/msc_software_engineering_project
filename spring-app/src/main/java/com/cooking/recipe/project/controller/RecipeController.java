package com.cooking.recipe.project.controller;

import com.cooking.recipe.project.entity.Recipe;
import com.cooking.recipe.project.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes") // Όλα τα URLs θα ξεκινάνε με /api/recipes
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // 1. Λήψη όλων των συνταγών (GET /api/recipes)
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    // 2. Δημιουργία νέας συνταγής (POST /api/recipes)
    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    // 3. Διαγραφή συνταγής (DELETE /api/recipes/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok().build();
    }

    // 4. Υπολογισμός προόδου (GET /api/recipes/{id}/progress?completedMinutes=10)
    @GetMapping("/{id}/progress")
    public ResponseEntity<Double> getProgress(@PathVariable Long id, @RequestParam int completedMinutes) {
        double progress = recipeService.calculateProgressPercentage(id, completedMinutes);
        return ResponseEntity.ok(progress);
    }

    // 5. Λήψη ΜΙΑΣ συνταγής με βάση το ID (GET /api/recipes/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Ενημέρωση συνταγής (PUT /api/recipes/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipeDetails) {
        try {
            Recipe existingRecipe = recipeService.getRecipeById(id);

            // Ενημέρωση των πεδίων
            existingRecipe.setName(recipeDetails.getName());
            existingRecipe.setCategory(recipeDetails.getCategory());
            existingRecipe.setDifficulty(recipeDetails.getDifficulty());
            existingRecipe.setTotalTime(recipeDetails.getTotalTime());

            // Προσοχή: Για τα λίστες (Ingredients/Steps) χρειάζεται ειδική λογική,
            // αλλά για τώρα ας κάνουμε save το existing που θα κάνει update τα βασικά.
            // Στην πράξη θα αντικαθιστούμε και τις λίστες.

            Recipe updatedRecipe = recipeService.saveRecipe(existingRecipe);
            return ResponseEntity.ok(updatedRecipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}