package it.uniroma3.siw.service;

import it.uniroma3.siw.exception.ResourceNotFoundException;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recensione", "id", id));
    }

    public List<Review> findByRecipe(Long recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    public List<Review> findByAuthor(Long authorId) {
        return reviewRepository.findByAuthorId(authorId);
    }

    @Transactional
    public Review save(Review review, Recipe recipe, User author) {
        review.setRecipe(recipe);
        review.setAuthor(author);
        return reviewRepository.save(review);
    }

    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public boolean isAuthor(Review review, User user) {
        return review.getAuthor().getId().equals(user.getId());
    }
}
