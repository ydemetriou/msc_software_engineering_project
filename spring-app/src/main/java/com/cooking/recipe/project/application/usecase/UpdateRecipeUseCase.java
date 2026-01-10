package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.*;
import com.cooking.recipe.project.domain.model.*;
import com.cooking.recipe.project.domain.model.enums.Difficulty;
import com.cooking.recipe.project.domain.model.enums.Unit;
import com.cooking.recipe.project.domain.service.CategoryDomainService;
import com.cooking.recipe.project.domain.service.RecipeDomainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateRecipeUseCase {
    private final RecipeDomainService recipeDomainService;
    private final CategoryDomainService categoryDomainService;

    public UpdateRecipeUseCase(RecipeDomainService recipeDomainService,
                               CategoryDomainService categoryDomainService) {
        this.recipeDomainService = recipeDomainService;
        this.categoryDomainService = categoryDomainService;
    }

    public void execute(Long id, CreateRecipeCommand command) {
        Recipe recipe = recipeDomainService.createRecipeFromDB(id);
        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }

        // 1. Ενημέρωση βασικών πεδίων
        recipe.setName(command.getName());
        recipe.setTotalTime(command.getTotalTime());

        if (command.getDifficulty() != null) {
            try {
                recipe.setDifficulty(Difficulty.valueOf(command.getDifficulty().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid difficulty: " + command.getDifficulty());
            }
        }

        // 2. Ενημέρωση Κατηγορίας
        if (command.getCategory() != null) {
            Category category = categoryDomainService.findByName(command.getCategory());
            if (category != null) {
                recipe.setCategory(category);
            }
        }

        // 3. Ενημέρωση Υλικών
        if (command.getIngredients() != null) {
            recipe.setIngredients(mapIngredients(command.getIngredients()));
        }

        // 4. Ενημέρωση Βημάτων
        if (command.getSteps() != null) {
            List<Step> newSteps = new ArrayList<>();
            for (StepDto sDto : command.getSteps()) {
                Step step = new Step(sDto.getTitle(), sDto.getDescription(), sDto.getDuration());
                if (sDto.getPhotoUrls() != null) {
                    step.setPhotos(sDto.getPhotoUrls().stream().map(Photo::new).collect(Collectors.toList()));
                }
                if (sDto.getIngredients() != null) {
                    step.setIngredients(mapIngredients(sDto.getIngredients()));
                }
                newSteps.add(step);
            }
            recipe.setSteps(newSteps);
        }

        // 5. Ενημέρωση Φωτογραφιών
        if (command.getPhotoUrls() != null) {
            recipe.setPhotos(command.getPhotoUrls().stream().map(Photo::new).collect(Collectors.toList()));
        }

        // --- VALIDATION ΕΛΕΓΧΟΣ (ΔΙΟΡΘΩΣΗ) ---
        // Χρησιμοποιούμε το recipe.getSteps() αντί για σκέτο steps
        if (recipe.getSteps() != null) {
            int stepDurationSum = recipe.getSteps().stream()
                    .mapToInt(step -> {
                        // Έλεγχος αν το duration είναι null (για ασφάλεια)
                        if (step.getDuration() == null) return 0;
                        return step.getDuration().intValue();
                    })
                    .sum();

            if (stepDurationSum != command.getTotalTime()) {
                throw new IllegalArgumentException("Ο συνολικός χρόνος (" + command.getTotalTime() +
                        ") δεν ταιριάζει με το άθροισμα των βημάτων (" + stepDurationSum + ")");
            }
        }
        // ------------------------------------

        // 6. Αποθήκευση αλλαγών
        recipeDomainService.save(recipe);
    }

    private List<Ingredient> mapIngredients(List<IngredientDto> dtos) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream().map(dto -> {
            Ingredient ing = new Ingredient();
            ing.setName(dto.getName());
            ing.setQuantity(dto.getQuantity());
            if (dto.getUnit() != null && !dto.getUnit().isEmpty()) {
                try {
                    ing.setUnit(Unit.valueOf(dto.getUnit().trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    ing.setUnit(null);
                }
            }
            return ing;
        }).collect(Collectors.toList());
    }
}