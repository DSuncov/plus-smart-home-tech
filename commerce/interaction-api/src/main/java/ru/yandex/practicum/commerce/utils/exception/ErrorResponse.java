package ru.yandex.practicum.commerce.utils.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
    Throwable cause,
    StackTraceElement[] stackTrace,
    HttpStatus httpStatus,
    String userMessage,
    String message,
    Throwable[] suppressed,
    String localizedMessage
) {}
