package org.data.stats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        findStatistics();
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public static void findStatistics() {
        String filePath = "10m.txt";

        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            long[] numbers = lines.mapToLong(Long::parseLong).toArray();

            if (numbers.length == 0) {
                System.out.println("The file is empty.");
                return;
            }

            long max = numbers[0];
            long min = numbers[0];
            double average;
            long sum = 0;
            long count = 0;

            List<Long> ascendingSequence = new ArrayList<>();
            List<Long> maxAscendingSequence = new ArrayList<>();
            List<Long> descendingSequence = new ArrayList<>();
            List<Long> maxDescendingSequence = new ArrayList<>();

            for (int i = 0; i < numbers.length; i++) {
                long number = numbers[i];

                if (number > max) {
                    max = number;
                }

                if (number < min) {
                    min = number;
                }

                sum += number;
                count++;

                if (i > 0) {
                    if (number >= numbers[i - 1]) {
                        ascendingSequence.add(number);
                    } else {
                        if (ascendingSequence.size() > maxAscendingSequence.size()) {
                            maxAscendingSequence = new ArrayList<>(ascendingSequence);
                        }
                        ascendingSequence.clear();
                        ascendingSequence.add(number);
                    }

                    if (number <= numbers[i - 1]) {
                        descendingSequence.add(number);
                    } else {
                        if (descendingSequence.size() > maxDescendingSequence.size()) {
                            maxDescendingSequence = new ArrayList<>(descendingSequence);
                        }
                        descendingSequence.clear();
                        descendingSequence.add(number);
                    }
                }
            }

            if (ascendingSequence.size() > maxAscendingSequence.size()) {
                maxAscendingSequence = new ArrayList<>(ascendingSequence);
            }

            if (descendingSequence.size() > maxDescendingSequence.size()) {
                maxDescendingSequence = new ArrayList<>(descendingSequence);
            }

            average = (double) sum / count;

            System.out.println("Max: " + max);
            System.out.println("Min: " + min);
            System.out.println("Average: " + average);
            System.out.println("Loading median...");
            double median = calculateMedian(numbers);
            System.out.println("Median: " + median);
            System.out.println("Longest Ascending Sequence: " + maxAscendingSequence);
            System.out.println("Longest Descending Sequence: " + maxDescendingSequence);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double calculateMedian(long[] numbers) {
        Arrays.sort(numbers);
        int length = numbers.length;

        if (length % 2 == 0) {
            return (double) (numbers[length / 2] + numbers[length / 2 - 1]) / 2;
        } else {
            return numbers[length / 2];
        }
    }
}