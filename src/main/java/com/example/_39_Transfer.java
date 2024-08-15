package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class _39_Transfer {

    // Transfers Listing data that has a created_at date into the complete file.
    public void transferListings() {
        String shopifyListingsFilePath = getAppDataFilePath("ShopifyListings.txt");
        String completeShopifyFilePath = getAppDataFilePath("CompleteShopify.txt");
    
        try {
            List<Listing> shopifyListings = readShopifyListings(shopifyListingsFilePath);
            createFileIfNotExists(completeShopifyFilePath);
            transferListingsToCompleteFile(shopifyListings, completeShopifyFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Reads Shopify listings from a file.
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

    // Parses a line of text into a Listing object.
    private Listing parseListing(String line) {
        try {
            String[] parts = line.split(", (?=\\w+: )| \\| \\| ");

            if (parts.length < 9) {
                System.err.println("Skipping invalid line (incorrect number of parts): " + line);
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
            listing.date = extractValue(parts[8]);

            return listing;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
            return null;
        }
    }

    // Extracts the value from a key-value pair.
    private String extractValue(String part) {
        String[] keyValue = part.split(": ", 2);
        if (keyValue.length < 2) {
            System.err.println("Invalid key-value pair: " + part);
            return "";
        }
        return keyValue[1];
    }

    // Transfers listings to the complete file, ensuring no duplicates
    private void transferListingsToCompleteFile(List<Listing> listings, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Set<String> transferredIds = new HashSet<>();
        
        if (Files.exists(path)) {
            List<String> existingLines = Files.readAllLines(path);
            for (String line : existingLines) {
                Listing existingListing = parseListing(line);
                if (existingListing != null) {
                    transferredIds.add(existingListing.id);
                }
            }
        }
        
        StringBuilder builder = new StringBuilder(); // Use StringBuilder for batch processing
        for (Listing listing : listings) {
            if (listing.date != null && !listing.date.trim().isEmpty() && !"null".equalsIgnoreCase(listing.date)) {
                if (!transferredIds.contains(listing.id)) {
                    builder.append(formatListing(listing)).append(System.lineSeparator());
                    transferredIds.add(listing.id); // Ensure no duplicates
                }
            }
        }
    
        Files.write(path, builder.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    
    // Creates the file if it does not already exist
    private void createFileIfNotExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    // Formats a Listing object into a string for writing to a file.
    private String formatListing(Listing listing) {
        return String.format("source_provider: %s, name: %s, price: %s, quantity: %s, created_at: %s, modified_at: %s, sku: %s | | id: %s, date: %s",
                listing.sourceProvider, listing.name, listing.price, listing.quantity, listing.createdAt, listing.modifiedAt, listing.sku, listing.id, listing.date);
    }

    // Returns the full path to a file in the application data folder
    private String getAppDataFilePath(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }

    static class Listing {
        String sourceProvider;
        String name;
        String price;
        String quantity;
        String createdAt;
        String modifiedAt;
        String sku;
        String id;
        String date; 
    }
}
