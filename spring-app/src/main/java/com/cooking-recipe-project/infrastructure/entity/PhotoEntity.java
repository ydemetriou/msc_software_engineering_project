package com.cooking.recipe.project.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "photos")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = true)
    private StepEntity step;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = true)
    private RecipeEntity recipe;

    public PhotoEntity() {
    }

    public PhotoEntity(String url) {
        this.url = url;
    }

    public PhotoEntity(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public PhotoEntity(Long id, String url, StepEntity step, RecipeEntity recipe) {
        this.id = id;
        this.url = url;
        this.step = step;
        this.recipe = recipe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StepEntity getStep() {
        return step;
    }

    public void setStep(StepEntity step) {
        this.step = step;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }
}
