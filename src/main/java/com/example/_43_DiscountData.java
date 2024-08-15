package com.example;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.*;
import java.util.ArrayList; // import the ArrayList class
import java.util.Arrays;


public class _43_DiscountData {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private Preferences prefs;
    private static final String PREF_CSV_PATH = "csvFilePath";
    private static final String APP_DATA_FOLDER_PATH = System.getProperty("user.home") + "/Downloads/JDSAppData";
    private List<String> brands;


    public _43_DiscountData() {
        initialize();
    }

    private void initialize() {
        brands = _09_BrandList.getBrands();
        frame = new JFrame("Discount Data");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int frameWidth = 1750;
        frame.setSize(frameWidth, 600);
        frame.setLayout(new BorderLayout());
        
        prefs = Preferences.userRoot().node(this.getClass().getName());
        
        // Initialize the table model and table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("Title");
        tableModel.addColumn("Category");
        tableModel.addColumn("Brand");
        tableModel.addColumn("Non-discount Sales USD");
        tableModel.addColumn("Discount Sales USD");
        tableModel.addColumn("Total Sales");
        tableModel.addColumn("Sales Lift");
        tableModel.addColumn("Total Discount");
        tableModel.addColumn("Time Frame");
        
        // Adjust the column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(600);  // Title
        table.getColumnModel().getColumn(1).setPreferredWidth(130);   // Category
        table.getColumnModel().getColumn(2).setPreferredWidth(95);   // Brand
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Non-discount Sales USD
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Discount Sales USD
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Total Sales
        table.getColumnModel().getColumn(6).setPreferredWidth(50);   // Sales Lift
        table.getColumnModel().getColumn(7).setPreferredWidth(25);   // Total Discount
        table.getColumnModel().getColumn(8).setPreferredWidth(150);  // Time Frame
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Discount Data"));
        frame.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose());
        JButton chooseFileButton = new JButton("Manually Choose File");
        chooseFileButton.addActionListener(e -> openFileChooser());
        buttonPanel.add(backButton);
        buttonPanel.add(chooseFileButton);
        
