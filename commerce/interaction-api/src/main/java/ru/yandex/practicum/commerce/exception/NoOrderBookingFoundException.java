package ru.yandex.practicum.commerce.exception;

public class NoOrderBookingFoundException extends RuntimeException {
    public NoOrderBookingFoundException(String message) {
        super(message);
    }
}
