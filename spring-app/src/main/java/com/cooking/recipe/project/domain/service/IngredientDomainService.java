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

    public Ingredient createIngredientFromDB(Long ingredientId) {
        IngredientEntity entity = ingredientRepository.findById(ingredientId).orElse(null);
        if (entity == null) return null;
        return IngredientMapper.toDomain(entity);
    }

    public Ingredient create(String name, double quantity, String unit) {
        Ingredient ing = new Ingredient();
        ing.setName(name);
        ing.setQuantity(quantity);
        ing.setUnit(unit);
        return ing;
    }

    public Ingredient save(Ingredient ingredient) {
        IngredientEntity saved = ingredientRepository.save(IngredientMapper.toEntity(ingredient));
        return IngredientMapper.toDomain(saved);
    }

    public Ingredient update(Ingredient ingredient) {
        if (ingredient == null || ingredient.getId() == null) return null;
        IngredientEntity saved = ingredientRepository.save(IngredientMapper.toEntity(ingredient));
        return IngredientMapper.toDomain(saved);
    }

    public void delete(Long id) {
        if (id != null) ingredientRepository.deleteById(id);
    }
}
