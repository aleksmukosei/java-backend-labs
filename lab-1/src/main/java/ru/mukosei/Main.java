package ru.mukosei;

import ru.mukosei.demo.CharacterCrudDemo;
import ru.mukosei.mapper.CharacterCsvMapper;
import ru.mukosei.model.RickAndMortyCharacter;
import ru.mukosei.repository.CharacterCsvRepository;
import ru.mukosei.service.OriginStatisticsService;
import ru.mukosei.writer.OriginCountCsvWriter;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String CHARACTERS_RESOURCE = "/characters.csv";
    private static final Path RESULT_FILE = Path.of("origin-count.csv");

    public static void main(String[] args) {
        CharacterCsvRepository repository = new CharacterCsvRepository(resolveCharactersFile(), new CharacterCsvMapper());

        // 1) чтение данных из файла
        List<RickAndMortyCharacter> characters = repository.findAll();
        System.out.println("Прочитано персонажей: " + characters.size());

        // 2) обработка: подсчёт персонажей по origin
        Map<String, Long> countByOrigin = new OriginStatisticsService().countByOrigin(characters);
        System.out.println("Количество по origin: " + countByOrigin);

        // 3) запись результата в новый файл
        new OriginCountCsvWriter().write(RESULT_FILE, countByOrigin);
        System.out.println("Результат записан в " + RESULT_FILE.toAbsolutePath());

        // дополнительное задание: CRUD над исходным csv-файлом
        new CharacterCrudDemo(repository).run();
    }

    private static Path resolveCharactersFile() {
        URL resource = Main.class.getResource(CHARACTERS_RESOURCE);
        if (resource == null) {
            throw new IllegalStateException("В ресурсах не найден файл " + CHARACTERS_RESOURCE);
        }
        try {
            return Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Некорректный путь к файлу " + CHARACTERS_RESOURCE, e);
        }
    }
}