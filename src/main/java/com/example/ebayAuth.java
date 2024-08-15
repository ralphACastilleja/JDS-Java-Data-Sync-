package com.example;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;

public class ebayAuth {
    private String clientId = "placeholder";
    private String clientSecret = "palceholder";

    public String getAccessToken() throws Exception {
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        String scope = "https://api.ebay.com/oauth/api_scope";

        HttpResponse<String> response = Unirest.post("https://api.ebay.com/identity/v1/oauth2/token")
            .header("Authorization", "Basic " + encodedCredentials)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body("grant_type=client_credentials&scope=" + scope)
            .asString();

        if (response.getStatus() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("Failed to obtain access token. Response: " + response.getBody());
        }
    }

    public static void main(String[] args) {
        try {
            ebayAuth auth = new ebayAuth();
            String accessToken = auth.getAccessToken();
            System.out.println("Access Token: " + accessToken);

            auth.getAccessTokenWithAdditionalScope(accessToken, "https://api.ebay.com/oauth/api_scope/sell.inventory");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to obtain access token with an additional scope
    public void getAccessTokenWithAdditionalScope(String baseAccessToken, String additionalScope) {
        try {
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            String scope = "https://api.ebay.com/oauth/api_scope " + additionalScope;

            HttpResponse<String> response = Unirest.post("https://api.ebay.com/identity/v1/oauth2/token")
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=client_credentials&scope=" + scope)
                .asString();

            if (response.getStatus() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.getBody());
                String newAccessToken = jsonNode.get("access_token").asText();
                System.out.println("New Access Token with additional scope: " + newAccessToken);
            } else {
                System.out.println("Failed to obtain access token with additional scope. Response: " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
