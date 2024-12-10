package org.example;

import java.util.Random;
import java.util.Scanner;

class Node {
    int[] keys;
    Node[] children;
    int keyCount;
    boolean isLeaf;

    Node(int t, boolean isLeaf) {
        this.isLeaf = isLeaf;
        keys = new int[2 * t - 1];
        children = new Node[2 * t];
        keyCount = 0;
    }
}

class TwoThreeTree {
    private Node root;
    private int t;
    private final Random rand;

    TwoThreeTree(int t) {
        this.t = t;
        this.rand = new Random(); 
        root = new Node(t, true);
    }

    void insert(int key) {
        Node r = root;
        if (r.keyCount == 2 * t - 1) {
            Node s = new Node(t, false);
            root = s;
            s.children[0] = r;
            splitChild(s, 0, r);
            nonFullInsert(s, key);
        } else {
            nonFullInsert(r, key);
        }
    }

    private void nonFullInsert(Node x, int k) {
        int i = x.keyCount - 1;
        if (x.isLeaf) {
            while (i >= 0 && x.keys[i] > k) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = k;
            x.keyCount = x.keyCount + 1;
        } else {
            while (i >= 0 && x.keys[i] > k) {
                i--;
            }
            if (x.children[i + 1].keyCount == 2 * t - 1) {
                splitChild(x, i + 1, x.children[i + 1]);
                if (x.keys[i + 1] < k) {
                    i++;
                }
            }
            nonFullInsert(x.children[i + 1], k);
        }
    }

    private void splitChild(Node x, int i, Node y) {
        Node z = new Node(t, y.isLeaf);
        z.keyCount = t - 1;
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
            }
        }
        y.keyCount = t - 1;
        for (int j = x.keyCount; j >= i + 1; j--) {
            x.children[j + 1] = x.children[j];
        }
        x.children[i + 1] = z;
        for (int j = x.keyCount - 1; j >= i; j--) {
            x.keys[j + 1] = x.keys[j];
        }
        x.keys[i] = y.keys[t - 1];
        x.keyCount = x.keyCount + 1;
    }

    int getAveragePathLength(int n) {
        for (int i = 0; i < n; i++) {
            insert(rand.nextInt(1000));
        }
        return calculateAveragePathLength(root, 0);
    }

    private int calculateAveragePathLength(Node node, int depth) {
        if (node == null) {
            return 0;
        }
        int totalDepth = depth * node.keyCount;
        for (int i = 0; i <= node.keyCount; i++) {
            totalDepth += calculateAveragePathLength(node.children[i], depth + 1);
        }
        return totalDepth;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Провести тесты (своё N)");
            System.out.println("2. Провести тесты (N = 1000)");
            System.out.println("3. Выйти");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите N: ");
                    int n = scanner.nextInt();
                    runTestsUser(n);
                    break;
                case 2:
                    runTests();
                    break;
                case 3:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void runTests() {
        int n = 1000;
        TwoThreeTree testTree = new TwoThreeTree(2);
        int averagePathLength = testTree.getAveragePathLength(n);
        System.out.println("Средняя длина пути в дереве после " + n + " случайных вставок: " + averagePathLength);
        System.out.println("Гипотеза: Средняя длина пути в дереве приблизительно равна log_2(N).");
    }

    private static void runTestsUser(int n) {
        TwoThreeTree testTree = new TwoThreeTree(2);
        int averagePathLength = testTree.getAveragePathLength(n);
        System.out.println("Средняя длина пути в дереве после " + n + " случайных вставок: " + averagePathLength);
        System.out.println("Гипотеза: Средняя длина пути в дереве приблизительно равна log_2(N).");
    }
}
