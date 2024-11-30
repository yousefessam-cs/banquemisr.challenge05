package com.banquemisr.challenge05.exception;

import com.banquemisr.challenge05.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String message = String.format("Resource not found: %s", ex.getMessage());
        logger.warning(() -> String.format("Resource not found: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        ErrorResponse errorResponse = new ErrorResponse("RESOURCE_NOT_FOUND", message, LocalDateTime.now(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        String message = String.format("Invalid input: %s", ex.getMessage());
        logger.warning(() -> String.format("Invalid input: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        ErrorResponse errorResponse = new ErrorResponse("INVALID_INPUT", message, LocalDateTime.now(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(UserRegistrationException ex, WebRequest request) {
        String message = String.format("User registration conflict: %s", ex.getMessage());
        logger.warning(() -> String.format("User registration conflict: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        ErrorResponse errorResponse = new ErrorResponse("USER_REGISTRATION_CONFLICT", message, LocalDateTime.now(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<ErrorResponse> handleUserLoginException(UserLoginException ex, WebRequest request) {
        String message = String.format("User login failed: %s", ex.getMessage());
        logger.warning(() -> String.format("User login failed: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        ErrorResponse errorResponse = new ErrorResponse("USER_LOGIN_FAILED", message, LocalDateTime.now(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailServiceException.class)
    public ResponseEntity<ErrorResponse> handleEmailServiceException(EmailServiceException ex, WebRequest request) {
        logger.log(Level.SEVERE, String.format("Email service error: %s. Request: %s", ex.getMessage(), request.getDescription(false)), ex);
        ErrorResponse errorResponse = new ErrorResponse("EMAIL_SERVICE_ERROR", ex.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.log(Level.SEVERE, String.format("An unexpected error occurred: %s. Request: %s", ex.getMessage(), request.getDescription(false)), ex);
        ErrorResponse errorResponse = new ErrorResponse("UNEXPECTED_ERROR", "An unexpected error occurred. Please try again later.", LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}