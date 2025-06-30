package com.library.restapi.demo.controller.manager;

import com.library.restapi.demo.exceptions.EmptyViewListException;
import com.library.restapi.demo.exceptions.EntityDeletionNotAllowedException;
import com.library.restapi.demo.exceptions.EntityNotFound;
import com.library.restapi.demo.model.messages.ErrorResponse;
import com.library.restapi.demo.model.messages.ErrorValidationField;
import com.library.restapi.demo.model.messages.ErrorResponseValidation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFound.class, IllegalArgumentException.class,
            EmptyViewListException.class, EntityDeletionNotAllowedException.class})
    public ResponseEntity<ErrorResponse> handleBasicExs(RuntimeException ex){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Unique constraint violation";

        Throwable cause = ex.getCause();
        if (cause instanceof org.hibernate.exception.ConstraintViolationException constraintEx) {
            String constraintName = constraintEx.getConstraintName();
            if (constraintName != null) {
                message = "Unique constraint violated: " + constraintName;
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseValidation> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorValidationField> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorValidationField(err.getField(), err.getDefaultMessage()))
                .sorted(Comparator.comparing(ErrorValidationField::field)
                        .thenComparing(ErrorValidationField::message))
                .toList();

        ErrorResponseValidation response = new ErrorResponseValidation(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

}
