package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.RecipeDto;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.repository.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class GetRecipeUseCase {
    private final RecipeRepository recipeRepository;

    public GetRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeDto execute(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        // Ο Constructor του RecipeDto κάνει όλη τη δύσκολη δουλειά του mapping
        return new RecipeDto(recipe);
    }
}