package org.example;

import java.util.*;

public class Main {
    private static final String TEST_WORLD = "Тесты для регулярного выражения: ";
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
        String regex4 = "[^abc]*";

        String[] tests1 = {"aababc", "aabb", "abac", "b"};
        String[] tests2 = {"aab", "ab", "b", "a"};
        String[] tests3 = {"abcd", "ad", "abbcdd", "a"};
        String[] tests4 = {"kghd", "hfura", "uioerth", "gfbclas"};

        NFA nfa1 = new NFA(regex1);
        NFA nfa2 = new NFA(regex2);
        NFA nfa3 = new NFA(regex3);
        NFA nfa4 = new NFA(regex4);

        System.out.println(TEST_WORLD + regex1);
        runTest(regex1, tests1[0], nfa1);  // +
        runTest(regex1, tests1[1], nfa1);  // -
        runTest(regex1, tests1[2], nfa1);  // +
        runTest(regex1, tests1[3], nfa1);  // -

        System.out.println(TEST_WORLD + regex2);
        runTest(regex2, tests2[0], nfa2);  // +
        runTest(regex2, tests2[1], nfa2);  // +
        runTest(regex2, tests2[2], nfa2);  // +
        runTest(regex2, tests2[3], nfa2);  // -

        System.out.println(TEST_WORLD + regex3);
        runTest(regex3, tests3[0], nfa3);  // +
        runTest(regex3, tests3[1], nfa3);  // +
        runTest(regex3, tests3[2], nfa3);  // -
        runTest(regex3, tests3[3], nfa3);  // -

        System.out.println(TEST_WORLD + regex4);
        runTest(regex4, tests4[0], nfa4);  // +
        runTest(regex4, tests4[1], nfa4);  // -
        runTest(regex4, tests4[2], nfa4);  // +
        runTest(regex4, tests4[3], nfa4);  // -
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
    private final Set<Integer> complementStates;

    public NFA(String pattern) {
        complementStates = new HashSet<>();
        regex = pattern.toCharArray();
        states = regex.length;
        graph = new Digraph(states + 1);
        buildGraph();
    }

    private void buildGraph() {
        Deque<Integer> operators = new ArrayDeque<>();
        int i = 0;
        while (i < states) {
            i = processState(i, operators) + 1;
        }
    }

    private int processState(int i, Deque<Integer> operators) {
        int left = i;
        if (isOperator(regex[i])) {
            operators.push(i);
        } else if (regex[i] == ')') {
            left = handleClosingParenthesis(i, operators);
        } else if (isComplementStart(i)) {
            complementStates.add(i);
            i = findComplementEnd(i);
            if (i == states) {
                throw new IllegalArgumentException("Некорректный описатель дополнения");
            }
        }

        handleKleeneStar(i, left);
        addTransitionForSpecialChars(i);
        return i;
    }

    private boolean isOperator(char c) {
        return c == '(' || c == '|';
    }

    private int handleClosingParenthesis(int i, Deque<Integer> operators) {
        int or = operators.pop();
        int left = or;
        if (regex[or] == '|') {
            left = operators.pop();
            graph.addEdge(left, or + 1);
            graph.addEdge(or, i);
        }
        return left;
    }

    private boolean isComplementStart(int i) {
        return regex[i] == '[' && i + 1 < states && regex[i + 1] == '^';
    }

    private int findComplementEnd(int i) {
        for (int j = i; j < states; j++) {
            if (regex[j] == ']') {
                return j;
            }
        }
        return states;
    }

    private void handleKleeneStar(int i, int left) {
        if (i < states - 1 && regex[i + 1] == '*') {
            graph.addEdge(left, i + 1);
            graph.addEdge(i + 1, left);
        }
    }

    private void addTransitionForSpecialChars(int i) {
        if (regex[i] == '(' || regex[i] == '*' || regex[i] == ')' || regex[i] == ']') {
            graph.addEdge(i, i + 1);
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
            if (state >= states) continue;
            processStateTransition(state, currentChar, nextStates);
        }
        return updateStatesWithDFS(nextStates);
    }

    private void processStateTransition(int state, char currentChar, Set<Integer> nextStates) {
        if (complementStates.contains(state) && regex[state] == '[') {
            handleComplementState(state, currentChar, nextStates);
        } else if (regex[state] == currentChar || regex[state] == '.') {
            nextStates.add(state + 1);
        }
    }

    private void handleComplementState(int state, char currentChar, Set<Integer> nextStates) {
        int i = state + 2;
        for (int j = i; j < states && regex[j] != ']'; j++) {
            if (regex[j] == currentChar) {
                return;
            }
        }
        if (i < states && regex[i - 1] == '^' && regex[i - 2] == '[') {
            int end = findComplementEnd(state);
            if (end < states && regex[end] == ']') {
                nextStates.add(end + 1);
            }
        }
    }

    private Set<Integer> updateStatesWithDFS(Set<Integer> nextStates) {
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
