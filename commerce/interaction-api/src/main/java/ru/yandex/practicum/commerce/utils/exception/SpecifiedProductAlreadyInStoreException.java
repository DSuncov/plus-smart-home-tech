package ru.yandex.practicum.commerce.utils.exception;

public class SpecifiedProductAlreadyInStoreException extends RuntimeException {
    public SpecifiedProductAlreadyInStoreException(String message) {
        super(message);
    }
}
