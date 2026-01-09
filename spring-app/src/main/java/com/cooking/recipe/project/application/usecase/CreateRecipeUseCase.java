package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.*;
import com.cooking.recipe.project.domain.model.*;
import com.cooking.recipe.project.domain.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateRecipeUseCase {
    private final RecipeRepository recipeRepository;

    public CreateRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void execute(CreateRecipeCommand command) {
        // Κατηγορία
        Category category = null;
        if (command.getCategory() != null) {
            category = new Category(command.getCategory());
        }

        // Φωτογραφίες Συνταγής
        List<Photo> photos = new ArrayList<>();
        if (command.getPhotoUrls() != null) {
            photos = command.getPhotoUrls().stream()
                    .map(Photo::new) // Χρήση constructor Photo(String url)
                    .collect(Collectors.toList());
        }

        // Υλικά
        List<Ingredient> ingredients = mapIngredients(command.getIngredients());

        // Βήματα
        List<Step> steps = new ArrayList<>();
        if (command.getSteps() != null) {
            for (StepDto sDto : command.getSteps()) {
                Step step = new Step(sDto.getTitle(), sDto.getDescription(), sDto.getDuration());

                // Φωτογραφίες Βήματος
                if (sDto.getPhotoUrls() != null) {
                    List<Photo> stepPhotos = sDto.getPhotoUrls().stream()
                            .map(Photo::new)
                            .collect(Collectors.toList());
                    step.setPhotos(stepPhotos);
                }

                // Υλικά Βήματος (Αν υποστηρίζεται από το Domain)
                if (sDto.getIngredients() != null) {
                    step.setIngredients(mapIngredients(sDto.getIngredients()));
                }

                steps.add(step);
            }
        }

        Recipe recipe = new Recipe(
                command.getName(),
                category,
                command.getDifficulty(),
                command.getTotalTime(),
                photos,
                ingredients,
                steps
        );
// TODO: [UNCOMMENT LATER] Waiting for Repository

//       recipeRepository.save(recipe);
    }

    // Helper μέθοδος για να μην γράφουμε τα ίδια
    private List<Ingredient> mapIngredients(List<IngredientDto> dtos) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream().map(dto -> {
            Ingredient ing = new Ingredient();
            ing.setName(dto.getName());
            ing.setQuantity(dto.getQuantity());
            ing.setUnit(dto.getUnit());
            return ing;
        }).collect(Collectors.toList());
    }
}