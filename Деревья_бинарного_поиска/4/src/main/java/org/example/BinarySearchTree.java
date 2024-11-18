package org.example;

import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinarySearchTree {

    private static final Logger logger = Logger.getLogger(BinarySearchTree.class.getName());

    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.INFO);
    }

    static class Node {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    private Node root;

    public void insert(int value) {
        root = insertRecursively(root, value);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Добавлен узел со значением: %d", value));
        }
    }

    private Node insertRecursively(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }
        if (value < current.value) {
            current.left = insertRecursively(current.left, value);
        } else if (value > current.value) {
            current.right = insertRecursively(current.right, value);
        }
        return current;
    }

    public void draw() {
        if (root == null) {
            logger.warning("Дерево пустое. Нечего вычерчивать.");
            return;
        }

        int height = calculateHeight(root);
        int width = (int) Math.pow(2, height) - 1;

        String[][] canvas = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                canvas[i][j] = " ";
            }
        }

        fillCanvas(root, canvas, 0, 0, width - 1);

        logger.info("Дерево вычерчено:");
        for (String[] row : canvas) {
            StringBuilder line = new StringBuilder();
            for (String cell : row) {
                line.append(cell);
            }
            if (logger.isLoggable(Level.INFO)) {
                logger.info(line.toString());
            }
        }
    }

    private void fillCanvas(Node node, String[][] canvas, int level, int left, int right) {
        if (node == null) {
            return;
        }

        int mid = (left + right) / 2;
        canvas[level][mid] = Integer.toString(node.value);

        fillCanvas(node.left, canvas, level + 1, left, mid - 1);
        fillCanvas(node.right, canvas, level + 1, mid + 1, right);
    }

    private int calculateHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
    }

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            logger.info("Выберите действие: 1 - Добавить узел, 2 - Нарисовать дерево, 3 - Выход");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    logger.info("Введите значение узла:");
                    int value = scanner.nextInt();
                    bst.insert(value);
                    break;

                case 2:
                    bst.draw();
                    break;

                case 3:
                    logger.info("Выход из программы.");
                    scanner.close();
                    return;

                default:
                    logger.warning("Некорректный выбор. Попробуйте снова.");
            }
        }
    }
}
