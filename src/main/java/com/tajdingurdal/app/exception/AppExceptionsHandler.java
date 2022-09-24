package com.tajdingurdal.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.Instant;

@ControllerAdvice // for this class to be able to handle exceptions then it need this special annotation.
public class AppExceptionsHandler {

    // handler just UserServiceException
    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(Instant.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // handler all Exception
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGlobalExceptions(Exception ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(Instant.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
