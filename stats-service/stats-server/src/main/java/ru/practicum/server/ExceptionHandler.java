package ru.practicum.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(DateValidationException exception) {
        log.warn(exception.getMessage());
        ApiError apiError = new ApiError();
        apiError.getErrors().add(Arrays.toString(exception.getStackTrace()));
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason(exception.getMessage());
        apiError.setMessage(exception.getLocalizedMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }
}