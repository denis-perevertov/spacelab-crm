package com.example.spacelab.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@ControllerAdvice
@Log
public class GlobalControllerAdvice {

//    @ModelAttribute
//    public void displayLoggedInAdmin(HttpServletRequest request, HttpServletResponse response) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Admin admin = null;
//        if(principal != null) {log.info(principal.toString()); log.info(principal.getClass().getName());};
//        log.info(request.getRequestURI() + " - " + request.getHeader("Authentication") + " - " + ((admin != null) ? admin.toString() : null));
//    }


    @ExceptionHandler(ObjectValidationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> validationFailedHandler(ObjectValidationException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Validation error", HttpStatus.BAD_REQUEST.value(), ex.getErrors()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> resourceNotFoundHandler(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Resource not found", HttpStatus.NOT_FOUND.value(), Map.of(ex.getResourceClassName(), ex.getMessage())),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> expiredTokenHandler(ExpiredJwtException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Token expired", HttpStatus.UNAUTHORIZED.value(), Map.of("token", ex.getMessage())),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> badCredentialsInTokenHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Bad credentials!", HttpStatus.UNAUTHORIZED.value(), Map.of("msg", ex.getMessage())),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> accessDeniedHandler(AccessDeniedException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Access to this part of the API denied", HttpStatus.FORBIDDEN.value(), Map.of("access", ex.getMessage())),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> tokenExceptionHandler(TokenException e) {
        return new ResponseEntity<>(
                new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST.value(), Map.of("token", e.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LessonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> lessonExceptionHandler(LessonException e) {
        return new ResponseEntity<>(
                new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST.value(), Map.of("lesson", e.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> badRequestHandler(Exception e) {
        return new ResponseEntity<>(
                new ErrorMessage("Bad request!", HttpStatus.BAD_REQUEST.value(), Map.of("error", e.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<String> fallbackInternalErrorHandler(Exception ex) {
//        log.severe(ex.getMessage());
//        ex.printStackTrace();
//        return new ResponseEntity<>("Server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
