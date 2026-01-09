package com.cooking.recipe.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.service.CategoryDomainService;

@RestController
public class IndexController {

    @Autowired
    private CategoryDomainService categoryService;

    @GetMapping({"/",  "/index"})
    public String index() {
        Category category = categoryService.create("test");
        categoryService.save(category);
        System.out.println("Category saved with name: " + category.getName());
        return category.getName();
    }
}
