package org.example;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LS {
    private static final Logger logger = Logger.getLogger(LS.class.getName());

    public static void main(String[] args) {
        if (args.length == 0) {
            logger.severe("Не указан путь к каталогу. Пожалуйста, укажите путь как аргумент.");
            return;
        }

        String directoryPath = args[0];
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            if (logger.isLoggable(Level.INFO)) {
                logger.severe(String.format("Указанный путь не является каталогом: %s", directoryPath));
            }
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            logger.info("Каталог пуст или не удалось получить список файлов.");
            return;
        }

        List<File> fileList = new ArrayList<>(Arrays.asList(files));
        List<Comparator<File>> comparators = new ArrayList<>();

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-t":
                    comparators.add(Comparator.comparingLong(File::lastModified));
                    break;
                case "-T":
                    comparators.add(Comparator.comparingLong(File::lastModified).reversed());
                    break;
                case "-n":
                    comparators.add(Comparator.comparing(File::getName));
                    break;
                case "-N":
                    comparators.add(Comparator.comparing(File::getName).reversed());
                    break;
                case "-s":
                    comparators.add(Comparator.comparingLong(File::length));
                    break;
                case "-S":
                    comparators.add(Comparator.comparingLong(File::length).reversed());
                    break;
                default:
                    if (logger.isLoggable(Level.INFO)) {
                        logger.severe(String.format("Неверный аргумент сортировки: %s", args[i]));
                    }
                    return;
            }
        }

        comparators.forEach(fileList::sort);

        logger.info("Список файлов:");
        for (File file : fileList) {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Имя: %s, Размер: %d байт, Последнее изменение: %tc",
                        file.getName(), file.length(), file.lastModified()));
            }
        }
    }
}