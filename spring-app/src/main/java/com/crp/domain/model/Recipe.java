package com.crp.domain.model;

import java.util.ArrayList;
import java.util.List;
import com.crp.domain.model.Ingredient;
import com.crp.domain.model.Category;
import com.crp.domain.model.Photo;
import com.crp.domain.model.Step;
import com.crp.domain.model.enums.Difficulty;

public class Recipe {
    private Long id;

    private String name;

    private Category category;

    private Difficulty difficulty;

    private int totalTime;

    private List<Photo> photos;

    private List<Ingredient> ingredients;

    private List<Step> steps;

    public Recipe(
            Long id,
            String name,
            Category category,
            Difficulty difficulty,
            int totalTime,
            List<Photo> photos,
            List<Ingredient> ingredients,
            List<Step> steps)
    {
        this.id = id;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.photos = photos;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe(
            String name,
            Category category,
            Difficulty difficulty,
            int totalTime,
            List<Photo> photos,
            List<Ingredient> ingredients,
            List<Step> steps)
    {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.photos = photos;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public void addIngredient(Ingredient ing) {
        ingredients.add(ing);
    }
    public void removeIngredient(Ingredient ing) {
        ingredients.remove(ing);
    }

    public void addStep(Step step) {
        // Keep insertion order; just append
        steps.add(step);
    }
    public void removeStep(Step step) { steps.remove(step); }

    // Reorder helpers
    public void moveStep(int fromIndex, int toIndex) {
        if (steps == null) return;
        if (fromIndex < 0 || fromIndex >= steps.size()) return;
        if (toIndex < 0 || toIndex >= steps.size()) return;
        Step s = steps.remove(fromIndex);
        steps.add(toIndex, s);
    }

    public void placeStepAfter(Step step, Step after) {
        if (steps == null) return;
        if (!steps.remove(step)) return; // remove existing occurrence
        int idx = steps.indexOf(after);
        if (idx == -1) {
            steps.add(step); // if 'after' not found, append
        } else {
            steps.add(idx + 1, step);
        }
    }

    public Long SummarizeStepsDuration() {
        return steps.stream().mapToLong(Step::getDuration).sum();
    }

    public double calculateProgress(Long lastCompletedStepId) {
        if (steps == null || steps.isEmpty() || lastCompletedStepId == null) {
            return 0.0;
        }
        // Sum durations up to and including the lastCompletedStepId, using insertion order
        long completedDuration = 0L;
        for (Step s : steps) {
            completedDuration += s.getDuration();
            if (lastCompletedStepId.equals(s.getId())) {
                break;
            }
        }
        int total = getTotalTime();
        if (total <= 0) {
            return 0.0;
        }
        // Return ratio [0.0, 1.0]
        double progress = (double) completedDuration / (double) total;
        if (progress < 0.0) return 0.0;
        if (progress > 1.0) return 1.0;
        return progress;
    }

    // Getters and setters (προαιρετικά)
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;    }

    public String getName() {return name;    }
    public void setName(String name) {this.name = name;}

    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category = category;}

    public Difficulty getDifficulty() {return difficulty;}
    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}

    public int getTotalTime() {return totalTime;}
    public void setTotalTime(int totalTime) {this.totalTime = totalTime;}

    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<Step> getSteps() {
        return steps;
    }
    public void setSteps(List<Step> steps) {
        // Set list as-is, preserving provided order, no auto-sorting
        this.steps = steps;
    }
}
