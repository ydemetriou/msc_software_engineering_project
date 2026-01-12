package com.cooking.recipe.project.infrastructure.repository;

import com.cooking.recipe.project.domain.repository.IngredientRepository;
import com.cooking.recipe.project.infrastructure.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaIngredientRepository extends JpaRepository<IngredientEntity, Long>, IngredientRepository {
}

