package org.example;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResizingArrayQueueOfStrings {
    private static final Logger logger = Logger.getLogger(ResizingArrayQueueOfStrings.class.getName());

    private String[] queue;
    private int head;
    private int tail;
    private int size;

    public ResizingArrayQueueOfStrings() {
        queue = new String[2];
        head = 0;
        tail = 0;
        size = 0;
    }

    public void enqueue(String item) {
        if (size == queue.length) {
            resize(2 * queue.length);
        }
        queue[tail] = item;
        tail = (tail + 1) % queue.length;
        size++;

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Элемент добавлен: %s", item));
        }
    }

    public String dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Очередь пуста");
        }
        String item = queue[head];
        queue[head] = null;
        head = (head + 1) % queue.length;
        size--;

        if (size > 0 && size == queue.length / 4) {
            resize(queue.length / 2);
        }

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Элемент удален: %s", item));
        }

        return item;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        String[] newQueue = new String[capacity];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[(head + i) % queue.length];
        }
        queue = newQueue;
        head = 0;
        tail = size;

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Размер массива изменен на: %d", capacity));
        }
    }

    public void displayQueue() {
        if (isEmpty()) {
            logger.log(Level.INFO, "Очередь пуста");
        } else {
            StringBuilder queueContent = new StringBuilder("Содержимое очереди: ");
            for (int i = 0; i < size; i++) {
                queueContent.append(queue[(head + i) % queue.length]).append(" ");
            }
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, queueContent.toString());
            }
        }
    }

    public static void main(String[] args) {
        ResizingArrayQueueOfStrings queue = new ResizingArrayQueueOfStrings();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            logger.log(Level.INFO, """
                    Выберите операцию:
                    1 - Добавить элемент в очередь
                    2 - Удалить элемент из очереди
                    3 - Показать содержимое очереди
                    4 - Выйти
                    """);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    logger.log(Level.INFO, "Введите элемент для добавления:");
                    String item = scanner.nextLine();
                    queue.enqueue(item);
                }
                case 2 -> {
                    try {
                        String removedItem = queue.dequeue();
                        if (logger.isLoggable(Level.INFO)) {
                            logger.log(Level.INFO, String.format("Удален элемент: %s", removedItem));
                        }
                    } catch (NoSuchElementException e) {
                        logger.log(Level.WARNING, e.getMessage());
                    }
                }
                case 3 -> queue.displayQueue();
                case 4 -> {
                    scanner.close();
                    logger.log(Level.INFO, "Выход из программы.");
                    return;
                }
                default -> logger.log(Level.WARNING, "Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }
}
