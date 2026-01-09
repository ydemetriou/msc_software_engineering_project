package com.cooking.recipe.project.domain.model;

public class Photo {

    private Long id;
    private String url;
    private Long recipeId;
    private Long stepId;

    public Photo() {
    }

    public Photo(String url) {
        this.url = url;
    }
    public Photo(Long id, String url) {
        this.id = id;
        this.url = url;
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

    public Long getRecipeId() {return recipeId; }
    public void setRecipeId(Long recipeId) {this.recipeId = recipeId; }
    public Long getStepId() {return stepId;    }
    public void setStepId(Long stepId) { this.stepId = stepId;    }

}