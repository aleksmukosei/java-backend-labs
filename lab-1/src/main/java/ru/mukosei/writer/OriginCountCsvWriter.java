package ru.mukosei.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Записывает результат подсчёта в csv-файл формата "origin,count".
 */
public class OriginCountCsvWriter {

    private static final String HEADER = "origin,count";

    public void write(Path resultFile, Map<String, Long> countByOrigin) {
        // HashMap не гарантирует порядок, поэтому для читаемого результата
        // сортируем записи: сначала по убыванию количества, при равенстве - по названию
        List<Map.Entry<String, Long>> entries = new ArrayList<>(countByOrigin.entrySet());
        entries.sort(Map.Entry.<String, Long>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()));

        try (BufferedWriter writer = Files.newBufferedWriter(resultFile, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            for (Map.Entry<String, Long> entry : entries) {
                writer.newLine();
                writer.write(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи результата в файл " + resultFile + ": " + e.getMessage());
            throw new UncheckedIOException(e);
        }
    }
}