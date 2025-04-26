package org.example;

import java.util.Random;
import java.util.Scanner;

public class KMPSearch {

    private int comparisons;

    private int[] computePrefixFunction(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        int k = 0;
        for (int q = 1; q < m; q++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(q)) {
                k = pi[k - 1];
            }
            if (pattern.charAt(k) == pattern.charAt(q)) {
                k++;
            }
            pi[q] = k;
        }
        return pi;
    }

    public void search(String text, String pattern) {
        resetComparisons();
        int n = text.length();
        int m = pattern.length();
        int[] pi = computePrefixFunction(pattern);
        int q = 0;
        for (int i = 0; i < n; i++) {
            q = getQ(text, pattern, pi, q, i);
            if (q == m) {
                q = pi[q - 1];
            }
        }
    }

    private int getQ(String text, String pattern, int[] pi, int q, int i) {
        while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
            comparisons++;
            q = pi[q - 1];
        }
        comparisons++;

        if (pattern.charAt(q) == text.charAt(i)) {
            q++;
        }
        return q;
    }

    private void resetComparisons() {
        comparisons = 0;
    }

    public int getComparisons() {
        return comparisons;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        KMPSearch kmp = new KMPSearch();
        int choice = 0;

        while (choice != 3) {
            displayMenu();
            choice = scanner.nextInt();
            handleChoice(choice, scanner, random, kmp);
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("Меню:");
        System.out.println("1. Запустить эксперимент");
        System.out.println("2. Тесты");
        System.out.println("3. Выход");
        System.out.print("Выберите пункт: ");
    }

    private static void handleChoice(int choice, Scanner scanner, Random random, KMPSearch kmp) {
        switch (choice) {
            case 1 -> runExperiment(scanner, random, kmp);
            case 2 -> runTests(random, kmp);
            case 3 -> System.out.println("Выход из программы.");
            default -> System.out.println("Некорректный выбор, попробуйте снова.");
        }
    }

    private static void runExperiment(Scanner scanner, Random random, KMPSearch kmp) {
        System.out.print("Введите длину образца (M): ");
        int m = scanner.nextInt();
        System.out.print("Введите длину текста (N): ");
        int n = scanner.nextInt();
        System.out.print("Введите количество повторений (T): ");
        int t = scanner.nextInt();
        int totalComparisons = 0;

        for (int i = 0; i < t; i++) {
            String pattern = generateRandomString(m, random);
            String text = generateRandomString(n, random);
            kmp.search(text, pattern);
            totalComparisons += kmp.getComparisons();
            System.out.printf("Тест %d: Образец '%s', Текст '%s'%n", i + 1, pattern, text);
        }

        double averageComparisons = (double) totalComparisons / t;
        System.out.printf("Среднее количество сравнений за %d повторений: %.2f%n", t, averageComparisons);
    }

    private static void runTests(Random random, KMPSearch kmp) {
        System.out.println("Тест программы:");
        for (int i = 0; i < 5; i++) {
            int patternLength = 3 + random.nextInt(5);
            int textLength = 10 + random.nextInt(15);
            String pattern = generateRandomString(patternLength, random);
            String text = generateRandomString(textLength, random);
            kmp.search(text, pattern);
            System.out.printf("Тест %d: Образец '%s', Текст '%s', Количество сравнений: %d%n",
                    i + 1, pattern, text, kmp.getComparisons());
        }
    }

    private static String generateRandomString(int length, Random random) {
        StringBuilder sb = new StringBuilder(length);
        for (int j = 0; j < length; j++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }
}