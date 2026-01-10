package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.RecipeDto;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.service.RecipeDomainService; // <--- ΝΕΟ IMPORT
import org.springframework.stereotype.Service;

@Service
public class GetRecipeUseCase {
    private final RecipeDomainService recipeDomainService;

    public GetRecipeUseCase(RecipeDomainService recipeDomainService) {
        this.recipeDomainService = recipeDomainService;
    }

    public RecipeDto execute(Long id) {
        // Χρήση της μεθόδου του service που φέρνει από τη βάση
        Recipe recipe = recipeDomainService.createRecipeFromDB(id);

        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }

        return new RecipeDto(recipe);
    }
}