package org.example;

import java.util.Random;
import java.util.Scanner;

public class BinaryStringSearch {

    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Меню:");
            System.out.println("1. Ввести значения M и N");
            System.out.println("2. Тесты");
            System.out.println("3. Выйти");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleInput(scanner);
                    break;
                case 2:
                    runTests();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }

        scanner.close();
    }

    private static void handleInput(Scanner scanner) {
        System.out.print("Введите значение M: ");
        int m = scanner.nextInt();
        System.out.print("Введите значение N: ");
        int n = scanner.nextInt();
        if (m > n) {
            System.out.println("M не может быть больше N.");
        } else {
            String binaryString = generateRandomBinaryString(n);
            if (n <= 100) {
                System.out.println("Сгенерированная строка: " + binaryString);
            }
            else {
                String lastNCharacters = binaryString.substring(n - m);
                System.out.println("Последние " + m + " символов: " + lastNCharacters);
            }
            int count = countOccurrences(binaryString, m);
            System.out.println("Количество вхождений последних " + m + " битов: " + count);
        }
    }

    public static String generateRandomBinaryString(int length) {
        StringBuilder binaryString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            binaryString.append(random.nextInt(2));
        }
        return binaryString.toString();
    }

    public static int countOccurrences(String binaryString, int m) {
        if (m < 5) {
            return countOccurrencesSimple(binaryString, m);
        } else if (m < 10) {
            return countOccurrencesKMP(binaryString, m);
        } else {
            return countOccurrencesRabinKarp(binaryString, m);
        }
    }

    public static int countOccurrencesSimple(String binaryString, int m) {
        int count = 0;
        String target = binaryString.substring(binaryString.length() - m);
        for (int i = 0; i <= binaryString.length() - m; i++) {
            if (binaryString.substring(i, i + m).equals(target)) {
                count++;
            }
        }
        return count;
    }

    public static int countOccurrencesKMP(String binaryString, int m) {
        String target = binaryString.substring(binaryString.length() - m);
        int[] lps = computeLPSArray(target);
        int count = 0;
        int i = 0;
        int j = 0;
        while (i < binaryString.length()) {
            if (target.charAt(j) == binaryString.charAt(i)) {
                i++;
                j++;
            }
            if (j == m) {
                count++;
                j = lps[j - 1];
            } else if (i < binaryString.length() && target.charAt(j) != binaryString.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return count;
    }

    public static int[] computeLPSArray(String target) {
        int m = target.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            if (target.charAt(i) == target.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static int countOccurrencesRabinKarp(String binaryString, int m) {
        String target = binaryString.substring(binaryString.length() - m);
        int q = 101;
        int d = 2;
        int h = computeHashBase(d, m - 1, q);
        int p = computeHash(target, m, d, q);
        int t = computeHash(binaryString.substring(0, m), m, d, q);

        int count = 0;
        for (int i = 0; i <= binaryString.length() - m; i++) {
            if (p == t && checkSubstring(binaryString, i, target)) {
                count++;
            }
            if (i < binaryString.length() - m) {
                t = updateHash(t, binaryString.charAt(i), binaryString.charAt(i + m), d, h, q);
            }
        }
        return count;
    }

    private static int computeHashBase(int d, int m, int q) {
        int h = 1;
        for (int i = 0; i < m; i++) {
            h = (h * d) % q;
        }
        return h;
    }

    private static int computeHash(String str, int m, int d, int q) {
        int hash = 0;
        for (int i = 0; i < m; i++) {
            hash = (d * hash + str.charAt(i)) % q;
        }
        return hash;
    }

    private static int updateHash(int currentHash, char oldChar, char newChar, int d, int h, int q) {
        int newHash = (d * (currentHash - oldChar * h) + newChar) % q;
        if (newHash < 0) {
            newHash = (newHash + q);
        }
        return newHash;
    }

    private static boolean checkSubstring(String binaryString, int start, String target) {
        for (int j = 0; j < target.length(); j++) {
            if (binaryString.charAt(start + j) != target.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    public static void runTests() {
        System.out.println("Тесты:");
        testCase(10, 2);
        testCase(15, 3);
        testCase(20, 5);
        testCase(35, 7);
        testCase(56, 8);
        testCase(100000, 10);
        testCase(10000, 2);
    }

    public static void testCase(int n, int m) {
        String binaryString = generateRandomBinaryString(n);
        int count = countOccurrences(binaryString, m);
        System.out.print("N = " + n);
        System.out.println(", M = " + m);
        if (n <= 100) {
            System.out.println("Сгенерированная строка: " + binaryString);
        }
        else {
            String lastNCharacters = binaryString.substring(n - m);
            System.out.println("Последние " + m + " символов: " + lastNCharacters);
        }

        System.out.println("Количество вхождений: " + count);
        System.out.println();
    }
}
