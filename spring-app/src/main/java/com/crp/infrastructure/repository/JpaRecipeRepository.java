package com.crp.infrastructure.repository;

import com.crp.infrastructure.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRecipeRepository extends JpaRepository<RecipeEntity, Long> {

}
