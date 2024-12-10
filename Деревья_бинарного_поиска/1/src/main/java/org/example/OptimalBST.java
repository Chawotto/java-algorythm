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

    public static float optComparesO(int nodeCount) {
        if (nodeCount <= 0) {
            throw new IllegalArgumentException("Количество узлов должно быть больше 0.");
        }

        double height = Math.log(nodeCount);

        return (float) (height / Math.log(2));
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Введите количество узлов (nodeCount): ");
            int nodeCount = scanner.nextInt();

            if (nodeCount <= 0) {
                logger.severe("Ошибка: количество узлов должно быть положительным числом.");
                return;
            }

            float averageComparisons = optComparesO(nodeCount);
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Среднее количество сравнений для оптимального ДБП: %f", averageComparisons));
            }
        } catch (Exception e) {
            logger.severe("Ошибка: " + e.getMessage());
        }
    }
}