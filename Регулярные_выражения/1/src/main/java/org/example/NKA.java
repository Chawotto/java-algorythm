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
                List<State> transitions = state.transitions.getOrDefault(symbol, Collections.emptyList());
                nextStates.addAll(transitions);
            }
            return nextStates;
        }
    }

    static Automaton buildNKA() {
        State s0 = new State(0);
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        State s4 = new State(4);
        State s5 = new State(5);
        State s6 = new State(6);
        State s7 = new State(7);
        State s8 = new State(8);
        State s9 = new State(9);
        State s10 = new State(10);
        State s11 = new State(11);
        State s12 = new State(12);
        State s13 = new State(13);
        State s14 = new State(14);
        State s15 = new State(15);
        State s16 = new State(16);
        State s17 = new State(17);

        s0.addEpsilonTransition(s1);
        s1.addEpsilonTransition(s2);
        s2.addTransition('.', s2);
        s2.addEpsilonTransition(s3);
        s3.addTransition('A', s4);
        s4.addTransition('B', s5);
        s5.addEpsilonTransition(s6);
        s6.addEpsilonTransition(s7);
        s6.addEpsilonTransition(s11);

        s7.addTransition('C', s8);
        s8.addEpsilonTransition(s9);
        s9.addTransition('F', s10);
        s10.addEpsilonTransition(s15);

        s11.addTransition('D', s12);
        s12.addEpsilonTransition(s13);
        s13.addTransition('F', s14);
        s14.addEpsilonTransition(s15);

        s15.addEpsilonTransition(s6);
        s15.addTransition('G', s16);
        s16.addEpsilonTransition(s17);

        s17.isFinal = true;

        Automaton automaton = new Automaton(s0);
        automaton.addState(s0);
        automaton.addState(s1);
        automaton.addState(s2);
        automaton.addState(s3);
        automaton.addState(s4);
        automaton.addState(s5);
        automaton.addState(s6);
        automaton.addState(s7);
        automaton.addState(s8);
        automaton.addState(s9);
        automaton.addState(s10);
        automaton.addState(s11);
        automaton.addState(s12);
        automaton.addState(s13);
        automaton.addState(s14);
        automaton.addState(s15);
        automaton.addState(s16);
        automaton.addState(s17);

        return automaton;
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

        Automaton automaton = buildNKA();

        runTests(automaton);
    }
}