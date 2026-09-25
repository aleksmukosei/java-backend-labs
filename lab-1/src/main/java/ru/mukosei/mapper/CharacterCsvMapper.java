package ru.mukosei.mapper;

import ru.mukosei.model.RickAndMortyCharacter;

import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * Преобразует строку CSV в объект персонажа и обратно.
 */
public class CharacterCsvMapper {

    public static final String HEADER = "id,name,status,species,type,gender,origin/name,location/name,created";

    private static final String SEPARATOR = ",";
    private static final int COLUMNS_COUNT = 9;

    public RickAndMortyCharacter fromCsvLine(String line) {
        // limit = -1 обязателен: иначе split выбросит пустые значения в конце строки
        String[] columns = line.split(SEPARATOR, -1);
        if (columns.length != COLUMNS_COUNT) {
            throw new IllegalArgumentException(
                    "Ожидалось " + COLUMNS_COUNT + " колонок, получено " + columns.length + ": " + line);
        }
        try {
            return new RickAndMortyCharacter(
                    Long.parseLong(columns[0]),
                    columns[1],
                    columns[2],
                    columns[3],
                    columns[4],
                    columns[5],
                    columns[6],
                    columns[7],
                    Instant.parse(columns[8])
            );
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректная строка CSV: " + line, e);
        }
    }

    public String toCsvLine(RickAndMortyCharacter character) {
        return String.join(SEPARATOR,
                String.valueOf(character.id()),
                character.name(),
                character.status(),
                character.species(),
                character.type(),
                character.gender(),
                character.origin(),
                character.location(),
                character.created().toString()
        );
    }
}