package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.CreateRecipeCommand;
import com.cooking.recipe.project.entity.Recipe; // Προσωρινά κάνουμε import από το παλιό entity
import com.cooking.recipe.project.repository.RecipeRepository; // Προσωρινά από το παλιό repository
import org.springframework.stereotype.Service; // Ή @Component

@Service // Το δηλώνουμε ως Service/Component για να το βλέπει το Spring
public class CreateRecipeUseCase {

    private final RecipeRepository recipeRepository;

    // Dependency Injection μέσω του Constructor
    public CreateRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void execute(CreateRecipeCommand command) {
        // 1. Μετατροπή του Command (DTO) σε Domain Entity
        Recipe recipe = new Recipe();
        recipe.setName(command.getName());
        recipe.setCategory(command.getCategory());
        recipe.setDifficulty(command.getDifficulty());
        recipe.setTotalTime(command.getTotalTime());

        // 2. Τυχόν business logic (π.χ. validate αν ο χρόνος είναι θετικός)
        if (recipe.getTotalTime() < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }

        // 3. Αποθήκευση μέσω του Repository
        recipeRepository.save(recipe);
    }
}