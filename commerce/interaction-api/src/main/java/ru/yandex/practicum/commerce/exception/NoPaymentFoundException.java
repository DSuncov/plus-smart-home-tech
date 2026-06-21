package ru.yandex.practicum.commerce.exception;

public class NoPaymentFoundException extends RuntimeException {
    public NoPaymentFoundException(String message) {
        super(message);
    }
}
