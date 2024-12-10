package org.example;

import java.util.Scanner;

enum Color {
    RED, BLACK
}

class Node {
    int data;
    Color color;
    Node left;
    Node right;
    Node parent;

    Node(int data, Color color, Node parent, Node left, Node right) {
        this.data = data;
        this.color = color;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }
}

class RedBlackTree {
    private Node tnull;
    Node root;

    public RedBlackTree() {
        tnull = new Node(0, Color.BLACK, null, null, null);
        root = tnull;
    }

    public void insert(int key) {
        Node node = new Node(key, Color.RED, null, tnull, tnull);
       
        Node y = null;
        Node x = this.root;

        while (x != tnull) {
            y = x;
            if (node.data < x.data) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data < y.data) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = Color.BLACK;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    private void fixInsert(Node k) {
        while (k.parent != null && k.parent.color == Color.RED) {
            if (k.parent == k.parent.parent.right) {
                fixInsertCase1(k);
            } else {
                fixInsertCase2(k);
            }
        }
        root.color = Color.BLACK;
    }

    private void fixInsertCase1(Node k) {
        Node u = k.parent.parent.left;
        if (u.color == Color.RED) {
            u.color = Color.BLACK;
            k.parent.color = Color.BLACK;
            k.parent.parent.color = Color.RED;

        } else {
            if (k == k.parent.left) {
                k = k.parent;
                rightRotate(k);
            }
            k.parent.color = Color.BLACK;
            k.parent.parent.color = Color.RED;
            leftRotate(k.parent.parent);
        }
    }

    private void fixInsertCase2(Node k) {
        Node u = k.parent.parent.right;
        if (u.color == Color.RED) {
            u.color = Color.BLACK;
            k.parent.color = Color.BLACK;
            k.parent.parent.color = Color.RED;

        } else {
            if (k == k.parent.right) {
                k = k.parent;
                leftRotate(k);
            }
            k.parent.color = Color.BLACK;
            k.parent.parent.color = Color.RED;
            rightRotate(k.parent.parent);
        }
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != tnull) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != tnull) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    public void delete(int data) {
        deleteNode(this.root, data);
    }

    private Node searchNode(Node root, int key) {
        Node node = root;
        while (node != tnull) {
            if (node.data == key) {
                return node;
            } else {
                node = (node.data > key) ? node.left : node.right;
            }
        }
        return tnull;
    }

    private void deleteNode(Node root, int key) {
        Node node = searchNode(root, key);
        if (node == tnull) return;

        Node y = node;
        Color yOriginalColor = y.color;
        Node x;

        if (node.left == tnull) {
            x = node.right;
            rbTransplant(node, node.right);
        } else if (node.right == tnull) {
            x = node.left;
            rbTransplant(node, node.left);
        } else {
            y = minimum(node.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == node) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            rbTransplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }
        if (yOriginalColor == Color.BLACK) {
            fixDelete(x);
        }
    }

    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private void fixDelete(Node x) {
        while (x != root && x.color == Color.BLACK) {
            if (x == x.parent.left) {
                fixDeleteCase1(x);
            } else {
                fixDeleteCase2(x);
            }
        }
        x.color = Color.BLACK;
    }

    private void fixDeleteCase1(Node x) {
        Node w = x.parent.right;
        if (w.color == Color.RED) {
            w.color = Color.BLACK;
            x.parent.color = Color.RED;
            leftRotate(x.parent);
            w = x.parent.right;
        }
        if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
            w.color = Color.RED;

        } else {
            if (w.right.color == Color.BLACK) {
                w.left.color = Color.BLACK;
                w.color = Color.RED;
                rightRotate(w);
                w = x.parent.right;
            }
            w.color = x.parent.color;
            x.parent.color = Color.BLACK;
            w.right.color = Color.BLACK;
            leftRotate(x.parent);

        }
    }

    private void fixDeleteCase2(Node x) {
        Node w = x.parent.left;
        if (w.color == Color.RED) {
            w.color = Color.BLACK;
            x.parent.color = Color.RED;
            rightRotate(x.parent);
            w = x.parent.left;
        }
        if (w.right.color == Color.BLACK && w.left.color == Color.BLACK) {
            w.color = Color.RED;

        } else {
            if (w.left.color == Color.BLACK) {
                w.right.color = Color.BLACK;
                w.color = Color.RED;
                leftRotate(w);
                w = x.parent.left;
            }
            w.color = x.parent.color;
            x.parent.color = Color.BLACK;
            w.left.color = Color.BLACK;
            rightRotate(x.parent);

        }
    }

    Node minimum(Node node) {
        while (node.left != tnull) {
            node = node.left;
        }
        return node;
    }

    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(Node node, String indent, boolean last) {
        if (node != tnull) {
            System.out.print(indent);
            System.out.print(last ? "R----" : "L----");
            System.out.print(node.data + "(" + node.color + ")\n");
            indent += last ? "     " : "|    ";
            printTree(node.left, indent, false);
            printTree(node.right, indent, true);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите операцию:");
            System.out.println("1. Вставить ключ");
            System.out.println("2. Удалить наименьший ключ");
            System.out.println("3. Визуализировать дерево");
            System.out.println("4. Запустить тест");
            System.out.println("5. Выйти");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите ключ для вставки: ");
                    int key = scanner.nextInt();
                    rbt.insert(key);
                    break;
                case 2:
                    rbt.delete(rbt.minimum(rbt.root).data);
                    break;
                case 3:
                    rbt.printTree();
                    break;
                case 4:
                    System.out.println("Запустить тест.");
                    runTest();
                    break;
                case 5:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void runTest() {
        RedBlackTree rbt = new RedBlackTree();

        int[] initialKeys = {10, 20, 30, 15, 25, 5};
        for (int key : initialKeys) {
            rbt.insert(key);
        }

        System.out.println("Исходное дерево:");
        rbt.printTree();

        int minKey = rbt.minimum(rbt.root).data - 1;
        rbt.insert(minKey);

        System.out.println("\nДерево после вставки наименьшего ключа:");
        rbt.printTree();

        rbt.delete(rbt.minimum(rbt.root).data);

        System.out.println("\nДерево после удаления наименьшего ключа:");
        rbt.printTree();
    }
}
