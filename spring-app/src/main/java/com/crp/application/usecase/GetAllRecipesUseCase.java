package com.crp.application.usecase;

import com.crp.application.dto.RecipeDto;
import com.crp.domain.service.RecipeDomainService; // <--- ΝΕΟ IMPORT
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllRecipesUseCase {
    private final RecipeDomainService recipeDomainService;

    public GetAllRecipesUseCase(RecipeDomainService recipeDomainService) {
        this.recipeDomainService = recipeDomainService;
    }

    public List<RecipeDto> execute() {
        // Καλούμε το findAll του Service και μετατρέπουμε σε DTOs
        return recipeDomainService.findAll().stream()
                .map(RecipeDto::new)
                .collect(Collectors.toList());
    }
}