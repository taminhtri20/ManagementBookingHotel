package org.example.bookinghotel.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestControllerAdvice
public class RestGlobalController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidateException(MethodArgumentNotValidException e, HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {
        String uri = request.getRequestURI();
        redirectAttributes.addFlashAttribute("error", e);
        return "redirect:" + uri;
    }
}
