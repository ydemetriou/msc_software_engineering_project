package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.application.dto.*;
import com.cooking.recipe.project.domain.model.*;
import com.cooking.recipe.project.domain.model.enums.Difficulty;
import com.cooking.recipe.project.domain.model.enums.Unit;
import com.cooking.recipe.project.domain.service.CategoryDomainService; // <--- ΝΕΟ
import com.cooking.recipe.project.domain.service.RecipeDomainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateRecipeUseCase {
    private final RecipeDomainService recipeDomainService;
    private final CategoryDomainService categoryDomainService; // <--- ΝΕΟ

    // Inject και τα δύο Services
    public CreateRecipeUseCase(RecipeDomainService recipeDomainService,
                               CategoryDomainService categoryDomainService) {
        this.recipeDomainService = recipeDomainService;
        this.categoryDomainService = categoryDomainService;
    }

    public void execute(CreateRecipeCommand command) {
        // 1. Κατηγορία: Την ψάχνουμε στη βάση!
        Category category = null;
        if (command.getCategory() != null) {
            // Ψάχνουμε με το όνομα (π.χ. "Ζυμαρικά")
            category = categoryDomainService.findByName(command.getCategory());

            // Αν δεν βρεθεί (π.χ. λάθος όνομα), αφήνουμε null για να μην σκάσει με 500 error
            if (category == null) {
                System.err.println("Category not found in DB: " + command.getCategory());
            }
        }

        // 2. Δυσκολία (με trim για ασφάλεια)
        Difficulty difficulty = null;
        if (command.getDifficulty() != null) {
            try {
                difficulty = Difficulty.valueOf(command.getDifficulty().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid difficulty: " + command.getDifficulty());
            }
        }

        // 3. Φωτογραφίες
        List<Photo> photos = new ArrayList<>();
        if (command.getPhotoUrls() != null) {
            photos = command.getPhotoUrls().stream()
                    .map(Photo::new)
                    .collect(Collectors.toList());
        }

        // 4. Υλικά
        List<Ingredient> ingredients = mapIngredients(command.getIngredients());

        // 5. Βήματα
        List<Step> steps = new ArrayList<>();
        if (command.getSteps() != null) {
            for (StepDto sDto : command.getSteps()) {
                Step step = new Step(sDto.getTitle(), sDto.getDescription(), sDto.getDuration());

                if (sDto.getPhotoUrls() != null) {
                    List<Photo> stepPhotos = sDto.getPhotoUrls().stream()
                            .map(Photo::new)
                            .collect(Collectors.toList());
                    step.setPhotos(stepPhotos);
                }

                if (sDto.getIngredients() != null) {
                    step.setIngredients(mapIngredients(sDto.getIngredients()));
                }

                steps.add(step);
            }
        }

        Recipe recipe = new Recipe(
                command.getName(),
                category,
                difficulty,
                command.getTotalTime(),
                photos,
                ingredients,
                steps
        );
        int stepDurationSum = steps.stream()
                .mapToInt(step -> step.getDuration().intValue())
                .sum();

        if (stepDurationSum != command.getTotalTime()) {
            throw new IllegalArgumentException("Ο συνολικός χρόνος (" + command.getTotalTime() +
                    ") δεν ταιριάζει με το άθροισμα των βημάτων (" + stepDurationSum + ")");
        }
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
                    System.err.println("Unknown unit: " + dto.getUnit());
                    ing.setUnit(null);
                }
            }
            return ing;
        }).collect(Collectors.toList());
    }
}