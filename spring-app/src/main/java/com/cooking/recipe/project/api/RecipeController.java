package com.cooking.recipe.project.api;

import com.cooking.recipe.project.application.dto.CreateRecipeCommand;
import com.cooking.recipe.project.application.usecase.CreateRecipeUseCase;
import com.cooking.recipe.project.application.usecase.ExecuteRecipeUseCase;
import com.cooking.recipe.project.entity.Recipe;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*") // Για να μιλάει με το Frontend
public class RecipeController {

    private final CreateRecipeUseCase createRecipeUseCase;
    private final ExecuteRecipeUseCase executeRecipeUseCase;

    // Ο Controller πλέον μιλάει ΜΟΝΟ με τα Use Cases, όχι με το Repository
    public RecipeController(CreateRecipeUseCase createRecipeUseCase,
                            ExecuteRecipeUseCase executeRecipeUseCase) {
        this.createRecipeUseCase = createRecipeUseCase;
        this.executeRecipeUseCase = executeRecipeUseCase;
    }

    @PostMapping
    public void createRecipe(@RequestBody CreateRecipeCommand command) {
        createRecipeUseCase.execute(command);
    }

    @GetMapping("/{id}/execute")
    public Recipe executeRecipe(@PathVariable Long id) {
        return executeRecipeUseCase.getRecipeForExecution(id);
    }

    // Εδώ μπορείς να προσθέσεις endpoint για την πρόοδο αν χρειαστεί
}