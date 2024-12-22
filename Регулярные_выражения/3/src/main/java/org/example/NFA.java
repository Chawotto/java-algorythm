package org.example;

import java.util.*;

public class NFA {
    private final char[] re;
    private final Digraph g;
    private final int m;
    private static final String FALSE_STRING = "false";

    public NFA(String regexp) {
        Deque<Integer> ops = new ArrayDeque<>();
        re = regexp.toCharArray();
        m = re.length;
        g = new Digraph(m + 1);

        for (int i = 0; i < m; i++) {
            int lp = i;

            if (re[i] == '(' || re[i] == '|') {
                ops.push(i);
            } else if (re[i] == ')') {
                int or = ops.pop();

                if (re[or] == '|') {
                    lp = ops.pop();
                    g.addEdge(lp, or + 1);
                    g.addEdge(or, i);
                } else {
                    lp = or;
                }
            } else if (re[i] == '[') {
                int rangeEnd = i;
                while (re[rangeEnd] != ']') rangeEnd++;
                g.addEdge(i, rangeEnd);
                i = rangeEnd;
            }

            if (i < m - 1 && re[i + 1] == '*') {
                g.addEdge(lp, i + 1);
                g.addEdge(i + 1, lp);
            }

            if (isOperator(re[i])) {
                g.addEdge(i, i + 1);
            }
        }
    }

    private boolean isOperator(char c) {
        return c == '(' || c == ')' || c == '*' || c == '|';
    }

    public boolean recognizes(String txt) {
        Bag<Integer> pc = initializePC();

        for (int i = 0; i < txt.length(); i++) {
            pc = updatePC(pc, txt.charAt(i));
        }

        return containsAcceptState(pc);
    }

    private Bag<Integer> initializePC() {
        Bag<Integer> pc = new Bag<>();
        DirectedDFS dfs = new DirectedDFS(g, 0);

        for (int v = 0; v < g.v(); v++) {
            if (dfs.marked(v)) pc.add(v);
        }
        return pc;
    }

    private Bag<Integer> updatePC(Bag<Integer> pc, char c) {
        Bag<Integer> match = new Bag<>();

        for (int v : pc) {
            if (v < m) {
                if (re[v] == c || re[v] == '.') {
                    match.add(v + 1);
                } else if (re[v] == '[') {
                    int rangeEnd = findRangeEnd(v);
                    if (isInRange(re, v, rangeEnd, c)) {
                        match.add(rangeEnd + 1);
                    }
                }
            }
        }

        return computeReachableStates(match);
    }

    private int findRangeEnd(int start) {
        int rangeEnd = start;
        while (re[rangeEnd] != ']') rangeEnd++;
        return rangeEnd;
    }

    private Bag<Integer> computeReachableStates(Bag<Integer> match) {
        Bag<Integer> pc = new Bag<>();
        DirectedDFS dfs = new DirectedDFS(g, match);

        for (int v = 0; v < g.v(); v++) {
            if (dfs.marked(v)) pc.add(v);
        }
        return pc;
    }

    private boolean containsAcceptState(Bag<Integer> pc) {
        for (int v : pc) {
            if (v == m) return true;
        }
        return false;
    }

    private boolean isInRange(char[] re, int start, int end, char c) {
        for (int i = start + 1; i < end; i += 3) {
            if (re[i + 1] == '-' && c >= re[i] && c <= re[i + 2]) {
                return true;
            }
        }
        return false;
    }

    private static class Digraph {
        private final int v;
        private final List<Integer>[] adj;

        public Digraph(int i) {
            this.v = i;
            adj = new List[i];
            for (int i1 = 0; i1 < i; i1++) {
                adj[i1] = new ArrayList<>();
            }
        }

        public void addEdge(int v, int w) {
            adj[v].add(w);
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
                if (!marked[s]) dfs(g, s);
            }
        }

        private void dfs(Digraph g, int v) {
            marked[v] = true;
            for (int w : g.adj(v)) {
                if (!marked[w]) dfs(g, w);
            }
        }

        public boolean marked(int v) {
            return marked[v];
        }
    }

    private static class Bag<Item> extends ArrayList<Item> {
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
                {"[A-Z]*", "HELLO", "true"},
                {"[a-z]*", "hello", "true"},
                {"[0-9]*", "12345", "true"},
                {"[A-Za-z]*", "HelloWorld", "true"},
                {"[A-Z]*", "hello", FALSE_STRING},
                {"[a-z]*", "HELLO", FALSE_STRING},
                {"[0-9]*", "abc123", FALSE_STRING}
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
