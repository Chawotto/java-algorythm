package org.example;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryTreeChecker {

    private static final Logger logger = Logger.getLogger(BinaryTreeChecker.class.getName());

    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.INFO);
    }

    static class Node {
        int value;
        int count;
        Node left;
        Node right;

        public Node(int value, int count) {
            this.value = value;
            this.count = count;
        }
    }

    public static boolean isBinaryTreeO(Node node) {
        if (node == null) {
            return true;
        }

        int leftCount = countNodes(node.left);
        int rightCount = countNodes(node.right);
        int totalNodes = leftCount + rightCount + 1;

        boolean isValid = totalNodes == node.count;

        if (!isValid && logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format(
                        "Ошибка в узле со значением %d: Ожидалось %d узлов, найдено %d",
                        node.value, node.count, totalNodes));
            }


        return isValid && isBinaryTreeO(node.left) && isBinaryTreeO(node.right);
    }

    private static int countNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public static void main(String[] args) {
        // Пример дерева
        Node root = new Node(1, 5);
        root.left = new Node(2, 2);
        root.right = new Node(3, 2);
        root.left.left = new Node(4, 1);
        root.left.right = null;
        root.right.left = new Node(5, 1);
        root.right.right = null;

        logger.info("Начинается проверка бинарного дерева...");

        boolean isValidTree = isBinaryTreeO(root);

        if (isValidTree) {
            logger.info("Дерево корректно: все счетчики узлов верны.");
        } else {
            logger.severe("Дерево некорректно: есть несоответствия в счетчиках узлов.");
        }
    }
}
