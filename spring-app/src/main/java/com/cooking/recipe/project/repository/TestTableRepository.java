package com.cooking.recipe.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooking.recipe.project.entity.TestTable;

public interface TestTableRepository extends JpaRepository<TestTable, Long> {
}
