package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Il nome è obbligatorio")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Il cognome è obbligatorio")
    private String surname;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Inserisci un'email valida")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
    private String password;

    @Column(nullable = false)
    private String role; // "USER", "ADMIN"

    @Column(nullable = false)
    private boolean banned = false;

    @Column
    private String oauthProvider; // "LOCAL", "GOOGLE", "GITHUB"

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Recipe> recipes = new ArrayList<>(); // CORRETTO!

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>(); // CORRETTO!

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public List<Recipe> getRecipes() { // CORRETTO!
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) { // CORRETTO!
        this.recipes = recipes;
    }

    public List<Review> getReviews() { // CORRETTO!
        return reviews;
    }

    public void setReviews(List<Review> reviews) { // CORRETTO!
        this.reviews = reviews;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }
}
