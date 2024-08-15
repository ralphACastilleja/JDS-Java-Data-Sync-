package com.example;

public class _42_ShopifyCompiler {

    public static void main(String[] args) {
        // Example dates for testing, adjust as needed
        String createdAfter = "2024-01-01T00:00:00Z";
        String createdBefore = "2024-07-31T23:59:59Z";

        // Create an instance of the compiler and run the compilation process
        _42_ShopifyCompiler compiler = new _42_ShopifyCompiler();
        compiler.compileShopifyData(createdAfter, createdBefore);
    }

    public void compileShopifyData(String createdAfter, String createdBefore) {
        System.out.println("Starting Shopify data compilation process...");

        // Step 1: Fetch product listings and save to "ShopifyListings.txt"
        _37_LISTSB listSB = new _37_LISTSB();
        listSB.fetchAndSaveProductListings();

        // Step 2: Fetch orders and save to "ShopifyOrders.txt"
        _35_SBAPI sbAPI = new _35_SBAPI();
        sbAPI.fetchAndSaveShopifyOrders(createdAfter, createdBefore);

        // Step 3: Compare listings with CSV data and update listings with dates, save to "CompleteShopify.txt"
        _38_CREATEDATSBL dateSBL = new _38_CREATEDATSBL();
        dateSBL.processShopifyListings();

        // Step 4: Transfer listings with a created date to "CompleteShopify.txt"
        _39_Transfer transfer = new _39_Transfer();
        transfer.transferListings();

        // Step 5: Compare complete listings with orders and calculate turnover time, save to "ShopifyParsedTurnover.txt"
        _40_ListOrderCompare orderCompare = new _40_ListOrderCompare();
        orderCompare.compareListingsAndOrders();

        // Step 6: Inject turnover data into general and category-specific turnover files
        _41_ParsedIntoTurnover parsedTurnover = new _41_ParsedIntoTurnover();
        parsedTurnover.processParsedTurnover();

        System.out.println("Shopify data compilation process completed.");
    }
}
