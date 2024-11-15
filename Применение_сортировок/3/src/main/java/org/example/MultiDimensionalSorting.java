package org.example;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiDimensionalSorting {

    private static final Logger logger = Logger.getLogger(MultiDimensionalSorting.class.getName());

    public static class Vector implements Comparable<Vector> {
        private final int[] components;

        public Vector(int[] components) {
            this.components = components;
        }

        public int getDimension() {
            return components.length;
        }

        @Override
        public int compareTo(Vector other) {
            int minDimension = Math.min(this.getDimension(), other.getDimension());
            for (int i = 0; i < minDimension; i++) {
                if (this.components[i] != other.components[i]) {
                    return Integer.compare(this.components[i], other.components[i]);
                }
            }
            return Integer.compare(this.getDimension(), other.getDimension());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Vector other = (Vector) obj;
            return Arrays.equals(this.components, other.components);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(components);
        }

        @Override
        public String toString() {
            return Arrays.toString(components);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Vector> vectors = new ArrayList<>();

        logger.info("Введите векторы в формате '1 2 3' .");
        logger.info("Для завершения ввода введите 'exit'.");

        while (true) {
            logger.info("Введите вектор: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int[] components = Arrays.stream(input.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                vectors.add(new Vector(components));
            } catch (NumberFormatException e) {
                logger.warning("Ошибка ввода. Убедитесь, что вы ввели только целые числа, разделенные пробелами.");
            }
        }

        vectors.sort(null);

        if (logger.isLoggable(Level.INFO)) {
            logger.info("\nОтсортированные векторы:");
            for (Vector vector : vectors) {
                logger.info(vector.toString());
            }
        }
    }
}
