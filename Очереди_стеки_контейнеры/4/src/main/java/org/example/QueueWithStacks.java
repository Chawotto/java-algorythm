package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueueWithStacks<T> {
    private static final Logger logger = Logger.getLogger(QueueWithStacks.class.getName());

    private Deque<T> stackPush; // стек для добавления элементов
    private Deque<T> stackPop;  // стек для извлечения элементов

    public QueueWithStacks() {
        stackPush = new ArrayDeque<>();
        stackPop = new ArrayDeque<>();
    }

    // Метод для добавления элемента в очередь
    public void enqueue(T value) {
        stackPush.push(value);
    }

    // Метод для удаления элемента из очереди
    public T dequeue() {
        if (stackPop.isEmpty()) {
            while (!stackPush.isEmpty()) {
                stackPop.push(stackPush.pop());
            }
        }

        if (stackPop.isEmpty()) {
            throw new IllegalStateException("Очередь пуста");
        }

        return stackPop.pop();
    }

    // Метод для получения элемента из очереди без удаления
    public T peek() {
        if (stackPop.isEmpty()) {
            while (!stackPush.isEmpty()) {
                stackPop.push(stackPush.pop());
            }
        }

        if (stackPop.isEmpty()) {
            throw new IllegalStateException("Очередь пуста");
        }

        return stackPop.peek();
    }

    // Метод для проверки, пуста ли очередь
    public boolean isEmpty() {
        return stackPush.isEmpty() && stackPop.isEmpty();
    }

    public static void main(String[] args) {
        QueueWithStacks<Integer> queue = new QueueWithStacks<>();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            logger.log(Level.INFO, """
                    Меню:
                    1. Добавить элемент в очередь (enqueue)
                    2. Удалить элемент из очереди (dequeue)
                    3. Посмотреть первый элемент в очереди (peek)
                    4. Проверить, пуста ли очередь (isEmpty)
                    5. Выйти
                    """);

            logger.log(Level.INFO, "Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    logger.log(Level.INFO, "Введите элемент для добавления в очередь: ");
                    int value = scanner.nextInt();
                    queue.enqueue(value);
                    logger.log(Level.INFO, "Элемент {0} добавлен в очередь.", value);
                }
                case 2 -> {
                    try {
                        int removedElement = queue.dequeue();
                        logger.log(Level.INFO, "Удален элемент: {0}", removedElement);
                    } catch (IllegalStateException e) {
                        logger.log(Level.WARNING, "Очередь пуста, невозможно удалить элемент.");
                    }
                }
                case 3 -> {
                    try {
                        int frontElement = queue.peek();
                        logger.log(Level.INFO, "Первый элемент в очереди: {0}", frontElement);
                    } catch (IllegalStateException e) {
                        logger.log(Level.WARNING, "Очередь пуста.");
                    }
                }
                case 4 -> {
                    if (queue.isEmpty()) {
                        logger.log(Level.INFO, "Очередь пуста.");
                    } else {
                        logger.log(Level.INFO, "Очередь не пуста.");
                    }
                }
                case 5 -> {
                    logger.log(Level.INFO, "Выход из программы.");
                    exit = true;
                }
                default -> logger.log(Level.WARNING, "Неверный выбор. Пожалуйста, выберите действие от 1 до 5.");
            }
        }
        scanner.close();
    }
}
