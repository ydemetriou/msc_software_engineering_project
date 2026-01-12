package com.crp.application.usecase;

import com.crp.domain.service.RecipeDomainService; // <--- ΝΕΟ IMPORT
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