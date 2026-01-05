package it.uniroma3.siw.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gestisce ResourceNotFoundException (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Risorsa non trovata: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("resourceName", ex.getResourceName());
        return mav;
    }

    /**
     * Gestisce UnauthorizedAccessException (403)
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        logger.warn("Accesso non autorizzato: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error/access-denied");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    /**
     * Gestisce errori di validazione
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Errore di validazione: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("errors", ex.getBindingResult().getAllErrors());
        return mav;
    }

    /**
     * Gestisce 404 per route non esistenti
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex) {
        logger.warn("Pagina non trovata: {}", ex.getRequestURL());

        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", "La pagina richiesta non esiste");
        return mav;
    }

    /**
     * Gestisce tutte le altre eccezioni (500)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex) {
        logger.error("Errore interno del server", ex);

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "Si è verificato un errore interno");
        mav.addObject("exceptionType", ex.getClass().getSimpleName());
        return mav;
    }
}
