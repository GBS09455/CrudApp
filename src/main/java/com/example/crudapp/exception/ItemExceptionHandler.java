package com.example.crudapp.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ItemExceptionHandler {

    private final MessageSource messageSource;

    private HttpHeaders utf8Headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
        return headers;
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<String> handleItemAlreadyExists(ItemAlreadyExistsException ex, Locale locale) {
        String msg = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), ex.getMessage(), locale);
        return ResponseEntity.badRequest().headers(utf8Headers()).body(msg);
    }

    @ExceptionHandler(PersonAlreadyExceptionHandler.class)
    public ResponseEntity<String> handlePersonAlreadyExists(PersonAlreadyExceptionHandler ex, Locale locale) {
        String msg = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), ex.getMessage(), locale);
        return ResponseEntity.status(HttpStatus.CONFLICT).headers(utf8Headers()).body(msg);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<String> handlePersonNotFound(PersonNotFoundException ex, Locale locale) {
        String msg = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), ex.getMessage(), locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(utf8Headers()).body(msg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex, Locale locale) {
        String msg = messageSource.getMessage("error.illegal.argument", new Object[]{ex.getMessage()}, ex.getMessage(), locale);
        return ResponseEntity.badRequest().headers(utf8Headers()).body(msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex, Locale locale) {
        String msg = messageSource.getMessage("error.internal", new Object[]{ex.getMessage()}, "Internal server error: " + ex.getMessage(), locale);
        return new ResponseEntity<>(msg, utf8Headers(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
