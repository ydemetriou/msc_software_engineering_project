package com.cooking.recipe.project.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Double quantity;

    @Column
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

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
    public void setId(Long id) {this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {this.name = name; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public RecipeEntity getRecipe() { return recipe; }
    public void setRecipe(RecipeEntity recipe) { this.recipe = recipe; }
}
