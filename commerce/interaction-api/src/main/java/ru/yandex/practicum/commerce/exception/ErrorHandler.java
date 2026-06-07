package ru.yandex.practicum.commerce.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException e) {
        return createResponse(e, HttpStatus.NOT_FOUND, "Обработана ошибка: NoProductsInShoppingCartException");
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException e) {
        return createResponse(e, HttpStatus.NOT_FOUND, "Обработана ошибка: NoSpecifiedProductInWarehouseException");
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        return createResponse(e, HttpStatus.UNAUTHORIZED, "Обработана ошибка: NotAuthorizedUserException");
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartLowQuantityInWarehouse(ProductInShoppingCartLowQuantityInWarehouse e) {
        return createResponse(e, HttpStatus.BAD_REQUEST, "Обработана ошибка: ProductInShoppingCartLowQuantityInWarehouse");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundException(ProductNotFoundException e) {
        return createResponse(e, HttpStatus.NOT_FOUND, "Обработана ошибка: ProductNotFoundException");
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpecifiedProductAlreadyInWarehouseException(SpecifiedProductAlreadyInWarehouseException e) {
        return createResponse(e, HttpStatus.BAD_REQUEST, "Обработана ошибка: SpecifiedProductAlreadyInWarehouseException");
    }

    private ErrorResponse createResponse(Exception e, HttpStatus httpStatus, String message) {
        Throwable cause = e.getCause();
        StackTraceElement[] stackTraceElements = cause.getStackTrace();

        List<ErrorResponse.StackTrace> stackTraces = new ArrayList<>();

        for (StackTraceElement stackTraceElement : stackTraceElements) {
            stackTraces.add(new ErrorResponse.StackTrace(
                    stackTraceElement.getClassLoaderName(),
                    stackTraceElement.getModuleName(),
                    stackTraceElement.getModuleVersion(),
                    stackTraceElement.getFileName(),
                    (long) stackTraceElement.getLineNumber(),
                    stackTraceElement.getClassName(),
                    stackTraceElement.isNativeMethod()));
        }

        ErrorResponse.Cause causeErrorResponse = new ErrorResponse.Cause(stackTraces, message, e.getLocalizedMessage());

        return new ErrorResponse(
                causeErrorResponse,
                stackTraces,
                httpStatus,
                message,
                e.getMessage(),
                List.of(causeErrorResponse),
                e.getLocalizedMessage());
    }
}
