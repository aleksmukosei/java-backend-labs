package ru.mukosei.repository;

import ru.mukosei.exception.CharacterNotFoundException;
import ru.mukosei.mapper.CharacterCsvMapper;
import ru.mukosei.model.RickAndMortyCharacter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Хранилище персонажей поверх csv-файла: чтение и CRUD-операции.
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

    /**
     * Ищет персонажа по id. Если не найден - возвращает пустой Optional вместо null.
     */
    public Optional<RickAndMortyCharacter> findById(long id) {
        for (RickAndMortyCharacter character : findAll()) {
            if (character.id() == id) {
                return Optional.of(character);
            }
        }
        return Optional.empty();
    }

    /**
     * Добавляет персонажа в конец файла. id назначается автоматически: максимальный существующий + 1.
     */
    public RickAndMortyCharacter create(RickAndMortyCharacter newCharacter) {
        List<RickAndMortyCharacter> characters = findAll();
        long nextId = 1;
        for (RickAndMortyCharacter character : characters) {
            nextId = Math.max(nextId, character.id() + 1);
        }
        RickAndMortyCharacter created = newCharacter.withId(nextId);
        characters.add(created);
        saveAll(characters);
        return created;
    }

    /**
     * Заменяет персонажа с тем же id. Если такого нет - CharacterNotFoundException.
     */
    public void update(RickAndMortyCharacter updatedCharacter) {
        List<RickAndMortyCharacter> characters = findAll();
        int index = indexOf(characters, updatedCharacter.id());
        characters.set(index, updatedCharacter);
        saveAll(characters);
    }

    /**
     * Удаляет персонажа по id. Если такого нет - CharacterNotFoundException.
     */
    public void deleteById(long id) {
        List<RickAndMortyCharacter> characters = findAll();
        int index = indexOf(characters, id);
        characters.remove(index);
        saveAll(characters);
    }

    private int indexOf(List<RickAndMortyCharacter> characters, long id) {
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).id() == id) {
                return i;
            }
        }
        throw new CharacterNotFoundException(id);
    }

    /**
     * CSV нельзя поправить посередине, поэтому файл перезаписывается целиком: заголовок + все персонажи.
     */
    private void saveAll(List<RickAndMortyCharacter> characters) {
        try (BufferedWriter writer = Files.newBufferedWriter(csvFile, StandardCharsets.UTF_8)) {
            writer.write(CharacterCsvMapper.HEADER);
            for (RickAndMortyCharacter character : characters) {
                writer.newLine();
                writer.write(mapper.toCsvLine(character));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла " + csvFile + ": " + e.getMessage());
            throw new UncheckedIOException(e);
        }
    }
}
