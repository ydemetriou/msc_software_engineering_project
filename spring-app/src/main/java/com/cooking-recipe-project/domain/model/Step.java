package com.cooking.recipe.project.domain.model;

import java.util.List;

public class Step {

    private Long id;

    private String title;

    private String description;

    private Long duration;

    private List<Photo> photos;

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

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
    }
}