package org.example;

import java.util.LinkedList;
import java.util.Random;

public class HashTableChain {
    private LinkedList<Integer>[] table;
    private int size;

    public HashTableChain(int size) {
        this.size = size;
        table = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public void insert(int key) {
        int index = Math.abs(key) % size;
        table[index].add(key);
    }

    public int getMinChainLength() {
        int minLength = Integer.MAX_VALUE;
        for (LinkedList<Integer> chain : table) {
            if (chain.size() < minLength) {
                minLength = chain.size();
            }
        }
        return minLength;
    }

    public int getMaxChainLength() {
        int maxLength = Integer.MIN_VALUE;
        for (LinkedList<Integer> chain : table) {
            if (chain.size() > maxLength) {
                maxLength = chain.size();
            }
        }
        return maxLength;
    }

    public static void main(String[] args) {
        int[] nValues = {10000, 100000, 1000000, 10000000};
        Random random = new Random();

        for (int N : nValues) {
            HashTableChain hashTable = new HashTableChain(N / 100);
            for (int i = 0; i < N; i++) {
                int key = random.nextInt();
                hashTable.insert(key);
            }

            int minChainLength = hashTable.getMinChainLength();
            int maxChainLength = hashTable.getMaxChainLength();

            System.out.println("Для N = " + N + ":");
            System.out.println("Длина самого короткого списка: " + minChainLength);
            System.out.println("Длина самого длинного списка: " + maxChainLength);
            System.out.println();
        }
    }
}
