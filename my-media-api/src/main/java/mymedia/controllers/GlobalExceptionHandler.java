package mymedia.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof SQLIntegrityConstraintViolationException) {
            return new ResponseEntity<>(
                    "Data Integrity Failure. Your request failed.",
                    HttpStatus.BAD_REQUEST);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return new ResponseEntity<>(
                    "Message Not Readable. Your request failed.",
                    HttpStatus.BAD_REQUEST);
        } else if (ex instanceof DataIntegrityViolationException e) {
            return new ResponseEntity<>(
                    "Data Integrity Failure. Your request failed.",
                    HttpStatus.BAD_REQUEST);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return new ResponseEntity<>(
                    "Media Type not supported. Your request failed.",
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else {
            ex.printStackTrace();
            return new ResponseEntity<>(
                    "Generic Failure. Error unknown.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
