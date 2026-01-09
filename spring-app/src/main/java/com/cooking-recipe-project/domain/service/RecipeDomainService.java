package com.cooking.recipe.project.domain.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.cooking.recipe.project.domain.model.Recipe;
import com.cooking.recipe.project.infrastructure.entity.RecipeEntity;
import com.cooking.recipe.project.infrastructure.repository.JpaRecipeRepository;
import com.cooking.recipe.project.infrastructure.mapper.RecipeMapper;

@Service
public class RecipeDomainService {

    @Autowired
    private JpaRecipeRepository recipeRepository;

    public Recipe createRecipe(Long recipeId) {
        RecipeEntity entity = recipeRepository.findById(recipeId).orElse(null);
        if (entity == null) return null;

        return RecipeMapper.toDomain(entity);
    }
}
