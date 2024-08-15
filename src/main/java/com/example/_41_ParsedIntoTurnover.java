package com.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Collections;  // <-- Add this import

public class _41_ParsedIntoTurnover {

    //Processes parsed turnover data and injects it into general and category-specific turnover files
    public void processParsedTurnover() {
        String shopifyParsedFilePath = getAppDataFilePath("ShopifyParsedTurnover.txt");
        String generalTurnoverFilePath = getAppDataFilePath("Turnover.txt");

        try {
            List<String> lines = Files.readAllLines(Paths.get(shopifyParsedFilePath));
            Set<String> generalTurnoverData = new HashSet<>(Files.readAllLines(Paths.get(generalTurnoverFilePath)));

            for (String line : lines) {
                injectIntoGeneralTurnover(line, generalTurnoverFilePath, generalTurnoverData);
                injectIntoCategoryTurnover(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Injects a line into the general turnover file, ensuring no duplicates
    private void injectIntoGeneralTurnover(String line, String generalTurnoverFilePath, Set<String> generalTurnoverData) throws IOException {
        if (!generalTurnoverData.contains(line)) {
            List<String> existingLines = Files.readAllLines(Paths.get(generalTurnoverFilePath)); //Read existing content

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(generalTurnoverFilePath), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) { //Create a new file with the new line at the top
                writer.write(line);
                writer.newLine();
                for (String existingLine : existingLines) {
                    writer.write(existingLine);
                    writer.newLine();
                }
            }
        }
    }

    //Injects a line into the appropriate category turnover file based on the category
    private void injectIntoCategoryTurnover(String line) throws IOException {
        String[] parts = line.split(",");
    
        if (parts.length < 7) {
            System.err.println("Skipping invalid line: " + line);
            return;
        }
    
        String category = parts[1].trim().toLowerCase(Locale.ENGLISH);
    
        Path categoryFilePath = getCategoryFilePath(category); //get the corresponding file path based on the category
    
        if (categoryFilePath != null) {
            if (Files.exists(categoryFilePath)) {
                Set<String> categoryData = new HashSet<>(Files.readAllLines(categoryFilePath));
                if (categoryData.add(line)) {
                    Files.write(categoryFilePath, Collections.singleton(line), StandardOpenOption.APPEND);
                }
            } else {
                Files.write(categoryFilePath, Collections.singleton(line), StandardOpenOption.CREATE); //If the file does not exist, create it and write the line
            }
        } else {
            System.err.println("Unknown category: " + category);
        }
    }

    //Returns the file path for the category-specific turnover file
    private Path getCategoryFilePath(String category) {
        switch (category.toLowerCase()) {
            case "wristwatch":
                return Paths.get(getAppDataFilePath("TurnoverB.txt"));
            case "wristwatch movement":
                return Paths.get(getAppDataFilePath("TurnoverC.txt"));
            case "pocket watch movement":
                return Paths.get(getAppDataFilePath("TurnoverD.txt"));
            case "band":
                return Paths.get(getAppDataFilePath("TurnoverE.txt"));
            case "pocket watch case":
                return Paths.get(getAppDataFilePath("TurnoverF.txt"));
            case "parts":
                return Paths.get(getAppDataFilePath("TurnoverG.txt"));
            case "collet":
                return Paths.get(getAppDataFilePath("TurnoverH.txt"));
            case "balance assembly":
                return Paths.get(getAppDataFilePath("TurnoverI.txt"));
            case "watch movement holder":
                return Paths.get(getAppDataFilePath("TurnoverJ.txt"));
            case "pocket watch":
                return Paths.get(getAppDataFilePath("TurnoverK.txt"));
            default:
                System.err.println("Unknown category: " + category);
                return null;
        }
    }

    //Returns the full path to a file in the application data folder
    private String getAppDataFilePath(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }
}
