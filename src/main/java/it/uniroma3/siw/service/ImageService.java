package it.uniroma3.siw.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    // Directory dove salvare le immagini (esterna al classpath per accesso
    // immediato)
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/images/recipes/";

    /**
     * Salva un'immagine caricata dall'utente e restituisce il path relativo
     * 
     * @param file Il file immagine caricato
     * @return Il path relativo dell'immagine (es: "/images/recipes/xyz.jpg")
     * @throws IOException Se c'è un errore nel salvataggio
     */
    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Crea la directory se non esiste
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Genera un nome file univoco usando UUID + timestamp
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = "recipe_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        // Salva il file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Restituisce il path relativo per l'URL.
        // In MvcConfig mappiamo "/images/**" a "file:uploads/images/"
        // Quindi "/images/recipes/nomefile.jpg" punterà a
        // "uploads/images/recipes/nomefile.jpg"
        return "/images/recipes/" + uniqueFilename;
    }

    /**
     * Elimina un'immagine dal filesystem
     * 
     * @param imageUrl Il path dell'immagine da eliminare
     */
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            // Rimuove il prefisso "/images/recipes/" per ottenere solo il nome file
            String filename = imageUrl.replace("/images/recipes/", "");
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log dell'errore ma non bloccare l'esecuzione
            System.err.println("Errore nell'eliminazione dell'immagine: " + e.getMessage());
        }
    }
}
