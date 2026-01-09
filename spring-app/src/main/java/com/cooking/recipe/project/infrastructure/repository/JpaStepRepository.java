package com.cooking.recipe.project.infrastructure.repository;

import com.cooking.recipe.project.domain.repository.StepRepository;
import com.cooking.recipe.project.infrastructure.entity.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStepRepository extends JpaRepository<StepEntity, Long>, StepRepository {
}
