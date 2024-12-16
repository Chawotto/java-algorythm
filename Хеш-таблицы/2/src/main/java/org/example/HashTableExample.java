package org.example;

import java.util.Scanner;

public class HashTableExample {
    private static final int R = 256;
    private static final int M = 255;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите опцию:");
            System.out.println("1. Ввести строки вручную");
            System.out.println("2. Запустить тесты");
            System.out.println("3. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manualInput(scanner);
                    break;
                case 2:
                    runAutomaticTests();
                    break;
                case 3:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static void manualInput(Scanner scanner) {
        System.out.println("Введите строку:");
        String input = scanner.nextLine();

        int hash = hash(input);
        System.out.println("Хэш-значение строки \"" + input + "\": " + hash);

        System.out.println("Введите перестановку строки:");
        String permutation = scanner.nextLine();

        int permutationHash = hash(permutation);
        System.out.println("Хэш-значение перестановки \"" + permutation + "\": " + permutationHash);

        if (hash == permutationHash) {
            System.out.println("Хэш-значения совпадают, что демонстрирует коллизию.");
        } else {
            System.out.println("Хэш-значения не совпадают.");
        }
    }

    private static void runAutomaticTests() {
        String[][] testCases = {
                {"hello", "llheo"},
                {"world", "dlrow"},
                {"java", "vaja"},
                {"test", "tset"}
        };

        for (String[] testCase : testCases) {
            String input = testCase[0];
            String permutation = testCase[1];

            int hash = hash(input);
            int permutationHash = hash(permutation);

            System.out.println("Тест для строки \"" + input + "\" и перестановки \"" + permutation + "\":");
            System.out.println("Хэш-значение строки: " + hash);
            System.out.println("Хэш-значение перестановки: " + permutationHash);

            if (hash == permutationHash) {
                System.out.println("Хэш-значения совпадают, что демонстрирует коллизию.");
            } else {
                System.out.println("Хэш-значения не совпадают.");
            }
            System.out.println();
        }
    }

    private static int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = (R * hash + key.charAt(i)) % M;
        }
        return hash;
    }
}
