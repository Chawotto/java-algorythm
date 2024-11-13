package org.example;

import java.util.Scanner;
import java.util.logging.Logger;

public class DoublyLinkedList {
    private static final Logger logger = Logger.getLogger(DoublyLinkedList.class.getName());
    private static final String NODE_NOT_FOUND_MESSAGE = "Узел с указанным значением не найден.";
    private DoubleNode head;
    private DoubleNode tail;  

    private static class DoubleNode {
        int value;
        DoubleNode next;
        DoubleNode prev;

        DoubleNode(int value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    public void insertAtBeginning(int value) {
        DoubleNode newNode = new DoubleNode(value);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        logger.info(() -> String.format("Element inserted at the beginning: %d", value));
    }

    public void insertAtEnd(int value) {
        DoubleNode newNode = new DoubleNode(value);
        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        logger.info(() -> String.format("Element inserted at the end: %d", value));
    }

    public void deleteFromBeginning() {
        if (head == null) {
            logger.warning("List is empty. Nothing to delete from beginning.");
            return;
        }
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        logger.info("Element deleted from the beginning.");
    }

    public void deleteFromEnd() {
        if (tail == null) {
            logger.warning("List is empty. Nothing to delete from end.");
            return;
        }
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        logger.info("Element deleted from the end.");
    }

    public void insertBefore(DoubleNode node, int value) {
        if (node == null || head == null) return;
        DoubleNode newNode = new DoubleNode(value);
        if (node == head) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else {
            newNode.prev = node.prev;
            newNode.next = node;
            node.prev.next = newNode;
            node.prev = newNode;
        }
        logger.info(() -> String.format("Element %d inserted before node with value %d", value, node.value));
    }

    public void insertAfter(DoubleNode node, int value) {
        if (node == null) return;
        DoubleNode newNode = new DoubleNode(value);
        if (node == tail) {
            node.next = newNode;
            newNode.prev = node;
            tail = newNode;
        } else {
            newNode.next = node.next;
            newNode.prev = node;
            node.next.prev = newNode;
            node.next = newNode;
        }
        logger.info(() -> String.format("Element %d inserted after node with value %d", value, node.value));
    }

    public void deleteNode(DoubleNode node) {
        if (node == null || head == null) return;
        if (node == head && node == tail) {
            head = tail = null;
        } else if (node == head) {
            head = node.next;
            head.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        logger.info(() -> String.format("Node with value %d deleted.", node.value));
    }

    public void printList() {
        DoubleNode current = head;
        StringBuilder listRepresentation = new StringBuilder();
        while (current != null) {
            listRepresentation.append(current.value).append(" ");
            current = current.next;
        }
        logger.info(() -> String.format("List: %s", listRepresentation.toString()));
    }

    // Метод для нахождения узла по значению
    public DoubleNode findNode(int value) {
        DoubleNode current = head;
        while (current != null) {
            if (current.value == value) return current;
            current = current.next;
        }
        return null;
    }

    public static void main(String[] args) {
        DoublyLinkedList list = new DoublyLinkedList();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            logger.info("""
        Выберите операцию:
        1. Вставка в начало
        2. Вставка в конец
        3. Удаление из начала
        4. Удаление из конца
        5. Вставка перед указанным значением
        6. Вставка после указанного значения
        7. Удаление указанного значения
        8. Вывести список
        9. Выйти
        """);

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    logger.info("Введите значение: ");
                    int value = scanner.nextInt();
                    list.insertAtBeginning(value);
                }
                case 2 -> {
                    logger.info("Введите значение: ");
                    int value = scanner.nextInt();
                    list.insertAtEnd(value);
                }
                case 3 -> list.deleteFromBeginning();
                case 4 -> list.deleteFromEnd();
                case 5 -> {
                    logger.info("Введите значение, перед которым нужно вставить: ");
                    int targetValue = scanner.nextInt();
                    logger.info("Введите новое значение: ");
                    int newValue = scanner.nextInt();
                    DoubleNode targetNode = list.findNode(targetValue);
                    if (targetNode != null) {
                        list.insertBefore(targetNode, newValue);
                    } else {
                        logger.warning(NODE_NOT_FOUND_MESSAGE);
                    }
                }
                case 6 -> {
                    logger.info("Введите значение, после которого нужно вставить: ");
                    int targetValue = scanner.nextInt();
                    logger.info("Введите новое значение: ");
                    int newValue = scanner.nextInt();
                    DoubleNode targetNode = list.findNode(targetValue);
                    if (targetNode != null) {
                        list.insertAfter(targetNode, newValue);
                    } else {
                        logger.warning(NODE_NOT_FOUND_MESSAGE);
                    }
                }
                case 7 -> {
                    logger.info("Введите значение для удаления: ");
                    int targetValue = scanner.nextInt();
                    DoubleNode targetNode = list.findNode(targetValue);
                    if (targetNode != null) {
                        list.deleteNode(targetNode);
                    } else {
                        logger.warning(NODE_NOT_FOUND_MESSAGE);
                    }
                }
                case 8 -> list.printList();
                case 9 -> {
                    logger.info("Выход из программы.");
                    scanner.close();
                    return;
                }
                default -> logger.warning("Некорректный ввод. Попробуйте еще раз.");
            }
        }
    }
}
