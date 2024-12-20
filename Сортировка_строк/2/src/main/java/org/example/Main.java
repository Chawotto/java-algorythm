package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Выберите опцию:");
            System.out.println("1. Провести тесты самостоятельно");
            System.out.println("2. Запустить заготовленные тесты");
            System.out.println("3. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    runCustomTest(scanner);
                    break;
                case 2:
                    runPredefinedTests();
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

    private static void runCustomTest(Scanner scanner) {
        System.out.println("Введите массив целых чисел через пробел:");
        String input = scanner.nextLine();
        String[] parts = input.split("\\s+");
        int[] array = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();

        System.out.println("Исходный массив: " + Arrays.toString(array));
        sort(array);
        System.out.println("Отсортированный массив: " + Arrays.toString(array));
    }

    private static void runPredefinedTests() {
        int[][] testCases = {
                {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5},
                {10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {59, 68, 32, 22, 6, 0, 2, -1, -5, 15}
        };

        for (int[] testCase : testCases) {
            int[] array = Arrays.copyOf(testCase, testCase.length);
            System.out.println("Исходный массив: " + Arrays.toString(array));
            sort(array);
            System.out.println("Отсортированный массив: " + Arrays.toString(array));
        }
    }

    public static void sort(int[] array) {
        sort(array, 0, array.length - 1);
    }

    private static void sort(int[] array, int low, int high) {
        if (low >= high) return;

        int lt = low;
        int i = low + 1;
        int gt = high;
        int pivot = array[low];

        while (i <= gt) {
            if (array[i] < pivot) {
                swap(array, lt++, i++);
            } else if (array[i] > pivot) {
                swap(array, i, gt--);
            } else {
                i++;
            }
        }

        sort(array, low, lt - 1);
        sort(array, gt + 1, high);
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
