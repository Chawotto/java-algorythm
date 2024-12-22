package org.example;

import java.util.Scanner;

public class CyclicPermutationChecker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Меню:");
            System.out.println("1. Проверить, является ли одна строка циклической перестановкой другой");
            System.out.println("2. Тесты");
            System.out.println("3. Выйти");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    checkCyclicPermutation(scanner);
                    break;
                case 2:
                    runTests();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        }

        scanner.close();
    }

    private static void checkCyclicPermutation(Scanner scanner) {
        System.out.print("Введите первую строку: ");
        String str1 = scanner.nextLine();
        System.out.print("Введите вторую строку: ");
        String str2 = scanner.nextLine();

        if (isCyclicPermutation(str1, str2)) {
            System.out.println("Строка \"" + str2 + "\" ЯВЛЯЕТСЯ циклической перестановкой строки \"" + str1 + "\".");
        } else {
            System.out.println("Строка \"" + str2 + "\" НЕ является циклической перестановкой строки \"" + str1 + "\".");
        }
    }

    private static boolean isCyclicPermutation(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }
        String concatenated = str1 + str1;
        return concatenated.contains(str2);
    }

    private static void runTests() {
        System.out.println("Запуск тестов:");
        String[][] testCases = {
                {"пальто", "топаль"},
                {"abcde", "cdeab"},
                {"hello", "llohe"},
                {"java", "vaja"},
                {"test", "tset"},
                {"abc", "cba"}
        };

        for (String[] testCase : testCases) {
            String str1 = testCase[0];
            String str2 = testCase[1];
            boolean result = isCyclicPermutation(str1, str2);
            System.out.println("Тест: \"" + str1 + "\" и \"" + str2 + "\" -> " + result);
        }
    }
}
