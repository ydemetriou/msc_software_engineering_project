package com.cooking.recipe.project.domain.model;

public class Photo {

    private Long id;
    private String url;

    public Photo() {
    }

    public Photo(String url) {
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

}