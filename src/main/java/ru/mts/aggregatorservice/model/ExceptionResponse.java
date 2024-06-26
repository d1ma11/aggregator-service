package ru.mts.aggregatorservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse<T> {
    private T error;
}
