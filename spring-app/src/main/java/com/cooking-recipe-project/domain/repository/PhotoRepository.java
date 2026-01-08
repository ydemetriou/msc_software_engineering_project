package com.cooking.recipe.project.domain.repository;

import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.domain.model.Photo;
import com.cooking.recipe.project.domain.model.Step;
import java.util.List;

public interface PhotoRepository {
    Photo findById(Long id);
    List<Photo> findAll();
    Photo findByName(String name);
    Photo save(Photo photo);
    void delete(Long id);
}
