package com.example;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class EbayTraditionalApi {
    // Replace these with your actual eBay application credentials
    //For security reasons I couldent use my clients Credentials 
    private static final String DEV_ID = "example";
    private static final String APP_ID = "example";
    private static final String CERT_ID = "example";
    private static final String USER_TOKEN = "example";
    private static final String API_ENDPOINT = "https://api.ebay.com/ws/api.dll";

    public static void main(String[] args) {
        try {
            String requestXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<GetOrdersRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">"
                    + "<RequesterCredentials>"
                    + "<eBayAuthToken>" + USER_TOKEN + "</eBayAuthToken>"
                    + "</RequesterCredentials>"
                    + "<OrderRole>Seller</OrderRole>"
                    + "<OrderStatus>All</OrderStatus>"
                    + "<NumberOfDays>30</NumberOfDays>"  // Specify the number of days to retrieve orders
                    + "<Pagination>"
                    + "<EntriesPerPage>10</EntriesPerPage>"
                    + "<PageNumber>1</PageNumber>"
                    + "</Pagination>"
                    + "</GetOrdersRequest>";

            // Create a connection to the eBay API endpoint
            URL url = new URL(API_ENDPOINT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.setRequestProperty("X-EBAY-API-COMPATIBILITY-LEVEL", "967");
            connection.setRequestProperty("X-EBAY-API-DEV-NAME", DEV_ID);
            connection.setRequestProperty("X-EBAY-API-APP-NAME", APP_ID);
            connection.setRequestProperty("X-EBAY-API-CERT-NAME", CERT_ID);
            connection.setRequestProperty("X-EBAY-API-CALL-NAME", "GetOrders");
            connection.setRequestProperty("X-EBAY-API-SITEID", "0");

            // Send the request XML
            OutputStream os = connection.getOutputStream();
            os.write(requestXML.getBytes());
            os.flush();

            // Get the response
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // Print the response
            System.out.println("Response: " + response.toString());

            // Parse the XML response
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.ByteArrayInputStream(response.toString().getBytes()));
            
            NodeList orders = doc.getElementsByTagName("Order");
            FileWriter csvWriter = new FileWriter("orders.csv");

            csvWriter.append("OrderID, OrderStatus, TotalPrice\n");

            for (int i = 0; i < orders.getLength(); i++) {
                String orderID = orders.item(i).getChildNodes().item(1).getTextContent();
                String orderStatus = orders.item(i).getChildNodes().item(3).getTextContent();
                String totalPrice = orders.item(i).getChildNodes().item(5).getTextContent();
                
                // Write order details to CSV
                csvWriter.append(orderID).append(", ")
                         .append(orderStatus).append(", ")
                         .append(totalPrice).append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

            System.out.println("Order details have been written to orders.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
