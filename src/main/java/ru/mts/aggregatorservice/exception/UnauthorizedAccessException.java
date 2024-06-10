package ru.mts.aggregatorservice.exception;

public class UnauthorizedAccessException extends CustomException{
    public UnauthorizedAccessException(String code, String message) {
        super(code, message);
    }
}
