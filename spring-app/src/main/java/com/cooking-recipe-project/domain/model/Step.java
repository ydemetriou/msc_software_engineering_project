package com.cooking.recipe.project.domain.model;

import java.util.List;

public class Step {

    private Long id;

    private String title;

    private String description;

    private Long duration;

    private List<Photo> photos;

    private List<Ingredient> ingredients;

    public Step() {
    }

    public Step(
            Long id,
            String title,
            String description,
            Long duration,
            List<Photo> photos
    ){
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.photos = photos;
    }

    public Step(String title, String description, Long duration) {
        this(null, title, description, duration, null);
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

    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
    }

    public void addIngredient(Ingredient ing) { this.ingredients.add(ing); }
    public void removeIngredient(Ingredient ing) { this.ingredients.remove(ing); }
}