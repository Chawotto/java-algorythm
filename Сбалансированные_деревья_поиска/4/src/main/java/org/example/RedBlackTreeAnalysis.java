package org.example;

import java.util.Random;
import java.util.Scanner;

public class RedBlackTreeAnalysis {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите количество экспериментов (например, 100):");
        int experiments = scanner.nextInt();

        System.out.println("Введите N (например, 10000, 100000, 1000000):");
        int n = scanner.nextInt();

        double totalRedNodePercentage = 0;

        for (int i = 0; i < experiments; i++) {
            RedBlackTree tree = new RedBlackTree();
            Random random = new Random();

            for (int j = 0; j < n; j++) {
                tree.insert(random.nextInt());
            }

            double redNodePercentage = tree.calculateRedNodePercentage();
            totalRedNodePercentage += redNodePercentage;

            System.out.printf("Эксперимент %d: Процент красных узлов = %.2f%%%n", i + 1, redNodePercentage);
        }

        double averageRedNodePercentage = totalRedNodePercentage / experiments;
        System.out.printf("Средний процент красных узлов после %d экспериментов: %.2f%%%n", experiments, averageRedNodePercentage);

        System.out.println("Гипотеза: Процент красных узлов в красно-черном дереве стабилизируется около 50%.");
    }
}

class RedBlackTree {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        int key;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(int key) {
            this.key = key;
            this.color = RED;
        }
    }

    private Node root;

    public void insert(int key) {
        Node newNode = new Node(key);
        if (root == null) {
            root = newNode;
            root.color = BLACK;
        } else {
            Node current = root;
            Node parent = null;

            while (current != null) {
                parent = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    return; // Ключ уже существует
                }
            }

            if (key < parent.key) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            newNode.parent = parent;
            fixInsert(newNode);
        }
    }

    private void fixInsert(Node node) {
        while (node.parent != null && node.parent.color == RED) {
            Node grandparent = node.parent.parent;
            if (grandparent == null) break;

            if (node.parent == grandparent.left) {
                node = handleLeftCase(node, grandparent);
            } else {
                node = handleRightCase(node, grandparent);
            }
        }
        root.color = BLACK;
    }

    private Node handleLeftCase(Node node, Node grandparent) {
        Node uncle = grandparent.right;

        if (uncle != null && uncle.color == RED) {
            recolorNodes(node.parent, uncle, grandparent);
            node = grandparent;
        } else {
            if (node == node.parent.right) {
                node = node.parent;
                rotateLeft(node);
            }
            node.parent.color = BLACK;
            grandparent.color = RED;
            rotateRight(grandparent);
        }

        return node;
    }

    private Node handleRightCase(Node node, Node grandparent) {
        Node uncle = grandparent.left;

        if (uncle != null && uncle.color == RED) {
            recolorNodes(node.parent, uncle, grandparent);
            node = grandparent;
        } else {
            if (node == node.parent.left) {
                node = node.parent;
                rotateRight(node);
            }
            node.parent.color = BLACK;
            grandparent.color = RED;
            rotateLeft(grandparent);
        }

        return node;
    }

    private void recolorNodes(Node parent, Node uncle, Node grandparent) {
        parent.color = BLACK;
        uncle.color = BLACK;
        grandparent.color = RED;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.left) {
            node.parent.left = leftChild;
        } else {
            node.parent.right = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    public double calculateRedNodePercentage() {
        int[] counts = countNodes(root);
        int redCount = counts[0];
        int totalCount = counts[1];
        return totalCount == 0 ? 0 : (redCount * 100.0) / totalCount;
    }

    private int[] countNodes(Node node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        int[] leftCounts = countNodes(node.left);
        int[] rightCounts = countNodes(node.right);

        int redCount = leftCounts[0] + rightCounts[0] + (node.color == RED ? 1 : 0);
        int totalCount = leftCounts[1] + rightCounts[1] + 1;

        return new int[]{redCount, totalCount};
    }
}
