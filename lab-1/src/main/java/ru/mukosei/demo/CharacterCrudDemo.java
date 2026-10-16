package ru.mukosei.demo;

import ru.mukosei.model.RickAndMortyCharacter;
import ru.mukosei.repository.CharacterCsvRepository;

import java.time.Instant;

/**
 * Демонстрация дополнительного задания: CRUD-операции над исходным csv-файлом.
 * В конце созданный персонаж удаляется, поэтому файл возвращается в исходное состояние.
 */
public class CharacterCrudDemo {

    private final CharacterCsvRepository repository;

    public CharacterCrudDemo(CharacterCsvRepository repository) {
        this.repository = repository;
    }

    public void run() {
        System.out.println("=== CRUD ===");
        System.out.println("Персонажей в файле: " + repository.findAll().size());

        // Create
        RickAndMortyCharacter newCharacter = new RickAndMortyCharacter(
                0, "Mr. Poopybutthole", "Alive", "Poopybutthole", "",
                "Male", "unknown", "Earth (Replacement Dimension)", Instant.now());
        RickAndMortyCharacter created = repository.create(newCharacter);
        System.out.println("Create: " + created);
        System.out.println("Персонажей в файле: " + repository.findAll().size());

        // Read
        RickAndMortyCharacter found = repository.findById(created.id())
                .orElseThrow(() -> new IllegalStateException("Только что созданный персонаж не найден"));
        System.out.println("Read:   " + found);

        // Update
        RickAndMortyCharacter updated = new RickAndMortyCharacter(
                found.id(), found.name(), "Dead", found.species(), found.type(),
                found.gender(), found.origin(), "Citadel of Ricks", found.created());
        repository.update(updated);
        System.out.println("Update: " + repository.findById(updated.id()).orElseThrow());

        // Delete
        repository.deleteById(created.id());
        System.out.println("Delete: найден после удаления? " + repository.findById(created.id()).isPresent());
        System.out.println("Персонажей в файле: " + repository.findAll().size());
    }
}
