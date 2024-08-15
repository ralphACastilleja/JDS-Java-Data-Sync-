package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Base64;



 class _37_LISTSB {

    private static final String API_KEY = "placeholder"; // Account Token
    private static final String API_SECRET = "placeholder"; // Secret Key
    private static final String API_BASE_URL = "https://api.sellbrite.com/v1/";

    public void fetchAndSaveProductListings() {
        ensureAppDataFolderExists();
        Set<String> existingListingIds = loadExistingListingIds(); // Load existing listing IDs to avoid duplicates

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getAppDataFilePath("ShopifyListings.txt"), true)))) {
            int page = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                String rawListingsData = fetchProductListings(page);

                JSONArray listingsData = new JSONArray(rawListingsData);
                for (int i = 0; i < listingsData.length(); i++) {
                    JSONObject listing = listingsData.getJSONObject(i);

                    String sourceProvider = listing.optString("source_provider", "N/A");
                    String id = listing.optString("id", "N/A");

                    if (!"ebay".equals(sourceProvider) && !existingListingIds.contains(id)) {
                        existingListingIds.add(id); // Add the ID to the set to prevent future duplicates
                        String name = listing.optString("name", "N/A");
                        String price = listing.optString("price", "N/A");
                        String quantity = listing.optString("quantity", "N/A");
                        String sku = listing.optString("sku", "N/A");
                        String modifiedAt = listing.optString("modified_at", "N/A");
                        String createdAt = listing.optString("created_at", "N/A");

                        // Debugging line to print all keys and values
                     //   System.out.println("All keys and values in the listing:");
                        for (String key : listing.keySet()) {
                            System.out.println(key + ": " + listing.optString(key, "N/A"));
                        }

                        writer.printf("source_provider: %s, name: %s, price: %s, quantity: %s, created_at: %s, modified_at: %s, sku: %s | | id: %s%n",
                                sourceProvider, name, price, quantity, createdAt, modifiedAt, sku, id);
            System.out.printf("source_provider: %s, name: %s, price: %s, quantity: %s, created_at: %s, modified_at: %s, sku: %s | | id: %s%n",
                                sourceProvider, name, price, quantity, createdAt, modifiedAt, sku, id);
                    }
                }

                if (listingsData.length() < 100) {
                    hasMoreData = false; // No more pages if the current page has less than 100 listings
                } else {
                    page++; // Move to the next page
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchProductListings(int page) throws Exception {
        String endpoint = "products?page=" + page;
        URL url = new URL(API_BASE_URL + endpoint);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((API_KEY + ":" + API_SECRET).getBytes()));

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed to fetch data: " + responseCode);
        }
    }

    private static Set<String> loadExistingListingIds() {
        Set<String> listingIds = new HashSet<>();
        String filePath = getAppDataFilePath("ShopifyListings.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("id: ")) {
                    String id = line.substring(line.indexOf("id: ") + 4).trim();
                    listingIds.add(id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listingIds;
    }

    private static String getAppDataFilePath(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }

    private static void ensureAppDataFolderExists() {
        String appDataFolderPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        File appDataFolder = new File(appDataFolderPath);
        if (!appDataFolder.exists()) {
            appDataFolder.mkdirs();
        }
    }
}