package ru.mukosei.service;

import ru.mukosei.model.RickAndMortyCharacter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Вариант 10: подсчёт количества персонажей по месту происхождения (origin/name).
 */
public class OriginStatisticsService {

    public Map<String, Long> countByOrigin(List<RickAndMortyCharacter> characters) {

        Map<String, Long> countByOrigin = new HashMap<>();
        for (RickAndMortyCharacter character : characters) {

            countByOrigin.merge(character.origin(), 1L, Long::sum);
        }
        return countByOrigin;
    }
}