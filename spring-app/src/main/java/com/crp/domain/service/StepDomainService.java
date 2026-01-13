package com.crp.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.crp.domain.model.Step;
import com.crp.infrastructure.entity.StepEntity;
import com.crp.infrastructure.repository.JpaStepRepository;
import com.crp.infrastructure.mapper.StepMapper;

@Service
public class StepDomainService {

    @Autowired
    private JpaStepRepository stepRepository;

    public Step createStepFromDB(Long stepId) {
        StepEntity entity = stepRepository.findById(stepId).orElse(null);
        if (entity == null) return null;

        return StepMapper.toDomain(entity);
    }

    public Step create(String title, String description, Long duration) {
        return new Step(title, description, duration);
    }

    public Step save(Step step) {
        StepEntity saved = stepRepository.save(StepMapper.toEntity(step));
        return StepMapper.toDomain(saved);
    }

    public Step update(Step step) {
        if (step == null || step.getId() == null) return null;
        StepEntity saved = stepRepository.save(StepMapper.toEntity(step));
        return StepMapper.toDomain(saved);
    }

    public void delete(Long id) {
        if (id != null) stepRepository.deleteById(id);
    }
}
