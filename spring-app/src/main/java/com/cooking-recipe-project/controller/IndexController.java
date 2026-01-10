package com.cooking.recipe.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.model.Step;

import com.cooking.recipe.project.domain.service.PhotoDomainService;
import com.cooking.recipe.project.domain.service.CategoryDomainService;
import com.cooking.recipe.project.domain.service.IngredientDomainService;
import com.cooking.recipe.project.domain.service.RecipeDomainService;
import com.cooking.recipe.project.domain.service.StepDomainService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private CategoryDomainService categoryService;
    @Autowired
    private IngredientDomainService ingredientService;
    @Autowired
    private PhotoDomainService photoService;
    @Autowired
    private RecipeDomainService recipeService;
    @Autowired
    private StepDomainService stepService;

    @GetMapping({"/",  "/index"})
    public String index() {
        // Create Category via service
        Category category = categoryService.create("Pasta");

        // Create recipe-level Photos via service
        List<Photo> recipePhotos = new ArrayList<>();
        recipePhotos.add(photoService.create("recipe_photo_1.jpg", null, null));
        recipePhotos.add(photoService.create("recipe_photo_2.jpg", null, null));

        // Create Steps via service with their own Photos
        List<Step> steps = new ArrayList<>();
        List<Photo> step1Photos = new ArrayList<>();
        step1Photos.add(photoService.create("step1_photo_1.jpg", null, null));
        Step step1 = stepService.create("Boil water", "Fill a pot with water and bring to a boil.", 10L);
        step1.setPhotos(step1Photos);

        List<Photo> step2Photos = new ArrayList<>();
        step2Photos.add(photoService.create("step2_photo_1.jpg", null, null));
        Step step2 = stepService.create("Add pasta", "Add pasta to boiling water and cook.", 8L);
        step2.setPhotos(step2Photos);

        steps.add(step1);
        steps.add(step2);

        // Optionally add ingredients via service (empty for now)
        List<Ingredient> ingredients = new ArrayList<>();
        // ingredients.add(ingredientService.create("Salt", 1.0, "tsp"));

        // Create the Recipe via the service layer
        Recipe recipe = recipeService.create(
                "Test Recipe",
                category,
                "Easy",
                20,
                recipePhotos,
                ingredients,
                steps
        );

        // Optionally save to DB for an end-to-end test:
        // recipe = recipeService.save(recipe);

        StringBuilder sb = new StringBuilder();
        sb.append("Recipe: ").append(recipe.getName()).append("\n");
        sb.append("Category: ").append(recipe.getCategory().getName()).append("\n");
        sb.append("Total time: ").append(recipe.getTotalTime()).append(" minutes\n");
        sb.append("Recipe photos: ").append(recipe.getPhotos() != null ? recipe.getPhotos().size() : 0).append("\n");
        sb.append("Steps: ").append(recipe.getSteps() != null ? recipe.getSteps().size() : 0).append("\n");
        for (Step s : recipe.getSteps()) {
            sb.append(" - ").append(s.getTitle())
              .append(" (duration ").append(s.getDuration()).append("m)")
              .append(", photos: ").append(s.getPhotos() != null ? s.getPhotos().size() : 0)
              .append("\n");
        }
        return sb.toString();
    }

}
