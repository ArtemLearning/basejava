package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {

    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));

        List<Integer> list = oddOrEven(List.of(new Integer[]{1, 4, 5, 6, 7}));
        for (Integer i : list) {
            System.out.println(i.toString());
        }

    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce((a, b) -> a * 10 + b)
                .orElseThrow();
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream().reduce(0, Integer::sum);
        return integers.stream()
                .filter(x -> (x % 2) == (sum % 2))
                .distinct()
                .collect(Collectors.toList());
    }
}
