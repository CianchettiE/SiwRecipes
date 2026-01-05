package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.security.CustomUserDetails;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    // UC1: Visualizzare elenco ricette (utente generico)
    @GetMapping
    public String getAllRecipes(Model model) {
        model.addAttribute("recipes", recipeService.findAll());
        return "recipes/list";
    }

    // Visualizza le ricette dell'utente corrente
    @GetMapping("/my-recipes")
    public String getMyRecipes(@AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        User currentUser = userDetails.getUser();
        model.addAttribute("recipes", recipeService.findByAuthor(currentUser.getId()));
        model.addAttribute("isMyRecipesPage", true);
        return "recipes/my-recipes";
    }

    // UC2: Visualizzare dettaglio ricetta (utente generico)
    @GetMapping("/{id}")
    public String getRecipeDetail(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("reviews", reviewService.findByRecipe(id));
        model.addAttribute("review", new Review()); // IMPORTANTE!
        return "recipes/detail";
    }

    // Filtra ricette per categoria
    @GetMapping("/category/{category}")
    public String getRecipesByCategory(@PathVariable String category, Model model) {
        model.addAttribute("recipes", recipeService.findByCategory(category));
        model.addAttribute("category", category);
        return "recipes/list";
    }

    // UC3: Form per inserire nuova ricetta (utente registrato)
    @GetMapping("/new")
    public String showNewRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "recipes/form";
    }

    // UC3: Salva nuova ricetta (utente registrato)
    @PostMapping("/new")
    public String createRecipe(@Valid @ModelAttribute Recipe recipe,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (result.hasErrors()) {
            return "recipes/form";
        }

        User author = userDetails.getUser();
        recipeService.save(recipe, author);

        return "redirect:/recipes";
    }

    // Form per modificare ricetta (solo autore)
    @GetMapping("/{id}/edit")
    public String showEditRecipeForm(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        Recipe recipe = recipeService.findById(id);

        // Verifica che l'utente sia l'autore
        if (!recipeService.isAuthor(recipe, userDetails.getUser())) {
            return "redirect:/recipes/" + id;
        }

        model.addAttribute("recipe", recipe);
        return "recipes/form";
    }

    // Aggiorna ricetta (solo autore)
    @PostMapping("/{id}/edit")
    public String updateRecipe(@PathVariable Long id,
            @Valid @ModelAttribute Recipe recipe,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            return "recipes/form";
        }

        Recipe existingRecipe = recipeService.findById(id);
        if (!recipeService.isAuthor(existingRecipe, userDetails.getUser())) {
            return "redirect:/recipes/" + id;
        }

        recipe.setId(id);
        recipe.setAuthor(existingRecipe.getAuthor());
        recipe.setCreatedAt(existingRecipe.getCreatedAt());
        recipeService.update(recipe);
        return "redirect:/recipes/" + id;
    }

    // Elimina ricetta (solo autore)
    @PostMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Recipe recipe = recipeService.findById(id);
        if (recipeService.isAuthor(recipe, userDetails.getUser())) {
            recipeService.delete(id);
        }
        return "redirect:/recipes";
    }
}
