package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.RecipeDto;
import com.cooking.recipe.project.domain.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GetAllRecipesUseCase {
    private final RecipeRepository recipeRepository;

    public GetAllRecipesUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeDto> execute() {
        // TODO: [UNCOMMENT LATER] Waiting for Repository Implementation
        /*
        return recipeRepository.findAll().stream()
                .map(RecipeDto::new)
                .collect(Collectors.toList());
        */

        // Επιστρέφουμε κενή λίστα προσωρινά για να δουλέψει το Frontend
        return Collections.emptyList();
    }
}