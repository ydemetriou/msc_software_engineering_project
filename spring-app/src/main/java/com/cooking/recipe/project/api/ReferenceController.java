package com.cooking.recipe.project.api;

import com.cooking.recipe.project.domain.model.Category;
import com.cooking.recipe.project.domain.model.enums.Difficulty;
import com.cooking.recipe.project.domain.model.enums.Unit;
import com.cooking.recipe.project.domain.service.CategoryDomainService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/references")
@CrossOrigin(origins = "*")
public class ReferenceController {

    private final CategoryDomainService categoryService;

    public ReferenceController(CategoryDomainService categoryService) {
        this.categoryService = categoryService;
    }

    // 1. Κατηγορίες από τη Βάση Δεδομένων (MySQL)
    @GetMapping("/categories")
    public List<String> getCategories() {
        return categoryService.findAll().stream()
                .map(Category::getName) // Παίρνουμε μόνο τα ονόματα
                .collect(Collectors.toList());
    }

    // 2. Δυσκολίες από το Java Enum (Difficulty)
    @GetMapping("/difficulties")
    public List<String> getDifficulties() {
        return Arrays.stream(Difficulty.values())
                .map(Enum::name) // Επιστρέφει "EASY", "MEDIUM", "HARD"
                .collect(Collectors.toList());
    }

    // 3. Μονάδες Μέτρησης από το Java Enum (Unit)
    @GetMapping("/units")
    public List<String> getUnits() {
        return Arrays.stream(Unit.values())
                .map(Enum::name) // Επιστρέφει "GR", "KG", "ML", κλπ.
                .collect(Collectors.toList());
    }
}