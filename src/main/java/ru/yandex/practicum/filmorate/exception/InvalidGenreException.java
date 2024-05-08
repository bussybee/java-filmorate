package ru.yandex.practicum.filmorate.exception;

public class InvalidGenreException extends RuntimeException {
    public InvalidGenreException(String message) {
        super(message);
    }
}
