package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.security.CustomUserDetails;
import it.uniroma3.siw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Mostra la pagina del profilo utente
     */
    @GetMapping
    public String showProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null || userDetails.getUser() == null) {
            return "redirect:/login";
        }

        User user = userService.findById(userDetails.getUser().getId());
        model.addAttribute("user", user);
        return "user/profile";
    }

    /**
     * Gestisce la cancellazione definitiva dell'account utente
     */
    @PostMapping("/delete")
    public String deleteAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null || userDetails.getUser() == null) {
            return "redirect:/login";
        }

        Long userId = userDetails.getUser().getId();

        // Elimina l'account
        userService.deleteAccount(userId);

        // Logout automatico
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Messaggio di conferma
        redirectAttributes.addFlashAttribute("accountDeleted",
                "Il tuo account è stato eliminato con successo.");

        return "redirect:/recipes";
    }
}
