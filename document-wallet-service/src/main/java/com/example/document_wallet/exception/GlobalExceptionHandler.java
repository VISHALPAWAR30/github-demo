package com.example.document_wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleStatus(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(Map.of(
                        "error", ex.getReason()
                ));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Map.of(
//                        "error", "Internal Server Error"
//                ));
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleEmptyBody() {
        return ResponseEntity
                .badRequest()
                .body("No data found!!");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }





}
