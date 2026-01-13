package com.crp.infrastructure.repository;

import com.crp.domain.repository.CategoryRepository;
import com.crp.infrastructure.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long>, CategoryRepository {
    Optional<CategoryEntity> findByName(String name);}
