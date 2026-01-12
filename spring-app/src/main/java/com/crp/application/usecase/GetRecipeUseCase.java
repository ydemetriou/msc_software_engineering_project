package com.crp.application.usecase;

import com.crp.application.dto.RecipeDto;
import com.crp.domain.model.Recipe;
import com.crp.domain.service.RecipeDomainService; // <--- ΝΕΟ IMPORT
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