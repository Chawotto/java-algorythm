package org.example;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LS {
    private static final Logger logger = Logger.getLogger(LS.class.getName());

    public static void main(String[] args) {
        if (args.length < 1) {
            logger.severe("Укажите путь к каталогу.");
            return;
        }

        String directoryPath = args[0];
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(String.format("Указанный путь не является каталогом: %s", directoryPath));
            }
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            logger.info("Каталог пуст или не удалось получить список файлов.");
            return;
        }

        List<String> sortParams = Arrays.asList(args).subList(1, args.length);

        List<File> fileList = new ArrayList<>(Arrays.asList(files));
        sortFiles(fileList, sortParams);

        // Выводим отсортированные файлы
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Список файлов:");
            for (File file : fileList) {
                logger.info(String.format("Имя: %s, Размер: %d байт, Последнее изменение: %tc",
                        file.getName(), file.length(), file.lastModified()));
            }
        }
    }

    // Сортировка файлов на основе параметров
    private static void sortFiles(List<File> files, List<String> sortParams) {
        // Создаем комбинированный компаратор
        Comparator<File> comparator = Comparator.comparing(File::getName); // По умолчанию - имя

        for (String param : sortParams) {
            switch (param) {
                case "-s": // По размеру (возрастание)
                    comparator = comparator.thenComparing(Comparator.comparingLong(File::length));
                    break;
                case "-S": // По размеру (убывание)
                    comparator = comparator.thenComparing(Comparator.comparingLong(File::length).reversed());
                    break;
                case "-t": // По дате изменения (возрастание)
                    comparator = comparator.thenComparing(Comparator.comparingLong(File::lastModified));
                    break;
                case "-T": // По дате изменения (убывание)
                    comparator = comparator.thenComparing(Comparator.comparingLong(File::lastModified).reversed());
                    break;
                case "-n": // По имени (возрастание)
                    comparator = comparator.thenComparing(File::getName);
                    break;
                case "-N": // По имени (убывание)
                    comparator = comparator.thenComparing(File::getName, Comparator.reverseOrder());
                    break;
                default:
                    if (logger.isLoggable(Level.SEVERE)) {
                        logger.severe(String.format("Неизвестный параметр сортировки: %s", param));
                    }
            }
        }

        files.sort(comparator);
    }
}
