package org.example;

import java.util.ArrayList;
import java.util.Scanner;

class Node {
    int key1;
    int key2;
    Node left;
    Node middle;
    Node right;
    boolean is2Node;

    Node(int key) {
        this.key1 = key;
        this.is2Node = true;
    }

    Node(int key1, int key2) {
        this.key1 = key1;
        this.key2 = key2;
        this.is2Node = false;
    }
}

public class TwoThreeST {
    private Node root;
    public static final String TEST = "Вставка ключа:";

    public TwoThreeST() {
        this.root = null;
    }

    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
            display();
            return;
        }

        ArrayList<Node> path = new ArrayList<>();
        Node current = findLeaf(key, path);

        if (current.is2Node) {
            Node newNode = convertTo3Node(current, key);
            updateNodeReference(current, newNode, path);
        } else {
            splitLeafAndPropagate(current, key, path);
        }

        display();
    }

    private Node findLeaf(int key, ArrayList<Node> path) {
        Node current = root;
        while (current.left != null) {
            path.add(current);
            if (current.is2Node) {
                current = (key < current.key1) ? current.left : current.right;
            } else {
                if (key < current.key1) {
                    current = current.left;
                } else if (key < current.key2) {
                    current = current.middle;
                } else {
                    current = current.right;
                }
            }
        }
        return current;
    }

    private Node convertTo3Node(Node node, int key) {
        int newKey1 = Math.min(node.key1, key);
        int newKey2 = Math.max(node.key1, key);
        Node newNode = new Node(newKey1, newKey2);

        newNode.left = node.left;
        newNode.middle = node.middle;
        newNode.right = node.right;

        return newNode;
    }

    private void updateNodeReference(Node oldNode, Node newNode, ArrayList<Node> path) {
        if (path.isEmpty()) {
            root = newNode;
            return;
        }

        Node parent = path.get(path.size() - 1);
        if (parent.is2Node) {
            if (parent.left == oldNode) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        } else {
            if (parent.left == oldNode) {
                parent.left = newNode;
            } else if (parent.middle == oldNode) {
                parent.middle = newNode;
            } else {
                parent.right = newNode;
            }
        }
    }

    private void splitLeafAndPropagate(Node node, int key, ArrayList<Node> path) {
        int[] keys = {node.key1, node.key2, key};
        java.util.Arrays.sort(keys);
        int middleKey = keys[1];
        Node leftNode = new Node(keys[0]);
        Node rightNode = new Node(keys[2]);

        propagateSplit(middleKey, leftNode, rightNode, path);
    }

    private void propagateSplit(int middleKey, Node leftNode, Node rightNode, ArrayList<Node> path) {
        if (path.isEmpty()) {
            root = new Node(middleKey);
            root.left = leftNode;
            root.right = rightNode;
            return;
        }

        Node parent = path.remove(path.size() - 1);
        if (parent.is2Node) {
            insertInto2NodeParent(parent, middleKey, leftNode, rightNode);
        } else {
            split3NodeParent(parent, middleKey, leftNode, rightNode, path);
        }
    }

    private void insertInto2NodeParent(Node parent, int middleKey, Node leftNode, Node rightNode) {
        if (middleKey < parent.key1) {
            parent.key2 = parent.key1;
            parent.key1 = middleKey;
            parent.middle = parent.right;
            parent.left = leftNode;
            parent.right = rightNode;
        } else {
            parent.key2 = middleKey;
            parent.middle = leftNode;
            parent.right = rightNode;
        }
        parent.is2Node = false;
    }

    private void split3NodeParent(Node parent, int middleKey, Node leftNode, Node rightNode, ArrayList<Node> path) {
        int[] parentKeys = {parent.key1, parent.key2, middleKey};
        java.util.Arrays.sort(parentKeys);
        middleKey = parentKeys[1];
        Node newLeft = new Node(parentKeys[0]);
        Node newRight = new Node(parentKeys[2]);
        newLeft.left = parent.left;
        newLeft.right = (middleKey < parent.key1) ? leftNode : parent.middle;
        newRight.left = (middleKey < parent.key1) ? parent.middle : leftNode;
        newRight.right = rightNode;

        propagateSplit(middleKey, newLeft, newRight, path);
    }

    public boolean search(int key) {
        boolean result = search(root, key);
        display();
        return result;
    }

    private boolean search(Node node, int key) {
        if (node == null) {
            return false;
        }
        if (node.is2Node) {
            if (key == node.key1) return true;
            if (key < node.key1) return search(node.left, key);
            return search(node.right, key);
        } else {
            if (key == node.key1 || key == node.key2) return true;
            if (key < node.key1) return search(node.left, key);
            if (key < node.key2) return search(node.middle, key);
            return search(node.right, key);
        }
    }

    public void display() {
        System.out.println("Структура дерева сейчас: ");
        display(root, "", true);
        System.out.println();
    }

    private void display(Node node, String prefix, boolean isTail) {
        if (node != null) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + node.key1 + (node.is2Node ? "" : " " + node.key2));
            display(node.left, prefix + (isTail ? "    " : "│   "), false);
            display(node.middle, prefix + (isTail ? "    " : "│   "), false);
            display(node.right, prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public void runTests() {
        TwoThreeST tree = new TwoThreeST();
        System.out.println("=== Начало тестов для 2-3 дерева ===");

        System.out.println("Тест 1: Вставка ключей [10, 20, 30]");
        int[] keys1 = {10, 20, 30};
        for (int key : keys1) {
            System.out.println(TEST + key);
            tree.insert(key);
        }
        System.out.println("Поиск ключа 20: " + tree.search(20));
        System.out.println("Поиск ключа 15: " + tree.search(15));

        tree = new TwoThreeST();
        System.out.println("\nТест 2: Вставка ключей с разделением узлов [5, 15, 25, 10, 20]");
        int[] keys2 = {5, 15, 25, 10, 20};
        for (int key : keys2) {
            System.out.println(TEST + key);
            tree.insert(key);
        }
        System.out.println("Поиск ключа 10: " + tree.search(10));
        System.out.println("Поиск ключа 30: " + tree.search(30));

        tree = new TwoThreeST();
        System.out.println("\nТест 3: Вставка большего набора ключей [25, 20, 5, 6, 12, 30, 7, 3]");
        int[] keys3 = {25, 20, 5, 6, 12, 30, 7, 3};
        for (int key : keys3) {
            System.out.println(TEST + key);
            tree.insert(key);
        }
        System.out.println("Поиск ключа 12: " + tree.search(12));
        System.out.println("Поиск ключа 99: " + tree.search(99));

        System.out.println("=== Конец тестов ===");
    }

    public static void main(String[] args) {
        TwoThreeST tree = new TwoThreeST();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите операцию:");
            System.out.println("1. Вставить");
            System.out.println("2. Найти");
            System.out.println("3. Запустить тесты");
            System.out.println("4. Выход");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите ключ для вставки: ");
                    int insertKey = scanner.nextInt();
                    tree.insert(insertKey);
                    break;
                case 2:
                    System.out.print("Введите ключ для поиска: ");
                    int searchKey = scanner.nextInt();
                    boolean found = tree.search(searchKey);
                    System.out.println("Результат поиска: " + found);
                    break;
                case 3:
                    tree.runTests();
                    break;
                case 4:
                    System.out.println("Выход...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }
}