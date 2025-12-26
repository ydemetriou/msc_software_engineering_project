package com.cooking.recipe.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_table")
public class TestTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
}
