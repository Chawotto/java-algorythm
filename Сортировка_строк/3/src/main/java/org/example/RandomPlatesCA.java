package org.example;

import java.util.Random;
import java.util.Scanner;

public class RandomPlatesCA {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final Random RANDOM = new Random();
    private static final int LETTERS_COUNT = 3;
    private static final int DIGITS_COUNT = 3;
    private static final int ASCII_SIZE = 256;
    private static final int TEST1_PLATES = 5;
    private static final int TEST2_PLATES = 10;
    private static final int TEST3_PLATES = 15;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Сгенерировать случайные автомобильные номера");
            System.out.println("2. Тесты");
            System.out.println("3. Выйти");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите количество номеров: ");
                    int n = scanner.nextInt();
                    String[] plates = randomPlatesCA(n);
                    System.out.println("Сгенерированные номера: " + java.util.Arrays.toString(plates));
                    break;
                case 2:
                    predefinedTests();
                    break;
                case 3:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    public static String[] randomPlatesCA(int n) {
        String[] plates = new String[n];
        for (int i = 0; i < n; i++) {
            plates[i] = generateRandomPlate();
        }
        lsdSort(plates);
        return plates;
    }

    private static String generateRandomPlate() {
        StringBuilder plate = new StringBuilder();
        plate.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        for (int i = 0; i < LETTERS_COUNT; i++) {
            plate.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        for (int i = 0; i < DIGITS_COUNT; i++) {
            plate.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return plate.toString();
    }

    private static void predefinedTests() {
        System.out.println("Тесты:");
        String[] testPlates1 = randomPlatesCA(TEST1_PLATES);
        System.out.println("Тест 1: " + java.util.Arrays.toString(testPlates1));

        String[] testPlates2 = randomPlatesCA(TEST2_PLATES);
        System.out.println("Тест 2: " + java.util.Arrays.toString(testPlates2));

        String[] testPlates3 = randomPlatesCA(TEST3_PLATES);
        System.out.println("Тест 3: " + java.util.Arrays.toString(testPlates3));
    }

    private static void lsdSort(String[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        int n = a.length;
        int w = a[0].length();
        String[] aux = new String[n];

        for (int d = w - 1; d >= 0; d--) {
            int[] count = new int[ASCII_SIZE + 1];

            for (int i = 0; i < n; i++) {
                count[a[i].charAt(d) + 1]++;
            }

            for (int r = 0; r < ASCII_SIZE; r++) {
                count[r + 1] += count[r];
            }

            for (int i = 0; i < n; i++) {
                aux[count[a[i].charAt(d)]++] = a[i];
            }

            System.arraycopy(aux, 0, a, 0, n);
        }
    }
}