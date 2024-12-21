package org.example;

import java.util.Scanner;

class TSTNode {
    char character;
    TSTNode left;
    TSTNode mid;
    TSTNode right;
    boolean isEnd;

    public TSTNode(char character) {
        this.character = character;
        this.left = null;
        this.mid = null;
        this.right = null;
        this.isEnd = false;
    }
}

class TST {
    private TSTNode root;

    public TST() {
        this.root = null;
    }

    public void insert(String key) {
        root = insert(root, key, 0);
    }

    private TSTNode insert(TSTNode node, String key, int d) {
        char c = key.charAt(d);
        if (node == null) {
            node = new TSTNode(c);
        }
        if (c < node.character) {
            node.left = insert(node.left, key, d);
        } else if (c > node.character) {
            node.right = insert(node.right, key, d);
        } else if (d < key.length() - 1) {
            node.mid = insert(node.mid, key, d + 1);
        } else {
            node.isEnd = true;
        }
        return node;
    }

    public boolean contains(String key) {
        TSTNode node = get(root, key, 0);
        return node == null || !node.isEnd;
    }

    private TSTNode get(TSTNode node, String key, int d) {
        if (node == null) return null;
        char c = key.charAt(d);
        if (c < node.character) {
            return get(node.left, key, d);
        } else if (c > node.character) {
            return get(node.right, key, d);
        } else if (d < key.length() - 1) {
            return get(node.mid, key, d + 1);
        } else {
            return node;
        }
    }
}

public class UniqueSubstrings {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Меню:");
            System.out.println("1. Ввести текст и длину подстрок");
            System.out.println("2. Выполнить тест");
            System.out.println("3. Выйти");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Введите текст:");
                    String text = scanner.nextLine();
                    System.out.println("Введите длину подстрок l:");
                    int l = scanner.nextInt();

                    TST tst = new TST();
                    int uniqueSubstringsCount = 0;

                    for (int i = 0; i <= text.length() - l; i++) {
                        String substring = text.substring(i, i + l);
                        if (tst.contains(substring)) {
                            tst.insert(substring);
                            uniqueSubstringsCount++;
                        }
                    }

                    System.out.println("Количество уникальных подстрок длиной " + l + ": " + uniqueSubstringsCount);
                    break;

                case 2:
                    runTest();
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

    private static void runTest() {
        String testText = "cgcgggcgcg";
        int testL = 3;

        TST tst = new TST();
        int uniqueSubstringsCount = 0;

        for (int i = 0; i <= testText.length() - testL; i++) {
            String substring = testText.substring(i, i + testL);
            if (tst.contains(substring)) {
                tst.insert(substring);
                uniqueSubstringsCount++;
            }
        }

        System.out.println("Тест:");
        System.out.println("Текст: " + testText);
        System.out.println("Длина подстрок: " + testL);
        System.out.println("Количество уникальных подстрок: " + uniqueSubstringsCount);
    }
}
