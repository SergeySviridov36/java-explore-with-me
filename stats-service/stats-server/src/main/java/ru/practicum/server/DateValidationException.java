package ru.practicum.server;

public class DateValidationException extends RuntimeException {
    public DateValidationException(String message) {
        super(message);
    }
}