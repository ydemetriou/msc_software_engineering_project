package com.cooking.recipe.project.application.usecase;

import com.cooking.recipe.project.domain.service.RecipeDomainService; // <--- ΝΕΟ IMPORT
import org.springframework.stereotype.Service;

@Service
public class DeleteRecipeUseCase {
    private final RecipeDomainService recipeDomainService;

    public DeleteRecipeUseCase(RecipeDomainService recipeDomainService) {
        this.recipeDomainService = recipeDomainService;
    }

    public void execute(Long id) {
        // Διαγραφή μέσω του Service
        recipeDomainService.delete(id);
    }
}