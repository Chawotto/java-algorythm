package org.example;

import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OptimalBST {
    private static final Logger logger = Logger.getLogger(OptimalBST.class.getName());

    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.INFO);
    }

    public static int optComparesO(int nodeCount) {
        if (nodeCount <= 0) {
            throw new IllegalArgumentException("Количество узлов должно быть больше 0.");
        }
        int height = (int) Math.ceil(Math.log(nodeCount) / Math.log(2));
        return (int) Math.ceil(height + (Math.pow(2, height) - nodeCount) / Math.pow(2, height));
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Введите количество узлов (nodeCount): ");
            int nodeCount = scanner.nextInt();

            if (nodeCount <= 0) {
                logger.severe("Ошибка: количество узлов должно быть положительным числом.");
                return;
            }

            int averageComparisons = optComparesO(nodeCount);
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Среднее количество сравнений для оптимального ДБП: %d", averageComparisons));
            }
        } catch (Exception e) {
            logger.severe("Ошибка: " + e.getMessage());
        }
    }
}
