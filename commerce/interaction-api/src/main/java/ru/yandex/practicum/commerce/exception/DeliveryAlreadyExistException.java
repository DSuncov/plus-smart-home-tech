package ru.yandex.practicum.commerce.exception;

public class DeliveryAlreadyExistException extends RuntimeException {
    public DeliveryAlreadyExistException(String message) {
        super(message);
    }
}
