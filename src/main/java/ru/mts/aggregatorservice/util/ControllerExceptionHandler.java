package ru.mts.aggregatorservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import ru.mts.aggregatorservice.exception.CustomException;
import ru.mts.aggregatorservice.model.ExceptionData;
import ru.mts.aggregatorservice.model.ExceptionResponse;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse<ExceptionData>> handleException(CustomException e) {
        ExceptionResponse<ExceptionData> exceptionResponse =
                new ExceptionResponse<>(new ExceptionData(e.getCode(), e.getMessage()));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
