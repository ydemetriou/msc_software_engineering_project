package com.crp.api;

import com.crp.application.dto.*;
import com.crp.application.usecase.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final CreateRecipeUseCase createUseCase;
    private final GetAllRecipesUseCase getAllUseCase;
    private final GetRecipeUseCase getUseCase;
    private final UpdateRecipeUseCase updateUseCase;
    private final DeleteRecipeUseCase deleteUseCase;

    public RecipeController(CreateRecipeUseCase createUseCase,
                            GetRecipeUseCase getUseCase,
                            GetAllRecipesUseCase getAllUseCase,
                            UpdateRecipeUseCase updateUseCase,
                            DeleteRecipeUseCase deleteUseCase) {
        this.createUseCase = createUseCase;
        this.getUseCase = getUseCase;
        this.getAllUseCase = getAllUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @PostMapping
    public void create(@RequestBody CreateRecipeCommand command) {
        createUseCase.execute(command);
    }

    @GetMapping("/{id}")
    public RecipeDto get(@PathVariable Long id) {
        return getUseCase.execute(id);
    }

    @GetMapping
    public List<RecipeDto> getAll() {
        return getAllUseCase.execute();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody CreateRecipeCommand command) {
        updateUseCase.execute(id, command);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteUseCase.execute(id);
    }
}