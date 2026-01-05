package it.uniroma3.siw.config;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crea admin di default se non esiste
        if (!userRepository.existsByEmail("admin@siwrecipes.it")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setSurname("Administrator");
            admin.setEmail("admin@siwrecipes.it");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setBanned(false);
            userRepository.save(admin);
            
            System.out.println("Admin creato: admin@siwrecipes.it / admin123");
        }
    }
}
