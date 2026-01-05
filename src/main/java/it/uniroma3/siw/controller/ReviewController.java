package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.security.CustomUserDetails;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/recipes/{recipeId}/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private RecipeService recipeService;
    
    // UC4: Inserire recensione (utente registrato)
    @PostMapping
    public String createReview(@PathVariable Long recipeId,
                              @Valid @ModelAttribute Review review,
                              BindingResult result,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            return "redirect:/recipes/" + recipeId;
        }
        
        Recipe recipe = recipeService.findById(recipeId);
        reviewService.save(review, recipe, userDetails.getUser());
        
        return "redirect:/recipes/" + recipeId;
    }
    
    // Elimina recensione (solo autore)
    @PostMapping("/{reviewId}/delete")
    public String deleteReview(@PathVariable Long recipeId,
                              @PathVariable Long reviewId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Review review = reviewService.findById(reviewId);
        
        // Verifica che l'utente sia l'autore
        if (reviewService.isAuthor(review, userDetails.getUser())) {
            reviewService.delete(reviewId);
        }
        
        return "redirect:/recipes/" + recipeId;
    }
}
