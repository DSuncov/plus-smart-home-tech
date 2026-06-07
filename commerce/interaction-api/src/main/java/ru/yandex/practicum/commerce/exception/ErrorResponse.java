package ru.yandex.practicum.commerce.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
    Cause cause,
    List<StackTrace> stackTraces,
    HttpStatus httpStatus,
    String userMessage,
    String message,
    List<Cause> suppressed,
    String localizedMessage
) {
    public record Cause(
            List<StackTrace> stackTrace,
            String message,
            String localizedMessage
    ) {}

    public record StackTrace(
            String classLoaderName,
            String moduleName,
            String moduleVersion,
            String fileName,
            Long lineNumber,
            String className,
            Boolean nativeMethod
    ) {}
}
