package com.cooking.recipe.project.domain.model;

public class Step {

    private Long id;

    private String title;

    private String description;

    private Long duration;

    public Step() {
    }

    public Step(String title, String description, Long duration) {
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
}