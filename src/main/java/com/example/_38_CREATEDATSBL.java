package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class _38_CREATEDATSBL {

    // Processes the Shopify listings, compares them with the eBay listings dates,
    // and updates the Shopify data to include dates if a match is found.
    public void processShopifyListings() {
        String shopifyListingsFilePath = getAppDataFilePath("ShopifyListings.txt");
        String secondTxtFilePath = getAppDataFilePath("2ndCsvFile.txt");
        String completeShopifyFilePath = getAppDataFilePath("CompleteShopify.txt");

        try {
            List<Listing> shopifyListings = readShopifyListings(shopifyListingsFilePath);
            List<CsvItem> csvItems = readCsvItems(secondTxtFilePath);

            Set<String> existingIds = loadExistingIds(completeShopifyFilePath); // Load existing IDs
            int changedCount = 0;
            List<MatchResult> matchResults = new ArrayList<>();
            Set<String> matchedNames = new HashSet<>();

            for (Listing listing : shopifyListings) {
                boolean matchFound = false;
                for (CsvItem csvItem : csvItems) {
                    double similarity = calculateSimilarity(listing.name.toLowerCase(), csvItem.name.toLowerCase().replaceAll("\\s+", " "));
                    if (similarity >= 0.9) {
                        listing.date = csvItem.date;
                        matchFound = true;
                        changedCount++;
                        matchResults.add(new MatchResult(listing, csvItem));
                        matchedNames.add(listing.name);
                        break;
                    }
                }
            }

            writeUpdatedListings(shopifyListings, shopifyListingsFilePath);
            writeCompleteShopify(matchResults, completeShopifyFilePath, matchedNames, existingIds);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Calculates the similarity between two strings using the Jaccard similarity measure
    private double calculateSimilarity(String str1, String str2) {
        Set<String> a = new HashSet<>(Arrays.asList(str1.split("\\s+")));
        Set<String> b = new HashSet<>(Arrays.asList(str2.split("\\s+")));
        
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        
        return (double) intersection.size() / union.size();
    }

    // Calculates the edit distance (Levenshtein distance) between two strings
    private int editDistance(String str1, String str2) {
        int[] costs = new int[str2.length() + 1];
        for (int i = 0; i <= str1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (str1.charAt(i - 1) != str2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[str2.length()] = lastValue;
        }
        return costs[str2.length()];
    }

    // Reads Shopify listings from a file
    private List<Listing> readShopifyListings(String filePath) throws IOException {
        List<Listing> listings = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            Listing listing = parseListing(line);
            if (listing != null) {
                listings.add(listing);
            }
        }
        return listings;
    }

    // Parses a line of text into a Listing object
    private Listing parseListing(String line) {
        try {
            String[] parts = line.split(", (?=\\w+: )| \\| \\| ");
            if (parts.length < 8) {
                return null;
            }

            Listing listing = new Listing();
            listing.sourceProvider = extractValue(parts[0]);
            listing.name = extractValue(parts[1]);
            listing.price = extractValue(parts[2]);
            listing.quantity = extractValue(parts[3]);
            listing.createdAt = extractValue(parts[4]);
            listing.modifiedAt = extractValue(parts[5]);
            listing.sku = extractValue(parts[6]);
            listing.id = extractValue(parts[7]);
            listing.date = null; // Initial date is null
            return listing;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
            return null;
        }
    }

    // Extracts the value from a key-value pair
    private String extractValue(String part) {
        String[] keyValue = part.split(": ", 2);
        if (keyValue.length < 2) {
            return "";
        }
        return keyValue[1];
    }

    // Reads CSV items from a file
    private List<CsvItem> readCsvItems(String filePath) throws IOException {
        List<CsvItem> items = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {
            CsvItem item = parseCsvItem(line);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    // Parses a line of CSV data into a CsvItem object
    private CsvItem parseCsvItem(String line) {
        String[] parts = line.split(",");

        if (parts.length < 6) {
            System.err.println("Skipping invalid CSV line: " + line);
            return null;
        }

        CsvItem item = new CsvItem();
        item.name = parts[5].trim();
        item.date = parts[3].trim();
        return item;
    }

    // Writes the updated Shopify listings back to a file
    private void writeUpdatedListings(List<Listing> listings, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Listing listing : listings) {
                writer.write(formatListing(listing));
                writer.newLine();
            }
        }
    }

    // Writes the complete Shopify data, including matches, to a file
    private void writeCompleteShopify(List<MatchResult> matchResults, String filePath, Set<String> matchedNames, Set<String> existingIds) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (MatchResult matchResult : matchResults) {
                if (!matchedNames.contains(matchResult.listing.name) && !existingIds.contains(matchResult.listing.id)) {
                    writer.write(formatListing(matchResult.listing));
                    writer.newLine();
                    matchedNames.add(matchResult.listing.name);
                    existingIds.add(matchResult.listing.id);
                }
            }
        }
    }

    // Formats a Listing object into a string for writing to a file
    private static String formatListing(Listing listing) {
        return String.format("source_provider: %s, name: %s, price: %s, quantity: %s, created_at: %s, modified_at: %s, sku: %s | | id: %s, date: %s",
                listing.sourceProvider, listing.name, listing.price, listing.quantity, listing.createdAt, listing.modifiedAt, listing.sku, listing.id, listing.date);
    }

    // Returns the full path to a file in the application data folder
    private static String getAppDataFilePath(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }

    // Loads existing IDs from a file
    private static Set<String> loadExistingIds(String filePath) throws IOException {
        Set<String> ids = new HashSet<>();
        if (Files.exists(Paths.get(filePath))) {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.contains("id: ")) {
                    String id = line.substring(line.indexOf("id: ") + 4, line.indexOf(", date:")).trim();
                    ids.add(id);
                }
            }
        }
        return ids;
    }

    // Inner classes to represent listings and CSV items
    static class Listing {
        String sourceProvider;
        String name;
        String price;
        String quantity;
        String createdAt;
        String modifiedAt;
        String sku;
        String id;
        String date; // This will be updated from the 2ndCsvFile
    }

    static class CsvItem {
        String name;
        String date;
    }

    static class MatchResult {
        Listing listing;
        CsvItem csvItem;

        MatchResult(Listing listing, CsvItem csvItem) {
            this.listing = listing;
            this.csvItem = csvItem;
        }
    }
}
