package org.example;

import java.util.*;

public class StringSort {

    private static final String[] FIXED_STRINGS_4 = {"ABCD", "EFGH", "IJKL", "MNOP", "QRST", "UVWX", "YZ12", "3456", "7890", "WXYZ"};
    private static final String[] FIXED_STRINGS_10 = {"ABCDEFGHIJ", "KLMNOPQRST", "UVWXYZ1234", "567890WXYZ", "QWERTYUIOP", "ASDFGHJKLZ", "ZXCVBNMASD", "QAZWSXEDCR", "RFVTGBYHNU", "JMKLOIPQAZ",
            "PLMNOKIJUH", "BVGFCDRETY", "UJHYGTFRDS", "QAZXSWEDCV", "1234567890", "0987654321", "POLKIMNBVC", "ZAQWSXEDCR", "FVGBHNJMKL", "UIOPLKJHGF",
            "WERASDFGHJ", "TYUIOPLKJH", "FGHJZXCVBN", "MNBVCXWERT", "YUIOKPLMNB", "OKJHGFDSWE", "ZXCVBNMASD", "PLKJHGFDSA", "WERTYUIOPQ", "CVBNMASDFGH",
            "QWERTZXCVBN", "ASDFGHJKLMN", "ZXCVBNMSDFG", "ERTYUIKJHGF", "QWERTYZXCVB", "PLMNBVCXZAS", "QAZXSDEWCRF", "VTGBYHNUMKI", "POLIKUJMYHB", "JNHBGVTFDCR",
            "WSXQAZERTYU", "WSXEDCFTGBH", "UJNHBGVTFCDR", "ZXCVBNMASDFF", "QAZXSWECDRTF", "VGBYHNUJMKIO", "ZXCASDFRTGHB", "NMJIKLOPERAS", "QAZXSWEFRTGH"};

    private static final char[] ONE_CHAR_VALUES = {'A', 'B'};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Меню:");
            System.out.println("1. Генерация массива строк и их сортировка");
            System.out.println("2. Тесты");
            System.out.println("3. Выход");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите количество строк: ");
                    int n = scanner.nextInt();
                    scanner.nextLine();
                    String[] randomStrings = randomItems(n);
                    System.out.println("Сгенерированные строки:");
                    for (String s : randomStrings) {
                        System.out.println(s);
                    }

                    msdSort(randomStrings);

                    System.out.println("Отсортированные строки:");
                    for (String s : randomStrings) {
                        System.out.println(s);
                    }
                    break;
                case 2:
                    runTests();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        scanner.close();
    }

    public static String[] randomItems(int n) {
        Random random = new Random();
        String[] result = new String[n];

        for (int i = 0; i < n; i++) {
            String part1 = FIXED_STRINGS_4[random.nextInt(FIXED_STRINGS_4.length)];
            String part2 = FIXED_STRINGS_10[random.nextInt(FIXED_STRINGS_10.length)];
            char part3 = ONE_CHAR_VALUES[random.nextInt(ONE_CHAR_VALUES.length)];

            int randomLength = 4 + random.nextInt(12);
            StringBuilder part4 = new StringBuilder();
            for (int j = 0; j < randomLength; j++) {
                part4.append((char) ('a' + random.nextInt(26)));
            }

            result[i] = part1 + part2 + part3 + part4;
        }

        return result;
    }

    public static void msdSort(String[] array) {
        String[] aux = new String[array.length];
        msdSort(array, aux, 0, array.length - 1, 0);
    }

    private static void msdSort(String[] array, String[] aux, int low, int high, int d) {
        if (low >= high) {
            return;
        }

        int i1 = 256; // ASCII range
        int[] count = new int[i1 + 2];

        for (int i = low; i <= high; i++) {
            int c = charAt(array[i], d);
            count[c + 2]++;
        }

        for (int r = 0; r < i1 + 1; r++) {
            count[r + 1] += count[r];
        }

        for (int i = low; i <= high; i++) {
            int c = charAt(array[i], d);
            aux[count[c + 1]++] = array[i];
        }

        System.arraycopy(aux, 0, array, low, high - low + 1);

        for (int r = 0; r < i1; r++) {
            msdSort(array, aux, low + count[r], low + count[r + 1] - 1, d + 1);
        }
    }

    private static int charAt(String s, int d) {
        return d < s.length() ? s.charAt(d) : -1;
    }

    public static void runTests() {
        String[] testArray = randomItems(10);
        System.out.println("Строки до сортировки:");
        for (String s : testArray) {
            System.out.println(s);
        }
        System.out.println("\n");
        msdSort(testArray);

        System.out.println("Строки после сортировки:");
        for (String s : testArray) {
            System.out.println(s);
        }
    }
}
