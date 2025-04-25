package org.example;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class SubstringMapping {

    private static final String RESULT_PREFIX = "Результат: ";

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Set<String> words = new HashSet<>();
    }

    static class Trie {
        private final TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            for (int i = 0; i < word.length(); i++) {
                TrieNode node = root;
                for (int j = i; j < word.length(); j++) {
                    char c = word.charAt(j);
                    node.children.putIfAbsent(c, new TrieNode());
                    node = node.children.get(c);
                    node.words.add(word);
                }
            }
        }

        public List<String> search(String substring) {
            TrieNode node = root;
            for (int i = 0; i < substring.length(); i++) {
                char c = substring.charAt(i);
                node = node.children.get(c);
                if (node == null) {
                    return Collections.emptyList();
                }
            }
            return new ArrayList<>(node.words);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        Trie trie = new Trie();

        boolean running = true;

        while (running) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить строку в список");
            System.out.println("2. Найти строки, содержащие подстроку");
            System.out.println("3. Провести тесты");
            System.out.println("4. Выход");
            System.out.print("Выберите пункт меню: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Введите строку для добавления: ");
                    String word = scanner.nextLine();
                    trie.insert(word);
                    System.out.println("Строка добавлена!");
                    break;

                case "2":
                    System.out.print("Введите подстроку для поиска: ");
                    String substring = scanner.nextLine();
                    List<String> results = trie.search(substring);
                    if (results.isEmpty()) {
                        System.out.println("Совпадений не найдено.");
                    } else {
                        System.out.println("Найденные строки: ");
                        results.forEach(System.out::println);
                    }
                    break;

                case "3":
                    runTests(trie);
                    break;

                case "4":
                    running = false;
                    System.out.println("Выход из программы.");
                    break;

                default:
                    System.out.println("Неверный выбор.");
            }
        }

        scanner.close();
    }

    private static void runTests(Trie trie) {
        System.out.println("\nЗапуск тестов");
        System.out.println("Добавление слов в список: 'победа', 'обед', 'беда', 'еда', 'да', 'а', 'зима' ");
        List<String> testWords = Arrays.asList("победа", "обед", "беда", "зима", "еда", "да", "а");
        for (String word : testWords) {
            trie.insert(word);
        }

        System.out.println("Тест 1: Поиск подстроки 'об'");
        System.out.println(RESULT_PREFIX + trie.search("об"));

        System.out.println("Тест 2: Поиск подстроки 'еда'");
        System.out.println(RESULT_PREFIX + trie.search("еда"));

        System.out.println("Тест 3: Поиск подстроки 'да'");
        System.out.println(RESULT_PREFIX + trie.search("да"));

        System.out.println("Тест 4: Поиск подстроки 'а'");
        System.out.println(RESULT_PREFIX + trie.search("а"));
    }
}