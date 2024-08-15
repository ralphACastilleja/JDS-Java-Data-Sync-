package com.example;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class _31_StatsCalc {

    // Method to calculate and display all statistics// Method to calculate and display all statistics
public static void calculateAndDisplayStatistics(List<Double> values, JFrame parentFrame) {
    if (values.isEmpty()) {
        JOptionPane.showMessageDialog(parentFrame, "No data available to calculate statistics.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    double median = calculateMedian(values);

    List<Double> mode = calculateMode(values);

    double standardDeviation = calculateStandardDeviation(values);

    StringBuilder result = new StringBuilder();
    result.append("Median: ").append(median).append("\n");
    result.append("Explanation: The median is the middle value when the data is sorted in ascending order. If the number of values is even, it is the average of the two middle values.\n\n");
    
    result.append("Mode: ").append(mode).append("\n");
    result.append("Explanation: The mode is the value(s) that appear most frequently in the data set. There can be more than one mode.\n\n");
    
    result.append("Standard Deviation: ").append(standardDeviation).append("\n");
    result.append("Explanation: The standard deviation measures the amount of variation or dispersion of the data values. A low standard deviation indicates that the values tend to be close to the mean, while a high standard deviation indicates that the values are spread out over a wider range.\n");

    JOptionPane.showMessageDialog(parentFrame, result.toString(), "Statistics", JOptionPane.INFORMATION_MESSAGE);
}

// Helper method to calculate the median
private static double calculateMedian(List<Double> values) {
    Collections.sort(values);
    int size = values.size();
    if (size % 2 == 0) {
        return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
    } else {
        return values.get(size / 2);
    }
}

// calculate the mode
private static List<Double> calculateMode(List<Double> values) {
    Map<Double, Integer> frequencyMap = new HashMap<>();
    for (double value : values) {
        frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
    }

    int maxFrequency = Collections.max(frequencyMap.values());
    return frequencyMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == maxFrequency)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
}

//  standard deviation
private static double calculateStandardDeviation(List<Double> values) {
    double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    double variance = values.stream().mapToDouble(value -> Math.pow(value - mean, 2)).sum() / values.size();
    return Math.sqrt(variance);
}
}