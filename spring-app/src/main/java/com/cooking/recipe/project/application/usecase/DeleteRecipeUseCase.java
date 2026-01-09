package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.domain.repository.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteRecipeUseCase {
    private final RecipeRepository recipeRepository;

    public DeleteRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void execute(Long id) {
// TODO: [UNCOMMENT LATER] Waiting for Repository
// recipeRepository.deleteById(id);
    }
}