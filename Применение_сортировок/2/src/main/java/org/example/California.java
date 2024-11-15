package org.example;

import java.util.*;
import java.util.logging.Logger;

public class California {

    private static final Logger logger = Logger.getLogger(California.class.getName());

    private static final String NATURAL_ORDER = "RWQOJMVAHBSGZXNTCIEKUPDYFL";

    public static class CaliforniaOrderComparator implements Comparator<String> {
        private final Map<Character, Integer> charOrderMap;

        public CaliforniaOrderComparator() {
            charOrderMap = new HashMap<>();
            for (int i = 0; i < NATURAL_ORDER.length(); i++) {
                charOrderMap.put(NATURAL_ORDER.charAt(i), i);
            }
        }

        @Override
        public int compare(String s1, String s2) {
            int length1 = s1.length();
            int length2 = s2.length();
            int minLength = Math.min(length1, length2);

            for (int i = 0; i < minLength; i++) {
                char c1 = Character.toUpperCase(s1.charAt(i));
                char c2 = Character.toUpperCase(s2.charAt(i));
                int pos1 = charOrderMap.getOrDefault(c1, Integer.MAX_VALUE);
                int pos2 = charOrderMap.getOrDefault(c2, Integer.MAX_VALUE);

                if (pos1 != pos2) {
                    return Integer.compare(pos1, pos2);
                }
            }
            return Integer.compare(length1, length2);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> strings = new ArrayList<>();
        CaliforniaOrderComparator comparator = new CaliforniaOrderComparator();

        logger.info("Добро пожаловать в программу сортировки California!");
        logger.info("Введите строки. Для завершения введите 'exit':");

        while (true) {
            logger.info("Введите строку: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            strings.add(input);
        }

        strings.sort(comparator);

        logger.info("\nСтроки в естественном порядке:");
        for (String str : strings) {
            logger.info(str);
        }
    }
}
