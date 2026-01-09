package com.cooking.recipe.project.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.cooking.recipe.project.domain.model.Step;
import com.cooking.recipe.project.infrastructure.entity.StepEntity;
import com.cooking.recipe.project.infrastructure.repository.JpaStepRepository;
import com.cooking.recipe.project.infrastructure.mapper.StepMapper;

@Service
public class StepDomainService {

    @Autowired
    private JpaStepRepository stepRepository;

    public Step createStep(Long stepId) {
        StepEntity entity = stepRepository.findById(stepId).orElse(null);
        if (entity == null) return null;

        return StepMapper.toDomain(entity);
    }
}
