package org.example;

import java.util.Random;
import java.util.Scanner;

public class RandomPlatesCA {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final Random RANDOM = new Random();

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
        for (int i = 0; i < 3; i++) {
            plate.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        for (int i = 0; i < 3; i++) {
            plate.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return plate.toString();
    }

    private static void predefinedTests() {
        System.out.println("Тесты:");
        String[] testPlates1 = randomPlatesCA(5);
        System.out.println("Тест 1: " + java.util.Arrays.toString(testPlates1));

        String[] testPlates2 = randomPlatesCA(10);
        System.out.println("Тест 2: " + java.util.Arrays.toString(testPlates2));

        String[] testPlates3 = randomPlatesCA(15);
        System.out.println("Тест 3: " + java.util.Arrays.toString(testPlates3));
    }

    private static void lsdSort(String[] a) {
        int n = a.length;
        int w = a[0].length();
        String[] aux = new String[n];

        for (int d = w - 1; d >= 0; d--) {
            int[] count = new int[256 + 1];

            for (int i = 0; i < n; i++) {
                count[a[i].charAt(d) + 1]++;
            }

            for (int r = 0; r < 256; r++) {
                count[r + 1] += count[r];
            }

            for (int i = 0; i < n; i++) {
                aux[count[a[i].charAt(d)]++] = a[i];
            }

            System.arraycopy(aux, 0, a, 0, n);
        }
    }
}
