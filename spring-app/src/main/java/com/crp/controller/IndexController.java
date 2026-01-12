package com.crp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.crp.domain.model.Photo;
import com.crp.domain.model.Category;
import com.crp.domain.model.Ingredient;
import com.crp.domain.model.Recipe;
import com.crp.domain.model.Step;

import com.crp.domain.service.RecipeDomainService;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private RecipeDomainService recipeService;

    @GetMapping({"/",  "/index"})
    public String index() {
        // Load recipe with id=2 from DB via service
        Recipe recipe = recipeService.createRecipeFromDB(2L);
        if (recipe == null) {
            return "<html><body><p>Recipe with id=2 not found.</p></body></html>";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h1>Recipe: ").append(escape(recipe.getName())).append("</h1>");
        sb.append("<p><strong>ID:</strong> ").append(recipe.getId()).append("</p>");
        Category category = recipe.getCategory();
        sb.append("<p><strong>Category:</strong> ")
          .append(category != null ? escape(category.getName()) : "-")
          .append("</p>");
        sb.append("<p><strong>Difficulty:</strong> ")
          .append(recipe.getDifficulty() != null ? escape(recipe.getDifficulty().name()) : "-")
          .append("</p>");
        sb.append("<p><strong>Total time:</strong> ").append(recipe.getTotalTime()).append(" minutes</p>");

        List<Photo> recipePhotos = recipe.getPhotos();
        sb.append("<h2>Recipe photos (" ).append(recipePhotos != null ? recipePhotos.size() : 0).append(")</h2>");
        if (recipePhotos != null && !recipePhotos.isEmpty()) {
            sb.append("<ul>");
            for (Photo p : recipePhotos) {
                sb.append("<li>").append(escape(p.getUrl())).append(" <small>(id=")
                  .append(p.getId()).append(")</small></li>");
            }
            sb.append("</ul>");
        } else {
            sb.append("<p><em>No photos</em></p>");
        }

        // Ingredients at recipe level
        List<Ingredient> ingredients = recipe.getIngredients();
        sb.append("<h2>Ingredients (" ).append(ingredients != null ? ingredients.size() : 0).append(")</h2>");
        if (ingredients != null && !ingredients.isEmpty()) {
            sb.append("<ul>");
            for (Ingredient ing : ingredients) {
                String unitStr = ing.getUnit() != null ? ing.getUnit().name() : "-";
                sb.append("<li>")
                  .append(escape(ing.getName()))
                  .append(" ").append(ing.getQuantity()).append(" ").append(escape(unitStr))
                  .append(" <small>(id=").append(ing.getId()).append(")</small></li>");
            }
            sb.append("</ul>");
        } else {
            sb.append("<p><em>No ingredients</em></p>");
        }

        // Steps printing
        List<Step> steps = recipe.getSteps();
        sb.append("<h2>Steps (" ).append(steps != null ? steps.size() : 0).append(")</h2>");
        if (steps != null && !steps.isEmpty()) {
            sb.append("<ol>");
            for (Step s : steps) {
                sb.append("<li><strong>")
                  .append(escape(s.getTitle()))
                  .append("</strong> <small>(id=").append(s.getId())
                  .append(", duration ").append(s.getDuration()).append("m)</small>");

                List<Photo> stepPhotos = s.getPhotos();
                sb.append("<div><strong>Photos (" )
                  .append(stepPhotos != null ? stepPhotos.size() : 0)
                  .append(")</strong></div>");
                if (stepPhotos != null && !stepPhotos.isEmpty()) {
                    sb.append("<ul>");
                    for (Photo sp : stepPhotos) {
                        sb.append("<li>").append(escape(sp.getUrl()))
                          .append(" <small>(id=").append(sp.getId()).append(")</small></li>");
                    }
                    sb.append("</ul>");
                } else {
                    sb.append("<p><em>No step photos</em></p>");
                }

                // Step ingredients
                List<Ingredient> stepIngs = s.getIngredients();
                sb.append("<div><strong>Ingredients (" )
                  .append(stepIngs != null ? stepIngs.size() : 0)
                  .append(")</strong></div>");
                if (stepIngs != null && !stepIngs.isEmpty()) {
                    sb.append("<ul>");
                    for (Ingredient si : stepIngs) {
                        String sUnitStr = si.getUnit() != null ? si.getUnit().name() : "-";
                        sb.append("<li>")
                          .append(escape(si.getName()))
                          .append(" ").append(si.getQuantity()).append(" ").append(escape(sUnitStr))
                          .append(" <small>(id=").append(si.getId()).append(")</small></li>");
                    }
                    sb.append("</ul>");
                } else {
                    sb.append("<p><em>No step ingredients</em></p>");
                }

                sb.append("</li>");
            }
            sb.append("</ol>");
        } else {
            sb.append("<p><em>No steps</em></p>");
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    // Basic HTML escape to avoid broken markup
    private String escape(String s) {
        if (s == null) return "";
        return s
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

}
