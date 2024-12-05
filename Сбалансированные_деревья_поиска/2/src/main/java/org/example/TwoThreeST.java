package org.example;

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

    public TwoThreeST() {
        this.root = null;
    }

    public void insert(int key) {
        root = insert(root, key);
    }

    private Node insert(Node node, int key) {
        if (node == null) {
            return new Node(key);
        }

        if (node.is2Node) {
            if (key < node.key1) {
                node.left = insert(node.left, key);
            } else {
                node.right = insert(node.right, key);
            }
        } else {
            if (key < node.key1) {
                node.left = insert(node.left, key);
            } else if (key < node.key2) {
                node.middle = insert(node.middle, key);
            } else {
                node.right = insert(node.right, key);
            }
        }

        if (node.left != null && !node.left.is2Node) {
            return split(node);
        }

        return node;
    }

    private Node split(Node node) {
        int key1 = node.left.key1;
        int key2 = node.left.key2;
        Node left = node.left.left;
        Node middle = node.left.middle;
        Node right = node.left.right;

        if (node.is2Node) {
            node.is2Node = false;
            node.key2 = node.key1;
            node.key1 = key2;
            node.middle = node.right;
            node.right = right;
            node.left = new Node(key1);
            node.left.left = left;
            node.left.right = middle;
        } else {
            Node newNode = new Node(key2);
            newNode.left = middle;
            newNode.right = right;
            node.key2 = key1;
            node.middle = left;
            return newNode;
        }

        return node;
    }

    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(Node node, int key) {
        if (node == null) {
            return false;
        }

        if (node.is2Node) {
            if (key == node.key1) {
                return true;
            } else if (key < node.key1) {
                return search(node.left, key);
            } else {
                return search(node.right, key);
            }
        } else {
            if (key == node.key1 || key == node.key2) {
                return true;
            } else if (key < node.key1) {
                return search(node.left, key);
            } else if (key < node.key2) {
                return search(node.middle, key);
            } else {
                return search(node.right, key);
            }
        }
    }

    public static void main(String[] args) {
        TwoThreeST tree = new TwoThreeST();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите операцию:");
            System.out.println("1. Вставить");
            System.out.println("2. Найти");
            System.out.println("3. Выход");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите ключ для вставки: ");
                    int insertKey = scanner.nextInt();
                    tree.insert(insertKey);
                    break;
                case 2:
                    System.out.print("Введите ключ для посика: ");
                    int searchKey = scanner.nextInt();
                    boolean found = tree.search(searchKey);
                    System.out.println("Результат поиска: " + found);
                    break;
                case 3:
                    System.out.println("Выход...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }
}
