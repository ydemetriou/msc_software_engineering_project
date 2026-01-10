package com.cooking.recipe.project.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/references")
@CrossOrigin(origins = "*")
public class ReferenceController {

    @GetMapping("/categories")
    public List<String> getCategories() {
        return Arrays.asList(
                "Ζυμαρικά", "Κρεατικά", "Λαδερά", "Όσπρια",
                "Θαλασσινά", "Γλυκά", "Σαλάτες", "Πίτες"
        );
    }

    @GetMapping("/units")
    public List<String> getUnits() {
        return Arrays.asList(
                "gr", "kg", "ml", "lt",
                "τεμάχια", "κουταλιά σούπας", "κουταλιά γλυκού", "κούπα"
        );
    }

    @GetMapping("/difficulties")
    public List<String> getDifficulties() {
        return Arrays.asList("Easy", "Medium", "Hard");
    }
}