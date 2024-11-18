package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryTreeTraversal {

    private static final Logger logger = Logger.getLogger(BinaryTreeTraversal.class.getName());

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

    public static void printLevel(Node root) {
        if (root == null) {
            logger.warning("Дерево пустое.");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        logger.info("Начинается поуровневый обход дерева:");
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Узел: %d",+ current.value));
                }
            if (current.left != null) {
                queue.add(current.left);
            }
            if (current.right != null) {
                queue.add(current.right);
            }
        }
    }

    public static Node createExampleTree() {
        //пример дерева
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        return root;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Node root = createExampleTree();
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Бинарное дерево создано. Корень имеет значение: %d", root.value));
        }
        logger.info("Хотите выполнить поуровневый обход дерева? (y/n)");

        String input = scanner.nextLine();
        if ("y".equalsIgnoreCase(input)) {
            printLevel(root);
        } else {
            logger.info("Выход из программы.");
        }

        scanner.close();
    }
}
