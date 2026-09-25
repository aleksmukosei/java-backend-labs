package ru.mukosei.model;

import java.time.Instant;

/**
 * Персонаж из characters.csv. Одна запись = одна строка файла.
 */
public record RickAndMortyCharacter(
        long id,
        String name,
        String status,
        String species,
        String type,
        String gender,
        String origin,
        String location,
        Instant created
) {

    /**
     * Возвращает копию персонажа с другим id (record неизменяемый, поэтому создаём новый объект).
     */
    public RickAndMortyCharacter withId(long newId) {
        return new RickAndMortyCharacter(newId, name, status, species, type, gender, origin, location, created);
    }
}