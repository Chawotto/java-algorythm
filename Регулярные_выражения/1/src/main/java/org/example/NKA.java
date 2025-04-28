package org.example;

import java.util.*;

public class NKA {
    static class State {
        int id;
        Map<Character, List<State>> transitions = new HashMap<>();
        boolean isFinal;

        State(int id) {
            this.id = id;
        }

        void addTransition(char symbol, State nextState) {
            transitions.computeIfAbsent(symbol, k -> new ArrayList<>()).add(nextState);
        }

        void addEpsilonTransition(State nextState) {
            addTransition('\0', nextState);
        }
    }

    static class Automaton {
        State startState;
        List<State> states = new ArrayList<>();

        Automaton(State startState) {
            this.startState = startState;
        }

        void addState(State state) {
            states.add(state);
        }

        boolean matches(String input) {
            Set<State> currentStates = new HashSet<>();
            currentStates.add(startState);
            currentStates = epsilonClosure(currentStates);

            for (char c : input.toCharArray()) {
                currentStates = move(currentStates, c);
                currentStates = epsilonClosure(currentStates);
            }

            return currentStates.stream().anyMatch(state -> state.isFinal);
        }

        private Set<State> epsilonClosure(Set<State> states) {
            Set<State> closure = new HashSet<>(states);
            Deque<State> stack = new ArrayDeque<>(states);

            while (!stack.isEmpty()) {
                State state = stack.pop();
                List<State> epsilonTransitions = state.transitions.getOrDefault('\0', Collections.emptyList());
                for (State nextState : epsilonTransitions) {
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }

            return closure;
        }

        private Set<State> move(Set<State> states, char symbol) {
            Set<State> nextStates = new HashSet<>();
            for (State state : states) {
                List<State> transitions = state.transitions.getOrDefault(symbol,
                        state.transitions.getOrDefault('.', Collections.emptyList()));
                nextStates.addAll(transitions);
            }
            return nextStates;
        }
    }

    private char[] re;
    private List<State> states;
    private Automaton automaton;

    public NKA(String regexp) {
        re = regexp.toCharArray();
        states = new ArrayList<>();
        Deque<Integer> ops = new ArrayDeque<>();
        Deque<Integer> orSplits = new ArrayDeque<>();
        int m = re.length;

        initializeStates(m);
        processRegularExpression(m, ops, orSplits);
        finalizeAutomaton();
    }

    private void initializeStates(int m) {
        for (int i = 0; i <= m; i++) {
            states.add(new State(i));
        }
        states.get(m).isFinal = true;
    }

    private void processRegularExpression(int m, Deque<Integer> ops, Deque<Integer> orSplits) {
        for (int i = 0; i < m; i++) {
            int lp = i;

            if (re[i] == '(') {
                handleOpenParenthesis(i, ops, orSplits);
            } else if (re[i] == '|') {
                ops.push(i);
            } else if (re[i] == ')') {
                lp = handleCloseParenthesis(i, ops, orSplits);
            }

            if (i < m - 1 && re[i + 1] == '*') {
                handleStarOperator(i, lp);
            }

            addTransitionForCurrentState(i);
        }
    }

    private void handleOpenParenthesis(int i, Deque<Integer> ops, Deque<Integer> orSplits) {
        ops.push(i);
        orSplits.push(i);
    }

    private int handleCloseParenthesis(int i, Deque<Integer> ops, Deque<Integer> orSplits) {
        List<Integer> orIndices = new ArrayList<>();
        while (!ops.isEmpty() && re[ops.peek()] == '|') {
            orIndices.add(ops.pop());
        }
        int lp = ops.isEmpty() ? i : ops.pop();
        if (!orSplits.isEmpty()) {
            orSplits.pop();
        }

        if (!orIndices.isEmpty()) {
            processOrOperator(lp, i, orIndices);
        }
        return lp;
    }

    private void processOrOperator(int lp, int i, List<Integer> orIndices) {
        List<Integer> splitPoints = new ArrayList<>();
        splitPoints.add(lp + 1);
        for (int or : orIndices) {
            splitPoints.add(or + 1);
        }

        for (int start : splitPoints) {
            states.get(lp).addEpsilonTransition(states.get(start));
        }

        int lastSplit = lp + 1;
        for (int or : orIndices) {
            connectStates(lastSplit, or);
            states.get(or).addEpsilonTransition(states.get(i));
            lastSplit = or + 1;
        }
        connectStates(lastSplit, i);
    }

    private void connectStates(int start, int end) {
        for (int j = start; j < end; j++) {
            if (isLiteralCharacter(re[j])) {
                states.get(j).addTransition(re[j], states.get(j + 1));
            } else {
                states.get(j).addEpsilonTransition(states.get(j + 1));
            }
        }
    }

    private void handleStarOperator(int i, int lp) {
        states.get(lp).addEpsilonTransition(states.get(i + 2));
        states.get(i + 1).addEpsilonTransition(states.get(lp));
    }

    private void addTransitionForCurrentState(int i) {
        if (isLiteralCharacter(re[i])) {
            states.get(i).addTransition(re[i], states.get(i + 1));
        } else {
            states.get(i).addEpsilonTransition(states.get(i + 1));
        }
    }

    private boolean isLiteralCharacter(char c) {
        return c != '(' && c != ')' && c != '|' && c != '*';
    }

    private void finalizeAutomaton() {
        automaton = new Automaton(states.get(0));
        states.forEach(automaton::addState);
    }

    static void runTests(Automaton automaton) {
        String[] testStrings = {
                "ABG",
                "AABCFG",
                "ABCFDG",
                "ABCFDFFFFFG",
                "ABCFDFG",
                "ABCFGF",
                "AAAABCFDDG",
                "ABCCFFFG"
        };

        boolean[] expectedResults = {
                true,
                true,
                false,
                false,
                true,
                false,
                false,
                false
        };

        System.out.println("Тестирование НКА:");
        for (int i = 0; i < testStrings.length; i++) {
            String testString = testStrings[i];
            boolean expectedResult = expectedResults[i];
            boolean result = automaton.matches(testString);

            System.out.printf("Тест %d: строка \"%s\" - %s (ожидалось: %s)%n",
                    i + 1, testString, result ? "соответствует" : "не соответствует",
                    expectedResult ? "соответствует" : "не соответствует"
            );

            if (result != expectedResult) {
                System.out.printf("Ошибка в тесте %d!%n", i + 1);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Регулярное выражение: .*AB((C|D|E)F)*G");
        NKA nka = new NKA(".*AB((C|D|E)F)*G");
        runTests(nka.automaton);
    }
}