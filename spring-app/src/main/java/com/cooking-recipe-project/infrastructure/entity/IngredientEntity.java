package com.cooking.recipe.project.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public IngredientEntity() {
    }

    public IngredientEntity(String name) {
        this.name = name;
    }

    public IngredientEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {return id; }
    public void setId(Long id) {this.id = id;   }

    public String getName() { return name;    }
    public void setName(String name) {this.name = name;   }
}
