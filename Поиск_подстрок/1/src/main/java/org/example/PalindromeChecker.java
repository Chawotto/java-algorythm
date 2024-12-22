package org.example;

import java.util.Scanner;

public class PalindromeChecker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Меню:");
            System.out.println("1. Ввести строку для проверки на палиндром");
            System.out.println("2. Тесты");
            System.out.println("3. Выйти");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    checkPalindrome(scanner);
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

    private static void checkPalindrome(Scanner scanner) {
        System.out.println("Введите строку: ");
        String input = scanner.nextLine();
        StringBuilder currentString = new StringBuilder();

        for (char c : input.toCharArray()) {
            currentString.append(c);
            if (isPalindrome(currentString.toString())) {
                System.out.println("Текущая строка \"" + currentString + "\" является палиндромом.");
            } else {
                System.out.println("Текущая строка \"" + currentString + "\" не является палиндромом.");
            }
        }
    }

    private static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    private static void runTests() {
        String[] tests = {
                "a", "aa", "aba", "abba", "abcba", "abcd", "racecar", "hello", "шалаш"
        };

        for (String test : tests) {
            System.out.println("Тест строки \"" + test + "\": " + (isPalindrome(test) ? "палиндром" : "не палиндром"));
        }
    }
}
