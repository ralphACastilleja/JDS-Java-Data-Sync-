package com.example;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
public class _16_PWmov2 {
    private static final SimpleDateFormat DATE_FORMAT_AVG = new SimpleDateFormat("MMM-dd-yy", Locale.US);
    private static final SimpleDateFormat DATE_FORMAT_START = new SimpleDateFormat("MMM-dd-yy HH:mm:ss z", Locale.US);
    private static final String APP_DATA_FOLDER_PATH = System.getProperty("user.home") + "/Downloads/JDSAppData";
    private static final String AVERAGE_PRICE_FILE = getAppDataFilePath("average_prices.txt");
    private static final String START_DATE_FILE = getAppDataFilePath("2ndCsvFileD.txt");
    private static final String TURNOVER_FILE = getAppDataFilePath("TurnoverD.txt");
    public static void calculateTurnoverTimes() {
        ensureAppDataFolderExists();
        Map<String, String[]> itemSaleData = loadItemData(AVERAGE_PRICE_FILE, true, new HashMap<>());
        Map<String, String[]> itemStartData = loadItemData(START_DATE_FILE, false, new HashMap<>());
        Map<String, String[]> existingTurnoverData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TURNOVER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String itemNumber = parts[0];
                    existingTurnoverData.put(itemNumber, parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TURNOVER_FILE, false))) {
            for (Map.Entry<String, String[]> entry : itemSaleData.entrySet()) {
                String itemNumber = entry.getKey();
                String[] saleData = entry.getValue();
                String saleDateStr = saleData[1];
                String itemTitle = saleData[0];
                String category = saleData.length > 2 ? saleData[2] : "Unknown";
                if (itemStartData.containsKey(itemNumber)) {
                    String[] startData = itemStartData.get(itemNumber);
                    String startDateStr = startData[1];
                    String itemBrand = startData[2];
                    if (startDateStr.isEmpty() || saleDateStr.isEmpty()) {
                        System.err.println("Missing dates for item " + itemNumber + ": startDate=" + startDateStr + ", saleDate=" + saleDateStr);
                        continue;
                    }
                    try {
                        Date saleDate = DATE_FORMAT_AVG.parse(saleDateStr);
                        Date startDate = DATE_FORMAT_START.parse(startDateStr);
                        long diffInMillies = Math.abs(saleDate.getTime() - startDate.getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                        writer.write(itemNumber + "," + itemBrand + "," + itemTitle + "," + category + "," + startDateStr + "," + saleDateStr + "," + diff + " days");
                        writer.newLine();
                        System.out.println("Calculated turnover time for item " + itemNumber + ": " + diff + " days");
                    } catch (ParseException e) {
                        System.err.println("Failed to parse dates for item " + itemNumber + ": " + startDateStr + ", " + saleDateStr);
                    }
                } else if (existingTurnoverData.containsKey(itemNumber)) {
                    writer.write(String.join(",", existingTurnoverData.get(itemNumber)));
                    writer.newLine();
                    System.out.println("Writing existing data for item " + itemNumber);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        printAverageTurnoverTimesPerBrand();
    }
    private static Map<String, String[]> loadItemData(String filename, boolean isSaleFile, Map<String, String[]> existingData) {
        ensureAppDataFolderExists();
        Map<String, String[]> itemData = new HashMap<>(existingData);
        List<String> categories = _09_BrandList.getCategories();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String itemNumber = parts[2];
                    if (itemData.containsKey(itemNumber)) {
                        continue;
                    }
                    String date = parts[3];
                    String title = parts[0];
                    String brand = parts.length > 4 ? parts[4] : "Unknown";
                    String category = isSaleFile ? getCategoryFromTitle(title, categories) : brand;
                    itemData.put(itemNumber, new String[]{title, date, category, brand});
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemData;
    }
    public static String getCategoryFromTitle(String title, List<String> categories) {
        title = title.toLowerCase();
        for (String category : categories) {
            int index = title.indexOf(category.toLowerCase());
            if (index != -1) {
                boolean isExactMatch = (index == 0 || !Character.isLetterOrDigit(title.charAt(index - 1))) &&
                                       (index + category.length() == title.length() || !Character.isLetterOrDigit(title.charAt(index + category.length())));
                if (isExactMatch) {
                    System.out.println("Matched category: " + category + " in title: " + title);
                    return category;
                } else {
                    System.out.println("Partial match found but not exact for category: " + category + " in title: " + title);
                }
            }
        }
        System.out.println("No category matched for title: " + title);
        return "Unknown";
    }
    public static void printAverageTurnoverTimesPerBrand() {
        ensureAppDataFolderExists();
        Map<String, List<Long>> brandTurnoverTimes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TURNOVER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String brand = parts[2];
                    long turnoverTime = Long.parseLong(parts[6].split(" ")[0]);
                    brandTurnoverTimes.computeIfAbsent(brand, k -> new ArrayList<>()).add(turnoverTime);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Double> averageTurnoverTimes = new HashMap<>();
        for (Map.Entry<String, List<Long>> entry : brandTurnoverTimes.entrySet()) {
            String brand = entry.getKey();
            List<Long> turnoverTimes = entry.getValue();
            double average = turnoverTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
            averageTurnoverTimes.put(brand, average);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TURNOVER_FILE, true))) {
            writer.newLine();
            writer.write("Average Turnover Times per Brand:");
            writer.newLine();
            for (Map.Entry<String, Double> entry : averageTurnoverTimes.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + " days");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getAppDataFilePath(String filename) {
        return APP_DATA_FOLDER_PATH + "/" + filename;
    }
    private static void ensureAppDataFolderExists() {
        File appDataFolder = new File(APP_DATA_FOLDER_PATH);
        if (!appDataFolder.exists()) {
            appDataFolder.mkdirs();
        }
    }
}
