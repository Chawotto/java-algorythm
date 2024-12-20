package org.example;

import java.util.*;

public class MSDStringSort {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите опцию:");
        System.out.println("1. Ввести строки для сортировки");
        System.out.println("2. Запустить заготовленные тесты");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                inputStringsAndSort(scanner);
                break;
            case 2:
                runPredefinedTests();
                break;
            default:
                System.out.println("Неверный выбор. Пожалуйста, выберите 1 или 2.");
        }
    }

    private static void inputStringsAndSort(Scanner scanner) {
        System.out.println("Введите строки для сортировки (по одной строке на строку, пустая строка для завершения ввода):");

        List<String> inputStrings = new ArrayList<>();
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            inputStrings.add(line);
        }

        List<String> sortedStrings = msdStringSort(inputStrings);
        System.out.println("Отсортированные строки:");
        for (String str : sortedStrings) {
            System.out.println(str);
        }
    }

    private static void runPredefinedTests() {
        System.out.println("\nЗаготовленные тесты:");

        runTest(Arrays.asList("apple", "banana", "apricot", "blueberry", "avocado"),
                Arrays.asList("apple", "apricot", "avocado", "banana", "blueberry"), "Тест 1");

        runTest(Arrays.asList("cat", "dog", "bat", "ant", "elephant"),
                Arrays.asList("ant", "bat", "cat", "dog", "elephant"), "Тест 2");

        runTest(Arrays.asList("123", "12", "1", "1234", "12345"),
                Arrays.asList("1", "12", "123", "1234", "12345"), "Тест 3");
    }

    private static void runTest(List<String> input, List<String> expected, String testName) {
        System.out.println(testName + ":");
        System.out.println("Исходные строки: " + input);
        List<String> result = msdStringSort(input);
        System.out.println("Отсортированные строки: " + result);
        System.out.println("Ожидаемые строки: " + expected);
        System.out.println("Результат: " + (result.equals(expected) ? "Пройден" : "Не пройден"));
        System.out.println();
    }

    public static List<String> msdStringSort(List<String> strings) {
        Queue<String>[] queues = new Queue[256];
        for (int i = 0; i < 256; i++) {
            queues[i] = new LinkedList<>();
        }

        for (String str : strings) {
            queues[str.charAt(0)].add(str);
        }

        List<String> result = new ArrayList<>();
        for (Queue<String> queue : queues) {
            if (!queue.isEmpty()) {
                List<String> sublist = new ArrayList<>(queue);
                result.addAll(msdStringSort(sublist, 1));
            }
        }

        return result;
    }

    private static List<String> msdStringSort(List<String> strings, int d) {
        if (strings.size() <= 1) {
            return strings;
        }

        Queue<String>[] queues = new Queue[256];
        for (int i = 0; i < 256; i++) {
            queues[i] = new LinkedList<>();
        }

        for (String str : strings) {
            if (d >= str.length()) {
                queues[0].add(str);
            } else {
                queues[str.charAt(d)].add(str);
            }
        }

        List<String> result = new ArrayList<>();
        for (Queue<String> queue : queues) {
            if (!queue.isEmpty()) {
                List<String> sublist = new ArrayList<>(queue);
                result.addAll(msdStringSort(sublist, d + 1));
            }
        }

        return result;
    }
}
