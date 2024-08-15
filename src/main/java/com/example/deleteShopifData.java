package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class deleteShopifData {

    public static void main(String[] args) {
        try {
            String[] filenames = {
                "ShopifyListings.txt",
                "ShopifyOrders.txt",
                "ShopifyParsedTurnover.txt",
                "CompleteShopify.txt"
            };
            for (String filename : filenames) {
                clearFileContent(getAppDataFilePath(filename));
            }
            System.out.println("Files cleared successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        ensureAppDataFolderExists();
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("Created " + filePath + " successfully");
        } else {
            new BufferedWriter(new FileWriter(file)).close();
            System.out.println("Cleared " + filePath + " successfully");
        }
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
