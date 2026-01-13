package com.crp.infrastructure.repository;

import com.crp.domain.repository.StepRepository;
import com.crp.infrastructure.entity.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStepRepository extends JpaRepository<StepEntity, Long>, StepRepository {
}
