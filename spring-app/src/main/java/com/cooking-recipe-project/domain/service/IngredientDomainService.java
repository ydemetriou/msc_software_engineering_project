package com.cooking.recipe.project.domain.service;

import com.cooking.recipe.project.domain.model.Ingredient;
import com.cooking.recipe.project.infrastructure.entity.IngredientEntity;
import com.cooking.recipe.project.infrastructure.mapper.IngredientMapper;
import com.cooking.recipe.project.infrastructure.repository.JpaIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientDomainService {

    @Autowired
    private JpaIngredientRepository ingredientRepository;

    public Ingredient createIngredient(Long ingredientId) {
        IngredientEntity entity = ingredientRepository.findById(ingredientId).orElse(null);
        if (entity == null) return null;
        return IngredientMapper.toDomain(entity);
    }
}

