package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Проверить текст на соответствие регулярному выражению");
            System.out.println("2. Запустить тесты");
            System.out.println("3. Выход");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> matchText(scanner);
                case 2 -> runTests();
                case 3 -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                default -> System.out.println("Некорректный выбор. Попробуйте ещё раз.");
            }
        }
    }

    private static void matchText(Scanner scanner) {
        System.out.print("Введите регулярное выражение: ");
        String regex = scanner.nextLine();
        NFA nfa = new NFA(regex);
        System.out.print("Введите текст для проверки: ");
        String text = scanner.nextLine();
        boolean result = nfa.recognizes(text);
        System.out.println("Результат: " + (result ? "соответствует" : "не соответствует"));
    }

    private static void runTests() {
        String regex1 = "(a|b)*c";
        String regex2 = "a*b+";
        String regex3 = "a(b|c)*d";

        String[] tests1 = {"aababc", "aabb", "abac", "b"};
        String[] tests2 = {"aab", "ab", "b", "a"};
        String[] tests3 = {"abcd", "ad", "abbcdd", "a"};

        NFA nfa1 = new NFA(regex1);
        NFA nfa2 = new NFA(regex2);
        NFA nfa3 = new NFA(regex3);

        System.out.println("Тесты для регулярного выражения: " + regex1);
        runTest(regex1, tests1[0], nfa1);  // +
        runTest(regex1, tests1[1], nfa1);  // -
        runTest(regex1, tests1[2], nfa1);  // +
        runTest(regex1, tests1[3], nfa1);  // -

        System.out.println("Тесты для регулярного выражения: " + regex2);
        runTest(regex2, tests2[0], nfa2);  // +
        runTest(regex2, tests2[1], nfa2);  // +
        runTest(regex2, tests2[2], nfa2);  // +
        runTest(regex2, tests2[3], nfa2);  // -

        System.out.println("Тесты для регулярного выражения: " + regex3);
        runTest(regex3, tests3[0], nfa3);  // +
        runTest(regex3, tests3[1], nfa3);  // +
        runTest(regex3, tests3[2], nfa3);  // -
        runTest(regex3, tests3[3], nfa3);  // -
    }

    private static void runTest(String regex, String text, NFA nfa) {
        System.out.println("Тест: Регулярное выражение: " + regex + ", Текст: " + text);
        System.out.println("Результат: " + (nfa.recognizes(text) ? "соответствует" : "не соответствует"));
    }
}

class NFA {
    private final char[] regex;
    private final Digraph graph;
    private final int states;

    public NFA(String pattern) {
        Deque<Integer> operators = new ArrayDeque<>();
        regex = pattern.toCharArray();
        states = regex.length;
        graph = new Digraph(states + 1);

        for (int i = 0; i < states; i++) {
            int left = i;

            if (regex[i] == '(' || regex[i] == '|') {
                operators.push(i);
            } else if (regex[i] == ')') {
                int or = operators.pop();

                if (regex[or] == '|') {
                    left = operators.pop();
                    graph.addEdge(left, or + 1);
                    graph.addEdge(or, i);
                } else {
                    left = or;
                }
            }

            if (i < states - 1 && regex[i + 1] == '*') {
                graph.addEdge(left, i + 1);
                graph.addEdge(i + 1, left);
            }

            if (regex[i] == '(' || regex[i] == '*' || regex[i] == ')') {
                graph.addEdge(i, i + 1);
            }
        }
    }

    public boolean recognizes(String text) {
        Set<Integer> currentStates = getInitialStates();

        for (int i = 0; i < text.length(); i++) {
            currentStates = getNextStates(currentStates, text.charAt(i));
        }

        return isFinalStateReached(currentStates);
    }

    private Set<Integer> getInitialStates() {
        Set<Integer> initialStates = new HashSet<>();
        DirectedDFS dfs = new DirectedDFS(graph, 0);

        for (int v = 0; v < graph.vertexCount(); v++) {
            if (dfs.marked(v)) {
                initialStates.add(v);
            }
        }
        return initialStates;
    }

    private Set<Integer> getNextStates(Set<Integer> currentStates, char currentChar) {
        Set<Integer> nextStates = new HashSet<>();

        for (int state : currentStates) {
            if (state < states && (regex[state] == currentChar || regex[state] == '.')) {
                nextStates.add(state + 1);
            }
        }

        DirectedDFS dfs = new DirectedDFS(graph, nextStates);
        Set<Integer> updatedStates = new HashSet<>();
        for (int v = 0; v < graph.vertexCount(); v++) {
            if (dfs.marked(v)) {
                updatedStates.add(v);
            }
        }

        return updatedStates;
    }

    private boolean isFinalStateReached(Set<Integer> currentStates) {
        for (int state : currentStates) {
            if (state == states) {
                return true;
            }
        }
        return false;
    }
}

class Digraph {
    private final List<Integer>[] adjacencyList;

    public Digraph(int vertices) {
        adjacencyList = new List[vertices];
        for (int v = 0; v < vertices; v++) {
            adjacencyList[v] = new ArrayList<>();
        }
    }

    public void addEdge(int v, int w) {
        adjacencyList[v].add(w);
    }

    public Iterable<Integer> adjacent(int v) {
        return adjacencyList[v];
    }

    public int vertexCount() {
        return adjacencyList.length;
    }
}

class DirectedDFS {
    private final boolean[] marked;

    public DirectedDFS(Digraph graph, int source) {
        marked = new boolean[graph.vertexCount()];
        dfs(graph, source);
    }

    public DirectedDFS(Digraph graph, Iterable<Integer> sources) {
        marked = new boolean[graph.vertexCount()];
        for (int s : sources) {
            if (!marked[s]) {
                dfs(graph, s);
            }
        }
    }

    private void dfs(Digraph graph, int v) {
        marked[v] = true;
        for (int w : graph.adjacent(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }
    }

    public boolean marked(int v) {
        return marked[v];
    }
}
