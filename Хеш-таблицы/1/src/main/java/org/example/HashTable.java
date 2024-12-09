package org.example;

import java.util.Arrays;

public class HashTable {
    private String[] table;
    private int size;
    private int count;

    public HashTable(int initialSize) {
        this.size = initialSize;
        this.table = new String[initialSize];
        this.count = 0;
    }

    private int hash(char key) {
        int k = key - 'A' + 1;
        return (11 * k) % size;
    }

    private void resize() {
        size *= 2;
        String[] newTable = new String[size];
        for (String item : table) {
            if (item != null) {
                insert(newTable, item.charAt(0));
            }
        }
        table = newTable;
    }

    private void insert(String[] table, char key) {
        int index = hash(key);
        while (table[index] != null) {
            index = (index + 1) % size;
        }
        table[index] = String.valueOf(key);
    }

    public void insert(char key) {
        if (count >= size / 2) {
            resize();
        }
        insert(table, key);
        count++;
    }

    public void printTable() {
        System.out.println(Arrays.toString(table));
    }

    public static void main(String[] args) {
        HashTable hashTable = new HashTable(4);
        char[] keys = {'E', 'A', 'S', 'Y', 'Q', 'U', 'T', 'I', 'O', 'N'};

        for (char key : keys) {
            hashTable.insert(key);
            System.out.println("После вставки " + key + ":");
            hashTable.printTable();
        }
    }
}
