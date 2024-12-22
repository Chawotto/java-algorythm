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
            k = getQ(pattern, pattern, pi, k, q);
            pi[q] = k;
        }
        return pi;
    }

    public void search(String text, String pattern) {
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
            q = pi[q - 1];
            comparisons++;
        }
        if (pattern.charAt(q) == text.charAt(i)) {
            q++;
            comparisons++;
        }
        return q;
    }

    public void resetComparisons() {
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
            StringBuilder pattern = generateRandomString(m, random);
            StringBuilder text = generateRandomString(n, random);
            kmp.resetComparisons();
            kmp.search(text.toString(), pattern.toString());
            totalComparisons += kmp.getComparisons();
        }

        double averageComparisons = (double) totalComparisons / t;
        System.out.printf("Среднее количество сравнений за %d повторений: %.2f%n", t, averageComparisons);
    }

    private static void runTests(Random random, KMPSearch kmp) {
        System.out.println("Тест программы:");
        for (int i = 0; i < 5; i++) {
            StringBuilder randomPattern = generateRandomString(3 + random.nextInt(5), random);
            StringBuilder randomText = generateRandomString(10 + random.nextInt(15), random);
            kmp.resetComparisons();
            kmp.search(randomText.toString(), randomPattern.toString());
            System.out.printf("Тест %d: Образец '%s', Текст '%s', Количество сравнений: %d%n", i + 1, randomPattern, randomText, kmp.getComparisons());
        }
    }

    private static StringBuilder generateRandomString(int length, Random random) {
        StringBuilder sb = new StringBuilder(length);
        for (int j = 0; j < length; j++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb;
    }
}
