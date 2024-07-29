package com.example.stage7.CRUD.Chart.controller;

import com.example.stage7.CRUD.Chart.exception.ChartNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChartNotFoundException.class)
    public ResponseEntity<String> handleChartNotFoundException(ChartNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
