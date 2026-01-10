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

    @GetMapping({"/",  "/index"})
    public String index() {
//      Category Service
//        Category category = categoryService.create("test");
//        categoryService.save(category);
//        System.out.println("Category saved with name: " + category.getName());
//        return category.getName();
//      Ingredient Service
//        Ingredient ingredient = ingredientService.create("pepper",10,"kg");
//        ingredientService.save(ingredient);
//        System.out.println("Ingredient saved with name: " + ingredient.getName());
//        return ingredient.getName();

//      Photo Service arguments 1st url, 2nd recipe_id, 3rd step_id
//        Photo photo = photoService.create("test.jpg", 1L,null);
//        photoService.save(photo);
//        System.out.println("Photo saved with name: " + photo.getUrl());
//        return photo.getUrl();
//

//      Recipe Service
        Category category = new Category("Zymarika");

        List<Photo> photos = List.of(
                new Photo("photo1"),
                new Photo("photo2")
        );

        List<Ingredient> ingredients = List.of(
                new Ingredient("ingredient1"),
                new Ingredient("ingredient2")
        );

        List<Step> steps = List.of(
                new Step("step1", "step1 description",10L),
                new Step("step2", "step2 description",10L)
        );

        Recipe recipe = recipeService.create("συνταγή 1", category, "Εύκολη", 10, photos, ingredients, steps);


        recipe = recipeService.save(recipe); // Συνήθως το save επιστρέφει το αποθηκευμένο αντικείμενο (με ID)

        System.out.println("Recipe saved with ID: " + recipe.getId() + " and name: " + recipe.getName());
        return recipe.getName();

    }

}
