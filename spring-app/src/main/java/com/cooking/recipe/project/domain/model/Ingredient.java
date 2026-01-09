package com.cooking.recipe.project.domain.model;

public class Ingredient {

    private Long id;

    private String name;

    private double quantity;

    private String unit;

    public Ingredient() {}

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}