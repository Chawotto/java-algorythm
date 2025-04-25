package org.example;

import java.util.*;

public class UniqueSubstrings {

    private static final String UNIQUE_SUBSTRINGS_PREFIX = "Уникальные подстроки: ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SuffixTree tree;

        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Ввести текст и подсчитать количество уникальных подстрок");
            System.out.println("2. Выполнить тесты");
            System.out.println("3. Выход");
            System.out.print("Выберите пункт меню: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите текст: ");
                    String text = scanner.nextLine();
                    tree = new SuffixTree(text);
                    System.out.println("Количество уникальных подстрок: " + tree.countUniqueSubstrings());
                    System.out.println(UNIQUE_SUBSTRINGS_PREFIX + tree.getUniqueSubstrings());
                    break;
                case 2:
                    runTests();
                    break;
                case 3:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }

    private static void runTests() {
        System.out.println("Тесты:");

        SuffixTree tree1 = new SuffixTree("abc");
        System.out.println("Текст: 'abc', Уникальных подстрок: " + tree1.countUniqueSubstrings() + " (ожидается 6)");
        System.out.println(UNIQUE_SUBSTRINGS_PREFIX + tree1.getUniqueSubstrings());

        SuffixTree tree2 = new SuffixTree("aaa");
        System.out.println("Текст: 'aaa', Уникальных подстрок: " + tree2.countUniqueSubstrings() + " (ожидается 3)");
        System.out.println(UNIQUE_SUBSTRINGS_PREFIX + tree2.getUniqueSubstrings());

        SuffixTree tree3 = new SuffixTree("");
        System.out.println("Текст: '', Уникальных подстрок: " + tree3.countUniqueSubstrings() + " (ожидается 0)");
        System.out.println(UNIQUE_SUBSTRINGS_PREFIX + tree3.getUniqueSubstrings());
    }
}

class SuffixTree {
    private final Node root = new Node();
    private final StringBuilder uniqueSubstrings = new StringBuilder();

    public SuffixTree(String text) {
        for (int i = 0; i < text.length(); i++) {
            addSubstring(text.substring(i));
        }
    }

    private void addSubstring(String substring) {
        Node current = root;
        for (char c : substring.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new Node());
        }
        current.isEndOfSubstring = true;
    }

    public int countUniqueSubstrings() {
        uniqueSubstrings.setLength(0);
        return countUniqueSubstrings(root, new StringBuilder());
    }

    private int countUniqueSubstrings(Node node, StringBuilder currentSubstring) {
        int count = 0;
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            currentSubstring.append(entry.getKey());
            count += 1 + countUniqueSubstrings(entry.getValue(), currentSubstring);
            uniqueSubstrings.append(currentSubstring).append(", ");
            currentSubstring.deleteCharAt(currentSubstring.length() - 1);
        }
        return count;
    }

    public String getUniqueSubstrings() {
        if (!uniqueSubstrings.isEmpty()) {
            uniqueSubstrings.setLength(uniqueSubstrings.length() - 2);
        }
        return uniqueSubstrings.toString();
    }

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        boolean isEndOfSubstring;
    }
}