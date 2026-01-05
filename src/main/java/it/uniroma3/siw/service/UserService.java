package it.uniroma3.siw.service;

import it.uniroma3.siw.exception.ResourceNotFoundException;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ReviewService reviewService;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "id", id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User registerUser(User user) {
        // Cripta la password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Imposta il ruolo di default
        user.setRole("USER");
        user.setBanned(false);
        user.setOauthProvider("LOCAL");
        return userRepository.save(user);
    }

    @Transactional
    public User banUser(Long userId) {
        User user = findById(userId);

        // Elimina tutte le ricette dell'utente
        List<Recipe> userRecipes = recipeService.findByAuthor(userId);
        for (Recipe recipe : userRecipes) {
            recipeService.delete(recipe.getId());
        }

        // Elimina tutte le recensioni dell'utente
        List<Review> userReviews = reviewService.findByAuthor(userId);
        for (Review review : userReviews) {
            reviewService.delete(review.getId());
        }

        // Banna l'utente
        user.setBanned(true);
        return userRepository.save(user);
    }

    @Transactional
    public User unbanUser(Long userId) {
        User user = findById(userId);
        user.setBanned(false);
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Elimina definitivamente l'account di un utente e tutti i dati associati.
     * Questa operazione è irreversibile.
     * 
     * @param userId ID dell'utente da eliminare
     */
    @Transactional
    public void deleteAccount(Long userId) {
        User user = findById(userId);

        // Elimina tutte le ricette dell'utente
        List<Recipe> userRecipes = recipeService.findByAuthor(userId);
        for (Recipe recipe : userRecipes) {
            recipeService.delete(recipe.getId());
        }

        // Elimina tutte le recensioni dell'utente
        List<Review> userReviews = reviewService.findByAuthor(userId);
        for (Review review : userReviews) {
            reviewService.delete(review.getId());
        }

        // Elimina definitivamente l'utente
        userRepository.delete(user);
    }
}
