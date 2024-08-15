package com.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import java.time.Instant;
import java.time.ZoneId;

public class _40_ListOrderCompare {

    //Compares Shopify listings and orders, then calculates turnover time
    public void compareListingsAndOrders() {
        String completeShopifyFilePath = getAppDataFilePath("CompleteShopify.txt");
        String shopifyOrdersFilePath = getAppDataFilePath("ShopifyOrders.txt");

        try {
            List<Listing> completeShopifyListings = readListings(completeShopifyFilePath);
            List<Order> shopifyOrders = readOrders(shopifyOrdersFilePath);

            List<ListingOrderMatch> matchResults = findMatches(completeShopifyListings, shopifyOrders);
            printMatchesAndTurnoverTime(matchResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Reads listings from a file
    private List<Listing> readListings(String filePath) throws IOException {
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

    //Parses a line of text into a Listing object
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

    //Reads orders from a file
    private List<Order> readOrders(String filePath) throws IOException {
        List<Order> orders = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            Order order = parseOrder(line);
            if (order != null) {
                orders.add(order);
            }
        }
        return orders;
    }

    //Parses a line of text into an Order object
    private Order parseOrder(String line) {
        try {
            String[] parts = line.split(", (?=\\w+: )| \\| \\| ");
            if (parts.length < 11) {
                System.err.println("Skipping invalid line (incorrect number of parts): " + line);
                return null;
            }

            Order order = new Order();
            order.channelName = extractValue(parts[0]);
            order.title = extractValue(parts[1]);
            order.unitPrice = extractValue(parts[2]);
            order.quantity = extractValue(parts[3]);
            order.orderedAt = extractValue(parts[4]);
            order.sku = extractValue(parts[5]);
            order.orderRef = extractValue(parts[6]);
            order.subtotal = extractValue(parts[7]);
            order.orderItemRef = extractValue(parts[8]);
            order.total = extractValue(parts[9]);
            order.inventorySku = extractValue(parts[10]);

            return order;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
            return null;
        }
    }

    //Extracts the value from a key-value pair
    private String extractValue(String part) {
        String[] keyValue = part.split(": ", 2);
        if (keyValue.length < 2) {
            System.err.println("Invalid key-value pair: " + part);
            return "";
        }
        return keyValue[1].trim();
    }

    //Finds matching listings and orders based on title
    private List<ListingOrderMatch> findMatches(List<Listing> listings, List<Order> orders) {
        Map<String, Order> orderMap = new HashMap<>();
        for (Order order : orders) {
            orderMap.put(order.title.toLowerCase(), order);  // Store orders in a map for quick lookup
        }

        List<ListingOrderMatch> matchResults = new ArrayList<>();
        for (Listing listing : listings) {
            Order order = orderMap.get(listing.name.toLowerCase());
            if (order != null) {
                matchResults.add(new ListingOrderMatch(listing, order));
            }
        }
        return matchResults;
    }

    //Prints matches and calculates turnover time
    private void printMatchesAndTurnoverTime(List<ListingOrderMatch> matchResults) throws IOException {
        DateTimeFormatter listingFormatter = DateTimeFormatter.ofPattern("MMM-dd-yy HH:mm:ss z", Locale.ENGLISH);
        DateTimeFormatter orderFormatter = DateTimeFormatter.ofPattern("MMM-dd-yy", Locale.ENGLISH);

        String filePath = getAppDataFilePath("ShopifyParsedTurnover.txt");
        Path parsedTurnoverPath = Paths.get(filePath);
        if (!Files.exists(parsedTurnoverPath)) {
            Files.createFile(parsedTurnoverPath);
        }

        // Read existing data to avoid duplicates
        Set<String> existingData = new HashSet<>(Files.readAllLines(parsedTurnoverPath));

        try (BufferedWriter writer = Files.newBufferedWriter(parsedTurnoverPath, StandardOpenOption.APPEND)) {
            for (ListingOrderMatch matchResult : matchResults) {
                ZonedDateTime listingDate = ZonedDateTime.parse(matchResult.listing.date, listingFormatter);
                Instant orderInstant = Instant.parse(matchResult.order.orderedAt);
                ZonedDateTime orderDate = orderInstant.atZone(ZoneId.of("UTC"));

                Duration turnoverTime = Duration.between(listingDate, orderDate);

                String category = getCategoryFromTitle(matchResult.listing.name);
                String brand = getBrandFromTitle(matchResult.listing.name);

                String entry = String.format("%s,%s,%s,%s,%s,%s,%d days",
                    matchResult.listing.id,
                    category,
                    brand,
                    "Unknown",
                    listingDate.format(listingFormatter),
                    orderDate.format(orderFormatter),
                    turnoverTime.toDays());

                if (existingData.add(entry)) {  // add returns false if entry already exists
                    writer.write(entry);
                    writer.newLine();
                }
            }
        }
    }

    //Creates the file if it does not already exist
    private void createFileIfNotExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    //Extracts the category from the listing title
    private String getCategoryFromTitle(String title) {
        List<String> categories = _09_BrandList.getCategories(); 
        for (String category : categories) {
            if (title.toLowerCase().contains(category.toLowerCase())) {
                return category;
            }
        }
        return "Unknown";
    }

    //Extracts the brand from the listing title
    private String getBrandFromTitle(String title) {
        List<String> brands = _09_BrandList.getBrands(); 
        for (String brand : brands) {
            if (title.toLowerCase().contains(brand.toLowerCase())) {
                return brand;
            }
        }
        return "Unknown";
    }

    //Returns the full path to a file in the application data folder
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

    static class Order {
        String channelName;
        String title;
        String unitPrice;
        String quantity;
        String orderedAt;
        String sku;
        String orderRef;
        String subtotal;
        String orderItemRef;
        String total;
        String inventorySku;
    }

    static class ListingOrderMatch {
        Listing listing;
        Order order;

        ListingOrderMatch(Listing listing, Order order) {
            this.listing = listing;
            this.order = order;
        }
    }
}
