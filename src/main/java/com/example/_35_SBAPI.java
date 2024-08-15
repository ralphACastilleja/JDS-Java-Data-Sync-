package com.example;


import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.io.File;
import java.io.InputStreamReader;





 public class _35_SBAPI {

    private static final String API_KEY = "place holder"; //Account Token
    private static final String API_SECRET = "place holder"; //Secret Key
    private static final String API_BASE_URL = "https://api.sellbrite.com/v1/";
    private final String shopifyOrdersFilePath = getAppDataFilePath("ShopifyOrders.txt");

     //retrieves Shopify orders between the given dates and saves them to a file.
    public void fetchAndSaveShopifyOrders(String createdAfter, String createdBefore) {
        try {
            int page = 1;
            boolean hasMoreData = true;

            ensureAppDataFolderExists();
            Set<String> existingOrderRefs = loadExistingOrderRefs();

            StringBuilder dataBuilder = new StringBuilder(); //Accumulate data in a StringBuilder

            while (hasMoreData) {
                String rawSalesData = fetchRawSalesData(page, createdAfter, createdBefore);
                JSONArray salesData = new JSONArray(rawSalesData);

                for (int i = 0; i < salesData.length(); i++) {
                    JSONObject order = salesData.getJSONObject(i);
                    if ("Shopify".equals(order.optString("channel_name"))) {
                        String orderRef = order.optString("order_ref", "N/A");
                        if (!existingOrderRefs.contains(orderRef)) {
                            existingOrderRefs.add(orderRef);
                            double subtotal = order.optDouble("subtotal", 0.0);
                            String channelName = order.optString("channel_name", "N/A");
                            String orderedAt = order.optString("ordered_at", "N/A");

                            JSONArray items = order.getJSONArray("items");
                            for (int j = 0; j < items.length(); j++) {
                                JSONObject item = items.getJSONObject(j);
                                String orderItemRef = item.optString("order_item_ref", "N/A");
                                double unitPrice = item.optDouble("unit_price", 0.0);
                                double total = item.optDouble("total", 0.0);
                                String title = item.optString("title", "No title");
                                int quantity = item.optInt("quantity", 0);
                                String sku = item.optString("sku", "N/A");
                                String inventorySku = item.optString("inventory_sku", "N/A");

                                dataBuilder.append(String.format("channelName: %s, title: %s, unitPrice: $%.2f, quantity: %d, ordered_at: %s, sku: %s | | orderRef: %s, subtotal: $%.2f, orderItemRef: %s, total: $%.2f, inventorySku: %s%n",
                                    channelName, title, unitPrice, quantity, orderedAt, sku, orderRef, subtotal, orderItemRef, total, inventorySku)); //Accumulate data
                            }
                        }
                    }
                }

                if (salesData.length() < 100) {
                    hasMoreData = false;
                } else {
                    page++;
                }
            }

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getShopifyOrdersFilePath(), true)))) { //Write accumulated data to file
                writer.write(dataBuilder.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // makes an API call to fetch raw sales data from Sellbrite.
    private String fetchRawSalesData(int page, String createdAfter, String createdBefore) throws Exception {
        String endpoint = "orders?page=" + page + "&created_after=" + createdAfter + "&created_before=" + createdBefore;
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

  //   loads existing order references from the Shopify orders file to avoid duplicates.
    private Set<String> loadExistingOrderRefs() throws IOException {
        Set<String> orderRefs = new HashSet<>();
        String filePath = getShopifyOrdersFilePath();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int startIdx = line.indexOf("orderRef: ") + 10;
                int endIdx = line.indexOf(",", startIdx);
                if (startIdx > 0 && endIdx > startIdx) {
                    String ref = line.substring(startIdx, endIdx).trim();
                    orderRefs.add(ref);
                }
            }
        }
        return orderRefs;
    }

    //checks if the application data folder exists, and creates it if not.
    private void ensureAppDataFolderExists() {
        String appDataFolderPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        File appDataFolder = new File(appDataFolderPath);
        if (!appDataFolder.exists()) {
            appDataFolder.mkdirs();
        }
    }

   //  returns the full path to a file in the application data folder.
    private String getAppDataFilePath(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }

   //  returns the full path to the Shopify orders file.
    private String getShopifyOrdersFilePath() {
        return shopifyOrdersFilePath;
    }
}
