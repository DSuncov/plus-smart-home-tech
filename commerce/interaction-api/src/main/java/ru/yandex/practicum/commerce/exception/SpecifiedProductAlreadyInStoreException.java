package ru.yandex.practicum.commerce.exception;

public class SpecifiedProductAlreadyInStoreException extends RuntimeException {
    public SpecifiedProductAlreadyInStoreException(String message) {
        super(message);
    }
}
