package com.cooking.recipe.project.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "steps")
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Πηγή 12: Τίτλος, περιγραφή, διάρκεια
    private String title;

    @Column(length = 1000) // Μεγαλύτερο κείμενο για περιγραφή
    private String description;

    private int duration; // σε λεπτά

    // Το πεδίο για τη σειρά εμφάνισης (1ο, 2ο, κλπ) - {ordered}
    private int stepOrder;

    // Πηγή 12: Φωτογραφίες βήματος
    @ElementCollection
    private List<String> stepPhotos = new ArrayList<>();

    // Σχέση N:1 με τη Συνταγή (πολλά βήματα ανήκουν σε μία συνταγή)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    // Constructors, Getters, Setters
    public Step() {}

    public Step(String title, String description, int duration, int stepOrder) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.stepOrder = stepOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getStepOrder() { return stepOrder; }
    public void setStepOrder(int stepOrder) { this.stepOrder = stepOrder; }
    public List<String> getStepPhotos() { return stepPhotos; }
    public void setStepPhotos(List<String> stepPhotos) { this.stepPhotos = stepPhotos; }
    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}