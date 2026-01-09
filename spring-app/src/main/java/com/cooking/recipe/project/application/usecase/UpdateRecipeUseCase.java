package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.*;
import com.cooking.recipe.project.domain.model.*;
import com.cooking.recipe.project.domain.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateRecipeUseCase {
    private final RecipeRepository recipeRepository;

    public UpdateRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void execute(Long id, CreateRecipeCommand command) {
// TODO: [UNCOMMENT LATER] Waiting for Repository
        //        Recipe recipe = recipeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Recipe not found"));
//
//        // Ενημέρωση βασικών πεδίων
//        recipe.setName(command.getName());
//        recipe.setDifficulty(command.getDifficulty());
//        recipe.setTotalTime(command.getTotalTime());
//
//        if (command.getCategory() != null) {
//            recipe.setCategory(new Category(command.getCategory()));
//        }
//
//        // Ενημέρωση Υλικών (Καθαρισμός και ξαναγέμισμα - Ασφαλής μέθοδος)
//        if (command.getIngredients() != null) {
//            recipe.setIngredients(mapIngredients(command.getIngredients()));
//        }
//
//        // Ενημέρωση Βημάτων
//        if (command.getSteps() != null) {
//            List<Step> newSteps = new ArrayList<>();
//            for (StepDto sDto : command.getSteps()) {
//                Step step = new Step(sDto.getTitle(), sDto.getDescription(), sDto.getDuration());
//                if (sDto.getPhotoUrls() != null) {
//                    step.setPhotos(sDto.getPhotoUrls().stream().map(Photo::new).collect(Collectors.toList()));
//                }
//                newSteps.add(step);
//            }
//            recipe.setSteps(newSteps);
//        }
//
//        // Ενημέρωση Φωτογραφιών
//        if (command.getPhotoUrls() != null) {
//            recipe.setPhotos(command.getPhotoUrls().stream().map(Photo::new).collect(Collectors.toList()));
//        }
//
//        recipeRepository.save(recipe);
    }

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