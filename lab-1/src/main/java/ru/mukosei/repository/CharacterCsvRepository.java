package ru.mukosei.repository;

import ru.mukosei.mapper.CharacterCsvMapper;
import ru.mukosei.model.RickAndMortyCharacter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище персонажей поверх csv-файла.
 */
public class CharacterCsvRepository {

    private final Path csvFile;
    private final CharacterCsvMapper mapper;

    public CharacterCsvRepository(Path csvFile, CharacterCsvMapper mapper) {
        this.csvFile = csvFile;
        this.mapper = mapper;
    }

    public List<RickAndMortyCharacter> findAll() {
        List<RickAndMortyCharacter> characters = new ArrayList<>();
        // BufferedReader читает файл построчно, try-with-resources гарантирует закрытие файла
        try (BufferedReader reader = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            // первая строка - заголовок, пропускаем её
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    characters.add(mapper.fromCsvLine(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла " + csvFile + ": " + e.getMessage());
            // без исходных данных программа продолжать не может
            throw new UncheckedIOException(e);
        }
        return characters;
    }
}