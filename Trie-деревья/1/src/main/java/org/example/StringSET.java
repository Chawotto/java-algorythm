package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StringSET {
    private final TrieNode root;

    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;

        TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
        }
    }

    public StringSET() {
        root = new TrieNode();
    }

    public void add(String key) {
        TrieNode current = root;
        for (char ch : key.toCharArray()) {
            current = current.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        current.isEndOfWord = true;
    }

    public void remove(String key) {
        remove(root, key, 0);
    }

    private boolean remove(TrieNode current, String key, int depth) {
        if (current == null) return false;

        if (depth == key.length()) {
            if (!current.isEndOfWord) return false;
            current.isEndOfWord = false;
            return current.children.isEmpty();
        }

        char ch = key.charAt(depth);
        TrieNode node = current.children.get(ch);
        if (node == null) return false;

        boolean shouldDeleteCurrentNode = remove(node, key, depth + 1) && !node.isEndOfWord;

        if (shouldDeleteCurrentNode) {
            current.children.remove(ch);
            return current.children.isEmpty();
        }

        return false;
    }

    public boolean contains(String key) {
        TrieNode current = root;
        for (char ch : key.toCharArray()) {
            TrieNode node = current.children.get(ch);
            if (node == null) return false;
            current = node;
        }
        return current.isEndOfWord;
    }

    public boolean isEmpty() {
        return root.children.isEmpty();
    }

    public int size() {
        return size(root);
    }

    private int size(TrieNode node) {
        if (node == null) return 0;
        int count = 0;
        if (node.isEndOfWord) count++;
        for (TrieNode child : node.children.values()) {
            count += size(child);
        }
        return count;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        toString(root, new StringBuilder(), result);
        return result.toString();
    }

    private void toString(TrieNode node, StringBuilder prefix, StringBuilder result) {
        if (node.isEndOfWord) {
            result.append(prefix.toString()).append("\n");
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            toString(entry.getValue(), prefix, result);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public void demonstrate() {
        System.out.println("Добавлено: 'bread', 'hello' ");
        add("hello");
        add("bread");
        System.out.println("Содержит 'hello': " + contains("hello"));
        System.out.println("Содержит 'bread': " + contains("bread"));
        System.out.println("Содержит 'hell': " + contains("hell"));
        System.out.println("Количество ключей: " + size());
        System.out.println("Пусто: " + isEmpty());
        System.out.println("Строковое представление:");
        System.out.println(this);
        System.out.println("Удаление 'bread'");
        remove("bread");
        System.out.println("Строковое представление:");
        System.out.println(this);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringSET set = new StringSET();

        while (true) {
            System.out.println("Выберите операцию:");
            System.out.println("1. Добавить ключ");
            System.out.println("2. Удалить ключ");
            System.out.println("3. Проверить наличие ключа");
            System.out.println("4. Проверить, пусто ли множество");
            System.out.println("5. Получить количество ключей");
            System.out.println("6. Получить строковое представление множества");
            System.out.println("7. Демонстрация работоспособности программы");
            System.out.println("8. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите ключ для добавления: ");
                    String addKey = scanner.nextLine();
                    set.add(addKey);
                    break;
                case 2:
                    System.out.print("Введите ключ для удаления: ");
                    String deleteKey = scanner.nextLine();
                    set.remove(deleteKey);
                    break;
                case 3:
                    System.out.print("Введите ключ для проверки: ");
                    String checkKey = scanner.nextLine();
                    System.out.println("Ключ '" + checkKey + "' " + (set.contains(checkKey) ? "присутствует" : "отсутствует"));
                    break;
                case 4:
                    System.out.println("Множество " + (set.isEmpty() ? "пусто" : "не пусто"));
                    break;
                case 5:
                    System.out.println("Количество ключей: " + set.size());
                    break;
                case 6:
                    System.out.println("Строковое представление множества:");
                    System.out.println(set);
                    break;
                case 7:
                    set.demonstrate();
                    break;
                case 8:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
