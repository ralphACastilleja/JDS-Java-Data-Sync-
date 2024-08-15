package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _07_AvgP {
    
    //This method processes the CSV file and extracts prices for each brand
    public static Map<String, List<Double>> processCSVFile(String filePath, List<String> brands) {
        Map<String, List<Double>> brandPrices = new HashMap<>();
        Map<String, Integer> brandOccurrences = new HashMap<>();
        for (String brand : brands) {
            brandPrices.put(brand, new ArrayList<>());
            brandOccurrences.put(brand, 0);
        }
 
        boolean titleFound = false;
        boolean soldForFound = false;
        int itemTitleColumnIndex = -1;
        int soldForColumnIndex = -1;
 
        //First, find the "Item Title" and "Sold For" columns by checking the first four rows
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines().parse(reader);
 
            int rowCount = 0;
            for (CSVRecord record : records) {
                rowCount++;
                for (int i = 0; i < record.size(); i++) {
                    String cellValue = record.get(i).trim().toLowerCase();
                    if (cellValue.equals("item title") || cellValue.equals("title")) {
                        itemTitleColumnIndex = i;
                        titleFound = true;
                    }
                    if (cellValue.equals("sold for")) {
                        soldForColumnIndex = i;
                        soldForFound = true;
                    }
                }
                if (titleFound && soldForFound) break;
                if (rowCount >= 4) break; //Only check the first 4 rows
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
 
        if (titleFound && soldForFound) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines().parse(reader);
                boolean skipHeader = true;
                for (CSVRecord record : records) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue;
                    }
                    if (record.size() > itemTitleColumnIndex && record.size() > soldForColumnIndex) {
                        String itemTitle = record.get(itemTitleColumnIndex).trim();
                        String soldForString = record.get(soldForColumnIndex).trim();
                        if (!itemTitle.isEmpty() && !soldForString.isEmpty()) {
                            try {
                                double price = Double.parseDouble(soldForString.replace("$", "").replace(",", ""));
                                for (String brand : brands) {
                                    if (itemTitle.toLowerCase().contains(brand.toLowerCase())) {
                                        brandPrices.get(brand).add(price);
                                        brandOccurrences.put(brand, brandOccurrences.get(brand) + 1);
                                        break;
                                    }
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("Skipping invalid price: " + soldForString);
                            }
                        } else {
                            System.out.println("Skipping empty title or price in record: " + record);
                        }
                    } else {
                        System.out.println("Record size mismatch: " + record.size() + ", expected at least " + Math.max(itemTitleColumnIndex, soldForColumnIndex));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.err.println("Item Title or Sold For column not found.");
            return null;
        }
 
        return brandPrices;
    }
 
    //This method calculates the number of occurrences for each brand in the CSV file
    public static Map<String, Integer> calculateBrandOccurrences(Map<String, List<Double>> brandPrices) {
        Map<String, Integer> brandOccurrences = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
            brandOccurrences.put(entry.getKey(), entry.getValue().size());
        }
        return brandOccurrences;
    }
    
    //This method calculates the average prices for each brand
    public static Map<String, Double> calculateAveragePrices(Map<String, List<Double>> brandPrices) {
        Map<String, Double> averagePrices = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
            List<Double> prices = entry.getValue();
            if (!prices.isEmpty()) {
                double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                averagePrices.put(entry.getKey(), averagePrice);
            }
        }
        return averagePrices;
    }
}
