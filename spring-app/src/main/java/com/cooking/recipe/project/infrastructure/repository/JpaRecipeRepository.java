package com.cooking.recipe.project.infrastructure.repository;

import com.cooking.recipe.project.infrastructure.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRecipeRepository extends JpaRepository<RecipeEntity, Long> {

}
