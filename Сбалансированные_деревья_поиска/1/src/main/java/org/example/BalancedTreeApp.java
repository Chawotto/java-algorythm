package org.example;

import java.util.Scanner;

public class BalancedTreeApp {

    public static int calculateInternalPath(int n) {
        if (n <= 0) {
            return 0;
        }

        int leftSubtreeNodes = (n - 1) / 2;
        int rightSubtreeNodes = (n - 1) / 2;

        return calculateInternalPath(leftSubtreeNodes)
                + calculateInternalPath(rightSubtreeNodes)
                + n - 1;
    }

    public static boolean isValidTreeSize(int n) {
        int powerCheck = n + 1;
        return (powerCheck & (powerCheck - 1)) == 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите количество узлов в дереве (формат 2^k - 1, например 7, 15, 31):");

        while (true) {
            System.out.print("Введите N (или 'exit' для выхода): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                int n = Integer.parseInt(input);

                if (!isValidTreeSize(n)) {
                    System.out.println("Ошибка: число узлов должно быть в формате 2^k - 1 (например, 7, 15, 31). Попробуйте снова.");
                } else {
                    int internalPathLength = calculateInternalPath(n);
                    System.out.println("Для N = " + n + ", длина внутреннего пути = " + internalPathLength);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число узлов или 'exit' для выхода.");
            }
        }

        scanner.close();
    }
}
