package com.example.spacelab.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
@Log
public class GlobalControllerAdvice {

//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//    public ResponseEntity<ErrorMessage> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex
//    ) {
//        String unsupported = "Unsupported content type: " + ex.getContentType();
//        String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
//        ErrorMessage errorMessage = new ErrorMessage(unsupported, supported);
//        return new ResponseEntity<>(errorMessage, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
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

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> tokenExceptionHandler(TokenException e) {
        return new ResponseEntity<>(
                new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST.value(), Map.of("token", e.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> fallbackInternalErrorHandler(Exception ex) {
        log.severe(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>("Unknown server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
//        Throwable mostSpecificCause = ex.getMostSpecificCause();
//        ErrorMessage errorMessage;
//        String exceptionName = mostSpecificCause.getClass().getName();
//        String message = mostSpecificCause.getMessage();
//        errorMessage = new ErrorMessage(exceptionName, message);
//        return new ResponseEntity<>(errorMessage,  HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
//    ) {
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
//        List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
//        String error;
//        for (FieldError fieldError : fieldErrors) {
//            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
//            errors.add(error);
//        }
//        for (ObjectError objectError : globalErrors) {
//            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
//            errors.add(error);
//        }
//        ErrorMessage errorMessage = new ErrorMessage(errors);
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorMessage> handleConstraintViolatedException(ConstraintViolationException ex
//    ) {
//        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
//        List<String> errors = new ArrayList<>(constraintViolations.size() );
//        String error;
//        for (ConstraintViolation constraintViolation : constraintViolations) {
//            error =  constraintViolation.getMessage();
//            errors.add(error);
//        }
//        ErrorMessage errorMessage = new ErrorMessage(errors);
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex
//    ) {
//        List<String> errors = new ArrayList<>( );
//        String error=ex.getParameterName()+", "+ex.getMessage();
//        errors.add(error);
//        ErrorMessage errorMessage = new ErrorMessage(errors);
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//    }
}
