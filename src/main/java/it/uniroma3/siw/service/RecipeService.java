package it.uniroma3.siw.service;

import it.uniroma3.siw.exception.ResourceNotFoundException;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe findById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ricetta", "id", id));
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }

    public List<Recipe> findByAuthor(Long authorId) {
        return recipeRepository.findByAuthorId(authorId);
    }

    @Transactional
    public Recipe save(Recipe recipe, User author) {
        recipe.setAuthor(author);
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe update(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    public boolean isAuthor(Recipe recipe, User user) {
        return recipe.getAuthor().getId().equals(user.getId());
    }
}
