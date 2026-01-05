package it.uniroma3.siw.controller;

import it.uniroma3.siw.security.CustomUserDetails;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ReviewService reviewService;

    // Dashboard amministratore
    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("recipes", recipeService.findAll());
        return "admin/dashboard";
    }

    // Visualizza tutti gli utenti
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    // UC6: Banna utente (amministratore)
    @PostMapping("/users/{id}/ban")
    public String banUser(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (id.equals(userDetails.getUser().getId())) {
            return "redirect:/admin/users?error=cannot_ban_yourself";
        }
        userService.banUser(id);
        return "redirect:/admin/users";
    }

    // Sbanna utente (amministratore)
    @PostMapping("/users/{id}/unban")
    public String unbanUser(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (id.equals(userDetails.getUser().getId())) {
            return "redirect:/admin/users?error=cannot_unban_yourself";
        }
        userService.unbanUser(id);
        return "redirect:/admin/users";
    }

    // UC5: Elimina qualsiasi ricetta (amministratore)
    @PostMapping("/recipes/{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.delete(id);
        return "redirect:/recipes";
    }

    // Elimina recensione inappropriata (amministratore)
    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id,
            @RequestParam Long recipeId) {
        reviewService.delete(id);
        return "redirect:/recipes/" + recipeId;
    }
}
