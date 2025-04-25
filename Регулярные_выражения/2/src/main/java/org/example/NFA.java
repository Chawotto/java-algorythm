package org.example;

import java.util.*;

public class NFA {
    private final char[] regexpArr;
    private final Digraph digraph;
    private final int regexpLen;

    public NFA(String regexp) {
        Deque<Integer> ops = new ArrayDeque<>();
        regexpArr = regexp.toCharArray();
        regexpLen = regexpArr.length;
        digraph = new Digraph(regexpLen + 1);

        for (int i = 0; i < regexpLen; i++) {
            int lp = i;

            switch (regexpArr[i]) {
                case '(', '|':
                    ops.push(i);
                    break;
                case ')':
                    int or = ops.pop();
                    if (regexpArr[or] == '|') {
                        lp = ops.pop();
                        digraph.addEdge(lp, or + 1);
                        digraph.addEdge(or, i);
                    } else {
                        lp = or;
                    }
                    break;
                default:
                    break;
            }

            if (i < regexpLen - 1 && regexpArr[i + 1] == '*') {
                digraph.addEdge(lp, i + 1);
                digraph.addEdge(i + 1, lp);
            }

            if (regexpArr[i] == '(' || regexpArr[i] == ')' || regexpArr[i] == '*' || regexpArr[i] == '|') {
                digraph.addEdge(i, i + 1);
            }
        }
    }

    public boolean recognizes(String txt) {
        Bag<Integer> pc = initializePc();

        for (int i = 0; i < txt.length(); i++) {
            pc = updatePc(pc, txt.charAt(i));
        }

        return pc.contains(regexpLen);
    }

    private Bag<Integer> initializePc() {
        Bag<Integer> pc = new Bag<>();
        DirectedDFS dfs = new DirectedDFS(digraph, 0);

        for (int v = 0; v < digraph.v(); v++) {
            if (dfs.marked(v)) {
                pc.add(v);
            }
        }
        return pc;
    }

    private Bag<Integer> updatePc(Bag<Integer> pc, char c) {
        Bag<Integer> match = new Bag<>();

        for (int v : pc) {
            if (v < regexpLen && (regexpArr[v] == c || regexpArr[v] == '.')) {
                match.add(v + 1);
            }
        }

        pc = new Bag<>();
        DirectedDFS dfs = new DirectedDFS(digraph, match);

        for (int v = 0; v < digraph.v(); v++) {
            if (dfs.marked(v)) {
                pc.add(v);
            }
        }
        return pc;
    }

    private static class Digraph {
        private final int v;
        private int e;
        private final List<Integer>[] adj;

        public Digraph(int v) {
            this.v = v;
            this.e = 0;
            adj = new List[v];
            for (int i = 0; i < v; i++) {
                adj[i] = new ArrayList<>();
            }
        }

        public void addEdge(int v, int w) {
            adj[v].add(w);
            e++;
        }

        public Iterable<Integer> adj(int v) {
            return adj[v];
        }

        public int v() {
            return v;
        }
    }

    private static class DirectedDFS {
        private final boolean[] marked;

        public DirectedDFS(Digraph g, int s) {
            marked = new boolean[g.v()];
            dfs(g, s);
        }

        public DirectedDFS(Digraph g, Iterable<Integer> sources) {
            marked = new boolean[g.v()];
            for (int s : sources) {
                if (!marked[s]) {
                    dfs(g, s);
                }
            }
        }

        private void dfs(Digraph g, int v) {
            marked[v] = true;
            for (int w : g.adj(v)) {
                if (!marked[w]) {
                    dfs(g, w);
                }
            }
        }

        public boolean marked(int v) {
            return marked[v];
        }
    }

    private static class Bag<N> extends ArrayList<N> {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (choice != 3) {
            System.out.println("Меню программы:");
            System.out.println("1. Проверить строку на соответствие регулярному выражению");
            System.out.println("2. Запустить тесты");
            System.out.println("3. Выход");
            System.out.print("Выберите пункт меню: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Введите регулярное выражение: ");
                    String regexp = scanner.nextLine();
                    System.out.print("Введите строку для проверки: ");
                    String txt = scanner.nextLine();
                    NFA nfa = new NFA(regexp);

                    if (nfa.recognizes(txt)) {
                        System.out.println("Строка соответствует регулярному выражению.");
                    } else {
                        System.out.println("Строка не соответствует регулярному выражению.");
                    }
                }
                case 2 -> runTests();
                case 3 -> System.out.println("Выход из программы.");
                default -> System.out.println("Неверный пункт меню. Попробуйте снова.");
            }
        }
    }

    private static void runTests() {
        String[][] tests = {
                {"(A|B)*", "ABABAB", "true"},
                {"A*B", "AAAB", "true"},
                {"A.B", "ACB", "true"},
                {"A.B", "AB", "false"},
                {"(A|B)*C", "ABABAC", "true"},
                {"(A|B)*C", "ABABA", "false"}
        };

        for (String[] test : tests) {
            String regexp = test[0];
            String txt = test[1];
            boolean expected = Boolean.parseBoolean(test[2]);

            NFA nfa = new NFA(regexp);
            boolean result = nfa.recognizes(txt);

            System.out.println("Регулярное выражение: " + regexp);
            System.out.println("Строка: " + txt);
            System.out.println("Ожидаемый результат: " + expected);
            System.out.println("Полученный результат: " + result);
            System.out.println(result == expected ? "Тест пройден." : "Тест НЕ пройден.");
            System.out.println();
        }
    }
}
