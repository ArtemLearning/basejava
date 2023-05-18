package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {

    public MainStream() {
    }

    public static void main(String[] args) {
        MainStream mainStream = new MainStream();
        System.out.println(mainStream.minValue(new int[]{1, 2, 3, 3, 2, 3}));

        List<Integer> list = mainStream.oddOrEven(List.of(new Integer[]{1, 4, 5, 6, 7}));
        for (Integer i : list) {
            System.out.println(i.toString());
        }

    }

    public int minValue(int[] values) {
        final int[] result = new int[1]; // New int array of 1 element
        Arrays.stream(values)
                .boxed()                 // Convert to boxed Integer
                .distinct()              // Get distinct numbers
                .sorted()                // Sort distinct number ascended
                .mapToInt(Integer::intValue).forEach(i -> result[0] = i + result[0] * 10);  // get number and put in into result array
        return result[0];
    }

    public List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream()
                .filter(x -> (x % 2) == (integers.stream().reduce(0, Integer::sum) % 2)) // Get sum of elements and check if it odd or even, and filter stream elements
                .distinct()                                                              // Get distinct numbers
                .collect(Collectors.toList());                                           // Convert to list
    }
}
