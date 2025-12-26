package com.cooking.recipe.project.repository;

import com.cooking.recipe.project.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Το Spring Boot φτιάχνει αυτόματα τα save, delete, findById, findAll!
}