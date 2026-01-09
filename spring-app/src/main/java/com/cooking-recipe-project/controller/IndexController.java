package com.cooking.recipe.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.service.PhotoDomainService;
import com.cooking.recipe.project.domain.service.CategoryDomainService;
import com.cooking.recipe.project.domain.service.IngredientDomainService;

@RestController
public class IndexController {

    @Autowired
    private CategoryDomainService categoryService;
    @Autowired
    private IngredientDomainService ingredientService;
    @Autowired
    private PhotoDomainService photoService;

    @GetMapping({"/",  "/index"})
    public String index() {
//   Category Service
//        Category category = categoryService.create("test");
//        categoryService.save(category);
//        System.out.println("Category saved with name: " + category.getName());
//        return category.getName();

//   Ingredient Service
//        Ingredient ingredient = ingredientService.create("pepper",10,"kg");
//        ingredientService.save(ingredient);
//        System.out.println("Ingredient saved with name: " + ingredient.getName());
//        return ingredient.getName();

//    Photo Service arguments 1st url, 2nd recipe_id, 3rd step_id
        Photo photo = photoService.create("test.jpg", 1L,null);
        photoService.save(photo);
        System.out.println("Photo saved with name: " + photo.getUrl());
        return photo.getUrl();
    }
}
