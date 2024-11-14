package org.example;

import java.util.*;
import java.util.logging.Logger;

public class Frequency {
    private static final Logger logger = Logger.getLogger(Frequency.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> frequencyMap = new HashMap<>();

        logger.info("Введите строки (по одной на строке). Введите \"exit\" для завершения ввода и вывода результата.");

        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                break;
            }
            frequencyMap.put(line, frequencyMap.getOrDefault(line, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedEntries = frequencyMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    int freqCompare = Integer.compare(entry2.getValue(), entry1.getValue());
                    if (freqCompare == 0) {
                        return entry1.getKey().compareTo(entry2.getKey());
                    }
                    return freqCompare;
                })
                .toList();

        logger.info("Результаты (по убыванию частоты):");

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            logger.info(entry.getKey() + ": " + entry.getValue());
        }
    }
}
