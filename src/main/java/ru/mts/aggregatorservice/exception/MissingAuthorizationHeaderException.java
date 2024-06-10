package ru.mts.aggregatorservice.exception;

public class MissingAuthorizationHeaderException extends CustomException{
    public MissingAuthorizationHeaderException(String code, String message) {
        super(code, message);
    }
}
