package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {  // CORRETTO!
    List<Recipe> findByCategory(String category);  // CORRETTO!
    List<Recipe> findByAuthorId(Long authorId);    // CORRETTO!
}  // MANCAVA!
