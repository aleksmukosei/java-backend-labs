package ru.mukosei.exception;

/**
 * Бросается, когда персонаж с указанным id не найден в хранилище.
 */
public class CharacterNotFoundException extends RuntimeException {

    public CharacterNotFoundException(long id) {
        super("Персонаж с id=" + id + " не найден");
    }
}
