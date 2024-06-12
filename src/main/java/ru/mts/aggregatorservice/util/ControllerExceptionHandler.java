package ru.mts.aggregatorservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import ru.mts.aggregatorservice.exception.CustomException;
import ru.mts.aggregatorservice.model.ExceptionData;
import ru.mts.aggregatorservice.model.ExceptionResponse;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Обрабатывает исключения, которые связаны с ошибкой при авторизации в системе
     *
     * @param e исключение, которое необходимо обработать
     * @return ResponseEntity с информацией об ошибке и статусом FORBIDDEN
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse<ExceptionData>> handleAuthorizationException(CustomException e) {
        ExceptionResponse<ExceptionData> exceptionResponse =
                new ExceptionResponse<>(new ExceptionData(e.getCode(), e.getMessage()));

        log.error("Произошла ошибка: {}, Код ошибки: {}, Сообщение ошибки: {}", e.getClass().getSimpleName(), e.getCode(), e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Обрабатывает исключения, которые связаны с запросом на несуществующий end-point
     *
     * @param e исключение, которое необходимо обработать
     * @return ResponseEntity с информацией об ошибке и статусом NOT_FOUND
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse<ExceptionData>> handleEndpointException(NoResourceFoundException e) {
        ExceptionResponse<ExceptionData> exceptionResponse =
                new ExceptionResponse<>(new ExceptionData(
                        "ENDPOINT_NOT_FOUND",
                        "Попытка запроса на несуществующий end-point")
                );

        log.error("Произошла ошибка: {}, Сообщение ошибки: {}", e.getClass().getSimpleName(), e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
