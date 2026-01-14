package com.crp.infrastructure.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "steps")
public class StepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column
    private Long duration; // in minutes

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoEntity> photos;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientEntity> ingredients;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeEntity recipe;

    public StepEntity() {
    }

    public StepEntity(String title, String description, Long duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public StepEntity(
            Long id,
            String title,
            String description, Long duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<PhotoEntity> getPhotos() {
        return photos;
    }

    // public void setPhotos(List<PhotoEntity> photos) {
    //     this.photos = photos;
    // }
    public void setPhotos(List<PhotoEntity> photos) {
        this.photos = photos;
        if (photos != null) {
            for (PhotoEntity photo : photos) {
                photo.setStep(this);
            }
        }
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }

    public void addIngredient(IngredientEntity ingredient) {
        if (ingredient == null) return;
        if (this.ingredients == null) {
            this.ingredients = new ArrayList<>();
        }
        // set back-references to maintain consistency
        ingredient.setStep(this);
        if (this.recipe != null) {
            ingredient.setRecipe(this.recipe);
        }
        this.ingredients.add(ingredient);
    }
}
