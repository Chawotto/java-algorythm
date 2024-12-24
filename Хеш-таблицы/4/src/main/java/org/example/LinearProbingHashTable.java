package org.example;

import java.util.Random;
import java.util.Scanner;

public class LinearProbingHashTable {
    private int[] table;
    private int size;

    public LinearProbingHashTable(int size) {
        this.size = size;
        this.table = new int[size];
        for (int i = 0; i < size; i++) {
            table[i] = -1;
        }
    }

    public void insert(int key) {
        int index = key % size;
        while (table[index] != -1) {
            index = (index + 1) % size;
        }
        table[index] = key;
    }

    public double calculateAverageProbeLength() {
        int totalProbes = 0;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (table[i] != -1) {
                int probeLength = 1;
                int index = table[i] % size;
                while (index != i) {
                    probeLength++;
                    index = (index + 1) % size;
                }
                totalProbes += probeLength;
                count++;
            }
        }
        double ccount = 0;
        if (count != 0) {
            ccount = (double) totalProbes / count;
        }
        return ccount;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int[] sizes = {10000, 100000, 1000000, 10000000};
        int choice = 0;

        while (choice != 2) {
            System.out.println("Выберите действие:");
            System.out.println("1. Запустить тесты");
            System.out.println("2. Выйти");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    for (int size : sizes) {
                        LinearProbingHashTable table = new LinearProbingHashTable(size);
                        int numKeys = size / 2;
                        for (int i = 0; i < numKeys; i++) {
                            int key = random.nextInt(size * 10);
                            table.insert(key);
                        }
                        double averageProbeLength = table.calculateAverageProbeLength();
                        System.out.println("Размер таблицы: " + size);
                        System.out.println("Средняя стоимость промахов: " + averageProbeLength);

                    }
                    break;

                case 2: {

                    break;
                }
                default: {
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            }
        }

    }
}