        JButton sortButton = new JButton("Sort by Category & Brand");
        sortButton.addActionListener(e -> showSortByCategoryAndBrand());
        buttonPanel.add(sortButton);
    
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        List<String[]> discountData = loadDiscountData();
        Map<String, Map<String, double[]>> metric = calculateCategoryAndBrandMetrics(discountData);
        displayDiscountDataInTable(discountData);
    }
    

    private void openFileChooser() {
        JFrame dropFrame = new JFrame("Drag and Drop CSV File");
        dropFrame.setSize(400, 200);
        dropFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel dropPanel = new JPanel();
        dropPanel.setLayout(new BorderLayout());
        JLabel dropLabel = new JLabel("Drag and drop your CSV file here", SwingConstants.CENTER);
        dropPanel.add(dropLabel, BorderLayout.CENTER);

        dropPanel.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferHandler.TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @SuppressWarnings("unchecked")
            public boolean importData(TransferHandler.TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                try {
                    Transferable transferable = support.getTransferable();
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        File file = files.get(0);
                        if (file.getName().toLowerCase().endsWith(".csv")) {
                            prefs.put(PREF_CSV_PATH, file.getAbsolutePath());
                            processCSVFile(file.getAbsolutePath(), true);
                            dropFrame.dispose();
                            return true;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Please drop a CSV file.", "Invalid File Type", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        dropFrame.add(dropPanel);
        dropFrame.setVisible(true);
    }

    private void processCSVFile(String filePath, boolean isUpdate) {
        System.out.println("Processing CSV file: " + filePath);
    
        ensureAppDataFolderExists(); // Ensure the directory exists
    
        List<String[]> rowData = new ArrayList<>(); // Initialize rowData
    
        boolean titleFound = false;
        boolean BaseSalesUSDFound = false;
        boolean DiscountSalesUSDFound = false;
        boolean TotalSalesUSDFound = false;
        boolean SalesLiftFound = false;
        boolean totalDiscountFound = false;
        boolean StartDateFound = false;
        boolean EndDateFound = false;
    
        int titleColumnIndex = -1;
        int BaseSalesUSDColumnIndex = -1;
        int DiscountSalesUSDColumnIndex = -1;
        int TotalSalesUSDColumnIndex = -1;
        int SalesLiftColumnIndex = -1;
        int totalDiscountColumnIndex = -1;
        int StartDateColumnIndex = -1;
        int EndDateColumnIndex = -1;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines().parse(reader);
    
            int rowCount = 0;
            for (CSVRecord record : records) {
                rowCount++;
                for (int i = 0; i < record.size(); i++) {
                    String cellValue = record.get(i).trim().toLowerCase();
                    if (cellValue.equals("title") || cellValue.equals("offer title")) {
                        titleColumnIndex = i;
                        titleFound = true;
                    }
                    if (cellValue.equals("base sales usd")) {
                        BaseSalesUSDColumnIndex = i;
                        BaseSalesUSDFound = true;
                    }
                    if (cellValue.equals("discount sales usd")) {
                        DiscountSalesUSDColumnIndex = i;
                        DiscountSalesUSDFound = true;
                    }
                    if (cellValue.equals("total sales usd")) {
                        TotalSalesUSDColumnIndex = i;
                        TotalSalesUSDFound = true;
                    }
                    if (cellValue.equals("sales lift %")) {
                        SalesLiftColumnIndex = i;
                        SalesLiftFound = true;
                    }
                    if (cellValue.equals("total discount usd")) {
                        totalDiscountColumnIndex = i;
                        totalDiscountFound = true;
                    }
                    if (cellValue.equals("start date")) {
                        StartDateColumnIndex = i;
                        StartDateFound = true;
                    }
                    if (cellValue.equals("end date")) {
                        EndDateColumnIndex = i;
                        EndDateFound = true;
                    }
                }
                if (titleFound && BaseSalesUSDFound && DiscountSalesUSDFound && TotalSalesUSDFound &&
                    SalesLiftFound && totalDiscountFound && StartDateFound && EndDateFound) break;
                if (rowCount >= 4) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to process CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (titleFound && BaseSalesUSDFound && DiscountSalesUSDFound && TotalSalesUSDFound &&
            SalesLiftFound && totalDiscountFound && StartDateFound && EndDateFound) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines().parse(reader);
                boolean skipHeader = true;
                for (CSVRecord record : records) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue;
                    }
    
                    if (record.size() > titleColumnIndex && record.size() > BaseSalesUSDColumnIndex && 
                        record.size() > DiscountSalesUSDColumnIndex && record.size() > TotalSalesUSDColumnIndex &&
                        record.size() > SalesLiftColumnIndex && record.size() > totalDiscountColumnIndex &&
                        record.size() > StartDateColumnIndex && record.size() > EndDateColumnIndex) {
                        
                        String title = record.get(titleColumnIndex).trim();
                        String baseSalesUSD = record.get(BaseSalesUSDColumnIndex).trim();
                        String discountSalesUSD = record.get(DiscountSalesUSDColumnIndex).trim();
                        String totalSalesUSD = record.get(TotalSalesUSDColumnIndex).trim();
                        String salesLift = record.get(SalesLiftColumnIndex).trim();
                        String totalDiscount = record.get(totalDiscountColumnIndex).trim();
                        String startDate = record.get(StartDateColumnIndex).trim();
                        String endDate = record.get(EndDateColumnIndex).trim();
    
                        String category = getCategoryFromTitle(title); // Get category from title
                        String brand = getBrandFromTitle(title); // Assuming you have a method to get brand from title
    
                        // Validate if all critical fields are correctly extracted
                        if (title.isEmpty() || category.isEmpty() || brand.isEmpty() || 
                            baseSalesUSD.isEmpty() || discountSalesUSD.isEmpty() || 
                            totalSalesUSD.isEmpty() || salesLift.isEmpty() || 
                            totalDiscount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                            System.err.println("Skipping incomplete record: " + record);
                            continue;
                        }
    
                        rowData.add(new String[]{title, category, brand, baseSalesUSD, discountSalesUSD, totalSalesUSD, salesLift, totalDiscount, startDate + " - " + endDate});
    
                        // Print out the data found
                        System.out.println("Title: " + title);
                        System.out.println("Category: " + category);
                        System.out.println("Brand: " + brand);
                        System.out.println("Base Sales USD: " + baseSalesUSD);
                        System.out.println("Discount Sales USD: " + discountSalesUSD);
                        System.out.println("Total Sales USD: " + totalSalesUSD);
                        System.out.println("Sales Lift: " + salesLift);
                        System.out.println("Total Discount: " + totalDiscount);
                        System.out.println("Start Date: " + startDate);
                        System.out.println("End Date: " + endDate);
                        System.out.println("----------------------------------------");
                    }
                }
                System.out.println("Finished processing data from CSV file");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to process CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Required columns not found.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Required columns not found.");
        }
    
        // Write the data to the file
        writeDiscountDataToFile(rowData);
    
        // Load and display the data in the table
        rowData = loadDiscountData();
        displayDiscountDataInTable(rowData);
    }
    

    private void writeDiscountDataToFile(List<String[]> rowData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("DiscountData.txt")))) {
            CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
            for (String[] record : rowData) {
                String formattedRecord = format.format(record);
                writer.write(formattedRecord);
                writer.newLine();
            }
            System.out.println("Finished writing formatted data to DiscountData.txt");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to write discount data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private Set<String> loadExistingItemNumbersFromFile(String filename) {
        Set<String> itemNumbers = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath(filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    itemNumbers.add(parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemNumbers;
    }
    



    private String getCategoryFromTitle(String title) {
        List<String> categories = _09_BrandList.getCategories2();
        for (String category : categories) {
            if (title.toLowerCase().contains(category.toLowerCase())) {
                return category;
            }
        }
        System.out.println("Debug715: jul 15 : Unknown category for title: " + title); // Print the title if no category is found
        return "Unknown"; // Return a default value if no category is found
    }


    private String getBrandFromTitle(String title) {
        for (String brand : brands) {
            if (title.toLowerCase().contains(brand.toLowerCase())) {
                return brand;
            }
        }
        return "Unknown"; // Return "Unknown" if no matching brand is found
    }
    




    private int findColumnIndex(Map<String, Integer> headerMap, String... possibleNames) {
        for (String name : possibleNames) {
            Integer index = headerMap.get(name.toLowerCase());
            if (index != null) {
                return index;
            }
        }
        throw new IllegalArgumentException("Required column not found: " + String.join(", ", possibleNames));
    }

    private void ensureAppDataFolderExists() {
        File appDataFolder = new File(System.getProperty("user.home") + "/Downloads/JDSAppData");
        if (!appDataFolder.exists()) {
            appDataFolder.mkdirs();
        }
    }
    
    private void displayDiscountDataInTable(List<String[]> rowData) {
        // Clear existing rows
        tableModel.setRowCount(0);
    
        // Add rows to the table
        for (String[] row : rowData) {
            tableModel.addRow(row);
        }
        // Revalidate and repaint the table to ensure data is displayed
        table.revalidate();
        table.repaint();
    }
    


    private String getAppDataFilePath(String filename) {
        return APP_DATA_FOLDER_PATH + "/" + filename;
     }

     private List<String[]> loadDiscountData() {
        List<String[]> discountData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("DiscountData.txt")))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines().withQuote('"').parse(reader);
            for (CSVRecord record : records) {
                int size = record.size();
                String[] parts = new String[size];
                for (int i = 0; i < size; i++) {
                    parts[i] = record.get(i);
                }
                discountData.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load discount data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return discountData;
    }
    
    private void showSortByCategoryAndBrand() {
        List<String[]> rowData = loadDiscountData();
    
        if (rowData == null || rowData.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No data available to sort.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Calculate the metrics for each category and brand
        Map<String, Map<String, double[]>> categoryBrandMetrics = calculateCategoryAndBrandMetrics(rowData);
    
        // Create the mini GUI to show the results
        JFrame resultFrame = new JFrame("Sort by Category & Brand");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setSize(1075, 600);
        resultFrame.setLayout(new BorderLayout());
    
        DefaultTableModel resultTableModel = new DefaultTableModel();
        JTable resultTable = new JTable(resultTableModel);
    
        resultTableModel.addColumn("Category");
        resultTableModel.addColumn("Brand");
        resultTableModel.addColumn("Avg Non-discount Sales ");
        resultTableModel.addColumn("Avg Discount Sales ");
        resultTableModel.addColumn("Avg Total Sales ");
        resultTableModel.addColumn("Avg Sales Lift %");
        resultTableModel.addColumn("Avg Total Discount ");
        resultTableModel.addColumn("Full Total Discount ");
    
        for (Map.Entry<String, Map<String, double[]>> categoryEntry : categoryBrandMetrics.entrySet()) {
            String category = categoryEntry.getKey();
            Map<String, double[]> brandMetrics = categoryEntry.getValue();
    
            for (Map.Entry<String, double[]> brandEntry : brandMetrics.entrySet()) {
                String brand = brandEntry.getKey();
                double[] metrics = brandEntry.getValue();
    
                // Format each metric to two decimal places
                resultTableModel.addRow(new Object[]{
                        category,
                        brand,
                        String.format("%.2f", metrics[0]),  // avgNonDiscountSalesUSD
                        String.format("%.2f", metrics[1]),  // avgDiscountSalesUSD
                        String.format("%.2f", metrics[2]),  // avgTotalSalesUSD
                        String.format("%.2f", metrics[3]),  // avgSalesLift
                        String.format("%.2f", metrics[4]),  // avgTotalDiscountUSD
                        String.format("%.2f", metrics[5])   // fullTotalDiscountUSD
                });
            }
        }
    
        // Set preferred column widths
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(230); // Category
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Brand
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Avg Non-discount Sales USD
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Avg Discount Sales USD
        resultTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Avg Total Sales USD
        resultTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Avg Sales Lift %
        resultTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Avg Total Discount USD
        resultTable.getColumnModel().getColumn(7).setPreferredWidth(150); // Full Total Discount USD
    
        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        resultFrame.add(resultScrollPane, BorderLayout.CENTER);
    
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> resultFrame.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        resultFrame.add(buttonPanel, BorderLayout.SOUTH);
    
        resultFrame.setLocationRelativeTo(frame);
        resultFrame.setVisible(true);
    }
    
   
    private Map<String, Map<String, double[]>> calculateCategoryAndBrandMetrics(List<String[]> discountData) {
        Map<String, Map<String, double[]>> metricsMap = new HashMap<>();
    
        for (String[] row : discountData) {
            try {
                double baseSalesUSD = Double.parseDouble(row[3]);
                double discountSalesUSD = Double.parseDouble(row[4]);
                double totalSalesUSD = Double.parseDouble(row[5]);
                double salesLift = Double.parseDouble(row[6]);
                double totalDiscountUSD = Double.parseDouble(row[7]);
    
                String category = row[1];
                String brand = row[2];
    
                metricsMap.computeIfAbsent(category, k -> new HashMap<>())
                          .computeIfAbsent(brand, k -> new double[8]);
    
                double[] metrics = metricsMap.get(category).get(brand);
                metrics[0] += baseSalesUSD;   // Sum base sales
                metrics[1] += discountSalesUSD; // Sum discount sales
                metrics[2] += totalSalesUSD; // Sum total sales
                metrics[3] += salesLift;     // Sum sales lift
                metrics[4] += totalDiscountUSD; // Sum total discount
                metrics[5] += 1;             // Count for averaging
            } catch (NumberFormatException e) {
                System.err.println("Skipping non-numeric row: " + Arrays.toString(row));
                continue;
            }
        }
    
        // Calculate averages
        for (Map<String, double[]> brandMetrics : metricsMap.values()) {
            for (double[] metrics : brandMetrics.values()) {
                metrics[0] /= metrics[5]; // Average base sales
                metrics[1] /= metrics[5]; // Average discount sales
                metrics[2] /= metrics[5]; // Average total sales
                metrics[3] /= metrics[5]; // Average sales lift
                metrics[4] /= metrics[5]; // Average total discount
            }
        }
    
        return metricsMap;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new _43_DiscountData());
    }
}
