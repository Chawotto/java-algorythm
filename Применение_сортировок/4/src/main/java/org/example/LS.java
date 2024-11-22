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
        showMenu(new Scanner(System.in), fileList);

        if (logger.isLoggable(Level.INFO)) {
            logger.info("Список файлов:");
            for (File file : fileList) {
                logger.info(String.format("Имя: %s, Размер: %d байт, Последнее изменение: %tc",
                        file.getName(), file.length(), file.lastModified()));
            }
        }
    }

    private static void showMenu(Scanner scanner, List<File> files) {
        logger.info("Выберите параметр сортировки:");
        logger.info("1. По имени (возрастание)");
        logger.info("2. По имени (убывание)");
        logger.info("3. По размеру (возрастание)");
        logger.info("4. По размеру (убывание)");
        logger.info("5. По дате изменения (возрастание)");
        logger.info("6. По дате изменения (убывание)");
        logger.info("Ваш выбор (1-6): ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                files.sort(Comparator.comparing(File::getName));
                break;
            case 2:
                files.sort(Comparator.comparing(File::getName).reversed());
                break;
            case 3:
                files.sort(Comparator.comparingLong(File::length));
                break;
            case 4:
                files.sort(Comparator.comparingLong(File::length).reversed());
                break;
            case 5:
                files.sort(Comparator.comparingLong(File::lastModified));
                break;
            case 6:
                files.sort(Comparator.comparingLong(File::lastModified).reversed());
                break;
            default:
                logger.severe("Неверный выбор. Сортировка не выполнена.");
                return;
        }
    }
}