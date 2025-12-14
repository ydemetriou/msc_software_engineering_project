package com.cooking_recipe_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooking_recipe_project.entity.TestTable;

public interface TestTableRepository extends JpaRepository<TestTable, Long> {
}
