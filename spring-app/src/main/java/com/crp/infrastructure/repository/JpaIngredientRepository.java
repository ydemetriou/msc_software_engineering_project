package com.crp.infrastructure.repository;

import com.crp.domain.repository.IngredientRepository;
import com.crp.infrastructure.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaIngredientRepository extends JpaRepository<IngredientEntity, Long>, IngredientRepository {
}

