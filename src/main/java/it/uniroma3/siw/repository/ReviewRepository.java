package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {  // CORRETTO!
    List<Review> findByRecipeId(Long recipeId);  // CORRETTO!
    List<Review> findByAuthorId(Long authorId);   // CORRETTO!
}  // MANCAVA!
