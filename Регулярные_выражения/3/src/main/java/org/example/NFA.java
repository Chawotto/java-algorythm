package org.example;

import java.util.*;

public class NFA {
    private final char[] regexpArr;
    private final Digraph digraph;
    private final int regexpLen;
    private static final String FALSE_STRING = "false";

    public NFA(String regexp) {
        Deque<Integer> ops = new ArrayDeque<>();
        regexpArr = regexp.toCharArray();
        regexpLen = regexpArr.length;
        digraph = new Digraph(regexpLen + 1);

        int i = 0;
        while (i < regexpLen) {
            int lp = i;

            if (regexpArr[i] == '[') {
                i = processRange(i);
            } else {
                lp = processOperator(ops, i, lp);

                if (i < regexpLen - 1 && regexpArr[i + 1] == '*') {
                    digraph.addEdge(lp, i + 1);
                    digraph.addEdge(i + 1, lp);
                }

                if (isOperator(regexpArr[i])) {
                    digraph.addEdge(i, i + 1);
                }
                i++;
            }
        }
    }

    private int processRange(int start) {
        int rangeEnd = start;
        while (rangeEnd < regexpLen && regexpArr[rangeEnd] != ']') {
            rangeEnd++;
        }
        if (rangeEnd < regexpLen) {
            digraph.addEdge(start, rangeEnd);
        }
        return rangeEnd + 1;
    }

    private int processOperator(Deque<Integer> ops, int i, int lp) {
        if (regexpArr[i] == '(' || regexpArr[i] == '|') {
            ops.push(i);
        } else if (regexpArr[i] == ')') {
            int or = ops.pop();
            if (regexpArr[or] == '|') {
                lp = ops.pop();
                digraph.addEdge(lp, or + 1);
                digraph.addEdge(or, i);
            } else {
                lp = or;
            }
        }
        return lp;
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
        DirectedDFS dfs = new DirectedDFS(digraph, 0);

        for (int v = 0; v < digraph.v(); v++) {
            if (dfs.marked(v)) pc.add(v);
        }
        return pc;
    }

    private Bag<Integer> updatePC(Bag<Integer> pc, char c) {
        Bag<Integer> match = new Bag<>();

        for (int v : pc) {
            if (v < regexpLen) {
                if (regexpArr[v] == c || regexpArr[v] == '.') {
                    match.add(v + 1);
                } else if (regexpArr[v] == '[') {
                    int rangeEnd = findRangeEnd(v);
                    if (isInRange(regexpArr, v, rangeEnd, c)) {
                        match.add(rangeEnd + 1);
                    }
                }
            }
        }

        return computeReachableStates(match);
    }

    private int findRangeEnd(int start) {
        int rangeEnd = start;
        while (rangeEnd < regexpLen && regexpArr[rangeEnd] != ']') rangeEnd++;
        return rangeEnd;
    }

    private Bag<Integer> computeReachableStates(Bag<Integer> match) {
        Bag<Integer> pc = new Bag<>();
        DirectedDFS dfs = new DirectedDFS(digraph, match);

        for (int v = 0; v < digraph.v(); v++) {
            if (dfs.marked(v)) pc.add(v);
        }
        return pc;
    }

    private boolean containsAcceptState(Bag<Integer> pc) {
        for (int v : pc) {
            if (v == regexpLen) return true;
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

    private static class Bag<I> extends ArrayList<I> {
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