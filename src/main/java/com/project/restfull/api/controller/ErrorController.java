package com.project.restfull.api.controller;

import com.project.restfull.api.pojo.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException e) {
        WebResponse<String> webResponse = new WebResponse<>();
        webResponse.setErrors(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(webResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException e) {
        WebResponse<String> response = new WebResponse<>();
        response.setErrors(e.getMessage());
        return ResponseEntity.status(e.getStatus())
                .body(response);
    }
}
