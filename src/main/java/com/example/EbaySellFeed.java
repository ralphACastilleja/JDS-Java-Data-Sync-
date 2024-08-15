package com.example;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EbaySellFeed {
    private String accessToken;

    // Constructor to set the access token
    public EbaySellFeed(String accessToken) {
        this.accessToken = accessToken;
    }

    public void createOrderReportTask() throws Exception {
        HttpResponse<String> response = Unirest.post("https://api.ebay.com/sell/feed/v1/order_task")
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json")
            .body("{ " +
                    "\"schemaVersion\": \"1169\", " +
                    "\"feedType\": \"LMS_ORDER_REPORT\", " +
                    "\"filterCriteria\": { " +
                        "\"creationDateRange\": { " +
                            "\"from\": \"2020-10-19T19:09:02.768Z\", " +
                            "\"to\": \"2020-10-26T19:09:02.768Z\" " +
                        "}, " +
                        "\"orderStatus\": \"ACTIVE\" " +
                    "} " +
                "}")
            .asString();

        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Response Body: " + response.getBody());

        // Check if the response status is 200 (OK)
        if (response.getStatus() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            System.out.println("Task ID: " + jsonNode.get("taskId").asText());
            System.out.println("Task Status: " + jsonNode.get("taskStatus").asText());
        } else {
            throw new RuntimeException("Failed to create sell feed task. Response: " + response.getBody());
        }
    }

    // Method to create a task for the Sell Feed API (inventory report)
    public void createInventoryReportTask() throws Exception {
        HttpResponse<String> response = Unirest.post("https://api.ebay.com/sell/feed/v1/inventory_task")
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json")
            .body("{ " +
                    "\"schemaVersion\": \"1169\", " +
                    "\"feedType\": \"LMS_ACTIVE_INVENTORY_REPORT\", " +
                    "\"filterCriteria\": { " +
                        "\"inventoryType\": \"ACTIVE\" " +
                    "} " +
                "}")
            .asString();

        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Response Body: " + response.getBody());

        if (response.getStatus() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            System.out.println("Task ID: " + jsonNode.get("taskId").asText());
            System.out.println("Task Status: " + jsonNode.get("taskStatus").asText());
        } else {
            throw new RuntimeException("Failed to create sell feed task. Response: " + response.getBody());
        }
    }

    public static void main(String[] args) {
        try {
            // Example access token
            String accessToken = "asdasd";

            // Create the Sell Feed task
            EbaySellFeed sellFeed = new EbaySellFeed(accessToken);
            
            sellFeed.createInventoryReportTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
