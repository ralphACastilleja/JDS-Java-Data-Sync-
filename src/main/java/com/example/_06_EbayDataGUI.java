package com.example;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.prefs.Preferences;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import com.formdev.flatlaf.FlatDarkLaf;

import java.util.Arrays;
import java.util.Properties;
import java.io.FileInputStream;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;




import java.awt.*;


//try (BufferedReader reader = new BufferedReader(new FileReader("average_prices.txt"))) {




    public class _06_EbayDataGUI {


        private static final String PREF_CSV_PATH = "csvFilePath";
        private static final String AVERAGE_TURNOVER_FILE = "average_turnover_times.txt";
        private JFrame frame;
        private Map<String, List<Double>> brandPrices;
        @SuppressWarnings("unused")
        private Map<String, Integer> brandOccurrences;
        private List<String> brands;
        private Preferences prefs;
        private Map<String, List<String>> brandItemNumbers;
        private Map<String, List<String>> brandSaleDates;
        private Map<String, List<String>> brandCategories;
        private Map<String, List<String>> itemTitles;
        @SuppressWarnings("unused")
        private String category; // Add category instance variable
    
    
        public static void createEbayGUI(JFrame parentFrame, String category) {
            new _06_EbayDataGUI(parentFrame);
        }
    
    
    
    
        public _06_EbayDataGUI() {
            this(null); // default values
        }
    
    
    
    
    
    
    
    
    
    
        //Constructor JFrame
        public _06_EbayDataGUI(JFrame parentFrame) {
            
            _12_Cat2.calculateTurnoverTimes();
    
    
            prefs = Preferences.userRoot().node(this.getClass().getName());
    
    
            frame = new JFrame("Average Price / completed Orders Data");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 600);
            frame.setLayout(new BorderLayout());
            loadAndApplyTheme(); 
    
    
    
    
            frame.setLocationRelativeTo(parentFrame);
    
    
            brandPrices = new HashMap<>();
            brandOccurrences = new HashMap<>();
            brandItemNumbers = new HashMap<>();
            brandSaleDates = new HashMap<>();
            brandCategories = new HashMap<>();
    
    
            initializeComponents();
            frame.setVisible(true);
    
    
            // stored data  startup 
            Map<String, List<Double>> storedPrices = loadAveragePrices();
            calculateAndDisplayAverages(storedPrices);
        }
    
    
       private void initializeComponents() {
        brands = _09_BrandList.getBrands();
    
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    
    
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            frame.dispose(); 
            new _00_StartingGUI();
        });
        buttonPanel.add(backButton);
    
    
        JButton chooseFileButton = new JButton("Manually Choose File");
        chooseFileButton.addActionListener(e -> openFileChooser());
        buttonPanel.add(chooseFileButton);
    
    
        JButton infoButton = new JButton("Info");
        infoButton.addActionListener(e -> showInfoDialog());
        buttonPanel.add(infoButton);
    
    
        JButton turnoverButton = new JButton("Switch to Turnover Time");
        turnoverButton.addActionListener(e -> showTurnoverView());
        turnoverButton.setFont(new Font("Arial", Font.BOLD, 16)); 
        buttonPanel.add(turnoverButton);
    
    
        JButton discountDataButton = new JButton("Discount Data");
    discountDataButton.setFont(new Font("Arial", Font.BOLD, 14)); 
    discountDataButton.addActionListener(e -> {
        new _43_DiscountData();
    });
    buttonPanel.add(discountDataButton);
    
    
    
    
    
    
        JButton categoryAverageButton = new JButton("View Category Averages");
    categoryAverageButton.addActionListener(e -> showCategoryChooserForAverages());
    buttonPanel.add(categoryAverageButton);
    
    
    
    
        JButton brandDistributionButton = new JButton("Brand Distribution Chart");
        brandDistributionButton.addActionListener(e -> showBrandDistributionChart());
        buttonPanel.add(brandDistributionButton);
        JButton categoryDistributionButton = new JButton("Category Distribution Chart");
        categoryDistributionButton.addActionListener(e -> showCategoryDistributionChart());
        buttonPanel.add(categoryDistributionButton);
        
    
    
        JButton priceRangeDistributionButton = new JButton("Show Price Range Distribution Chart");
        priceRangeDistributionButton.addActionListener(e -> showPriceRangeDistributionChart());
        buttonPanel.add(priceRangeDistributionButton);
    
    
        JButton yearDistributionButton = new JButton("Year Distribution Chart");
        yearDistributionButton.addActionListener(e -> showYearDistributionChart());
        buttonPanel.add(yearDistributionButton);
    
    
    
    
        JButton statsButton = new JButton("Show Statistics");
        statsButton.addActionListener(e -> showStatistics());
        buttonPanel.add(statsButton);
    
    
    
    
        JButton updateShopifyDataButton = new JButton("Update Shopify Data");
        updateShopifyDataButton.setFont(updateShopifyDataButton.getFont().deriveFont(updateShopifyDataButton.getFont().getSize() * 0.8f)); // Adjust the font size if needed
        updateShopifyDataButton.addActionListener(e -> {
            _42_ShopifyCompiler compiler = new _42_ShopifyCompiler();
            compiler.compileShopifyData("2024-07-01T00:00:00Z", "2030-07-19T23:59:59Z");
        });
        buttonPanel.add(updateShopifyDataButton);
        
    
    
        JButton deleteAveragePriceDataButton = new JButton("Delete Stored Data");
        deleteAveragePriceDataButton.addActionListener(e -> deleteStoredAveragePriceData());
        buttonPanel.add(deleteAveragePriceDataButton);
    
    
        JScrollPane scrollPane = new JScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(frame.getWidth(), 40));
    
    
        frame.add(scrollPane, BorderLayout.SOUTH);
    }
    
    
    
    
    //Loads our data from the file we store teh average prices  
       private List<String> loadSaleDates() {
           List<String> saleDates = new ArrayList<>();
           try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
               while ((line = reader.readLine()) != null) {
                   String[] parts = line.split(",");
                   if (parts.length >= 5) {
                       String saleDate = parts[3]; 
                       saleDates.add(saleDate);
              
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(frame, "Failed to load sale dates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           }
           return saleDates;
       }
    
    
    
    
       //Chart for years
       @SuppressWarnings("unchecked")
    private void showYearDistributionChart() {
           List<String> saleDates = loadSaleDates();
    
    
    
    
           Map<String, Integer> yearOccurrences = new HashMap<>();
           String[] years = { "2022", "2023", "2024", "2025"};
           for (String year : years) {
               yearOccurrences.put(year, 0);
           }
    
    
    
    
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yy");
           for (String saleDate : saleDates) {
               try {
                   LocalDate date = LocalDate.parse(saleDate, formatter);
                   String year = String.valueOf(date.getYear());
                   if (yearOccurrences.containsKey(year)) {
                       yearOccurrences.put(year, yearOccurrences.get(year) + 1);
                   }
               } catch (DateTimeParseException e) {
                   System.err.println("Skipping invalid date: " + saleDate);
               }
           }
    
    
    
    
           DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
           for (Map.Entry<String, Integer> entry : yearOccurrences.entrySet()) {
               dataset.setValue(entry.getKey(), entry.getValue());
           }
    
    
    
    
           JFreeChart chart = ChartFactory.createPieChart(
               "Year Distribution",
               dataset,
               true, // legend
               true, // tooltips
               false // URLs
           );
    
    
    
    
           PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
           plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           plot.setSectionPaint("Section", Color.GREEN);
    
    
    
    
           plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           chart.addSubtitle(new TextTitle("Year Distribution of Completed Orders:"));
           chart.addSubtitle(new TextTitle("Since: 2022"));
           chart.addSubtitle(new TextTitle("Amount of Orders: 8000"));
           ChartPanel chartPanel = new ChartPanel(chart);
           chartPanel.setPreferredSize(new Dimension(2000, 1600));
           JFrame chartFrame = new JFrame("Ebay Completed Orders Year Distribution");
           chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           chartFrame.add(chartPanel, BorderLayout.CENTER);
    
    
    
    
           JButton backButton = new JButton("Back");
           backButton.addActionListener(e -> chartFrame.dispose());
           backButton.setPreferredSize(new Dimension(500, 100));
    
    
    
    
           JPanel buttonPanel = new JPanel();
           buttonPanel.add(backButton);
           chartFrame.add(buttonPanel, BorderLayout.SOUTH);
    
    
    
    
           chartFrame.pack();
           chartFrame.setVisible(true);
       }
    
    
    
    
    
    
      // Price range pie chart 
       @SuppressWarnings("unchecked")
    private void showPriceRangeDistributionChart() {
           List<Double> prices = loadPrices();
    
    
    
    
           int[] priceRanges = new int[7]; 
           for (double price : prices) {
               if (price < 100) {
                   priceRanges[0]++;
               } else if (price < 200) {
                   priceRanges[1]++;
               } else if (price < 400) {
                   priceRanges[2]++;
               } else if (price < 800) {
                   priceRanges[3]++;
               } else if (price < 1200) {
                   priceRanges[4]++;
               } else if (price < 1600) {
                   priceRanges[5]++;
               } else {
                   priceRanges[6]++;
               }
           }
    
    
    
    
           DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
           String[] labels = {"Below 100", "100-200", "200-400", "400-800", "800-1200", "1200-1600", "1600+"};
           for (int i = 0; i < priceRanges.length; i++) {
               dataset.setValue(labels[i], priceRanges[i]);
           }
    
    
    
    
           JFreeChart chart = ChartFactory.createPieChart(
               "Price Range Distribution",
               dataset,
               true, // legend
               true, // tooltips
               false // URLs
           );
    
    
    
    
           PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
           plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           plot.setSectionPaint("Section", Color.GREEN);
    
    
    
    
           plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           chart.addSubtitle(new TextTitle("Price Range Distribution of Completed Orders:"));
           chart.addSubtitle(new TextTitle("Since: 2022"));
           chart.addSubtitle(new TextTitle("Amount of Orders: 8000"));
    
    
    
    
           ChartPanel chartPanel = new ChartPanel(chart);
           chartPanel.setPreferredSize(new Dimension(2000, 1600));
           JFrame chartFrame = new JFrame("Ebay Completed Orders Price Range Distribution");
           chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           chartFrame.add(chartPanel, BorderLayout.CENTER);
    
    
    
    
           JButton backButton = new JButton("Back");
           backButton.addActionListener(e -> chartFrame.dispose());
           JPanel buttonPanel = new JPanel();
           buttonPanel.add(backButton);
           chartFrame.add(buttonPanel, BorderLayout.SOUTH);
    
    
    
    
           chartFrame.pack();
           chartFrame.setVisible(true);
       }
    
    
    
    
    
    
       //Loads Data fromn the average prices
       private List<Double> loadPrices() {
           List<Double> prices = new ArrayList<>();
           try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
               while ((line = reader.readLine()) != null) {
                   String[] parts = line.split(",");
                   if (parts.length >= 5) {
                       double price = Double.parseDouble(parts[1]);
                       prices.add(price);
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(frame, "Failed to load prices: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           }
           return prices;
       }
    
    
    
    
       //Category distrubtion chart
       @SuppressWarnings("unchecked")
    private void showCategoryDistributionChart() {
           Map<String, Integer> categoryOccurrences = loadCategoryOccurrences();
    
    
    
    
           DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
           for (Map.Entry<String, Integer> entry : categoryOccurrences.entrySet()) {
               dataset.setValue(entry.getKey(), entry.getValue());
           }
    
    
    
    
           JFreeChart chart = ChartFactory.createPieChart(
               "Category Distribution",
               dataset,
               true, // legend
               true, // tooltips
               false // URLs
           );
    
    
           PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
           plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           plot.setSectionPaint("Section", Color.GREEN);
    
    
    
    
           plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           chart.addSubtitle(new TextTitle("Category Distribution of Completed Orders:"));
           chart.addSubtitle(new TextTitle("Since: 2022"));
           chart.addSubtitle(new TextTitle("Amount of Orders: 8000"));
           ChartPanel chartPanel = new ChartPanel(chart);
           chartPanel.setPreferredSize(new Dimension(2000, 1600));
           JFrame chartFrame = new JFrame("Ebay Completed Orders Category Distribution");
           chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           chartFrame.add(chartPanel, BorderLayout.CENTER);
    
    
    
    
           JButton backButton = new JButton("Back");
           backButton.addActionListener(e -> chartFrame.dispose());
           JPanel buttonPanel = new JPanel();
           buttonPanel.add(backButton);
           chartFrame.add(buttonPanel, BorderLayout.SOUTH);
    
    
    
    
           chartFrame.pack();
           chartFrame.setVisible(true);
       }
    
    
    
    
    
    
       //Load Cat occurences and store them 
       private Map<String, Integer> loadCategoryOccurrences() {
        Map<String, Integer> categoryOccurrences = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) { 
                    String category = parts[4]; 
                    categoryOccurrences.put(category, categoryOccurrences.getOrDefault(category, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load category occurrences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return categoryOccurrences;
    }
    
    
    //Loads dialog obox
    private void showCategoryChooserForAverages() {
        List<String> categories = _09_BrandList.getCategories();
        String[] categoryArray = categories.toArray(new String[0]);
        String selectedCategory = (String) JOptionPane.showInputDialog(
            frame,
            "Select a category:",
            "Category Chooser",
            JOptionPane.PLAIN_MESSAGE,
            null,
            categoryArray,
            categoryArray[0]
        );
    
    
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            Map<String, Double> averagePrices = calculateAveragePricePerCategory(selectedCategory);
            displayCategoryAverages(selectedCategory, averagePrices);
        }
    }
    
    
    //Once the user has clicked  cateogry this method is used to display specific data 
    private void displayCategoryAverages(String category, Map<String, Double> averagePrices) {
        JPanel resultPanel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
    
    
        tableModel.addColumn("Brand");
        tableModel.addColumn("Average Price");
    
    
        for (Map.Entry<String, Double> entry : averagePrices.entrySet()) {
            String brand = entry.getKey();
            double averagePrice = entry.getValue();
            tableModel.addRow(new Object[]{brand, String.format("%.2f", averagePrice)});
        }
    
    
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(186, 186, 186));
    
    
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.PLAIN, 17));
                ((JComponent) c).setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(72, 72, 72)));
                return c;
            }
        };
    
    
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    
    
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Average Prices for Category: " + category));
    
    
        resultPanel.add(scrollPane, BorderLayout.CENTER);
    
    
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> ((JDialog) SwingUtilities.getWindowAncestor(resultPanel)).dispose());
    
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    
        JDialog resultDialog = new JDialog(frame, "Category Average Prices", true); 
        resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        resultDialog.add(resultPanel);
        resultDialog.setSize(800, 600);
        resultDialog.setLocationRelativeTo(frame);
        resultDialog.setVisible(true);
    }
    
    
    
    
    
    
    //Calculates average pruce of items using specific data
    private Map<String, Double> calculateAveragePricePerCategory(String category) {
        Map<String, List<Double>> brandPrices = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equalsIgnoreCase(category)) {
                    String brand = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    brandPrices.computeIfAbsent(brand, k -> new ArrayList<>()).add(price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to calculate average prices: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    
        Map<String, Double> averagePrices = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
            String brand = entry.getKey();
            List<Double> prices = entry.getValue();
            if (!prices.isEmpty()) {
                double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                averagePrices.put(brand, averagePrice);
            }
        }
    
    
        return averagePrices;
    }
    
    
    
    
    //Pie chart Brand distrubutioon 
       @SuppressWarnings("unchecked")
    private void showBrandDistributionChart() {
           Map<String, Integer> brandOccurrences = loadBrandOccurrences();
    
    
    
    
           DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
           for (Map.Entry<String, Integer> entry : brandOccurrences.entrySet()) {
               dataset.setValue(entry.getKey(), entry.getValue());
           }
    
    
    
    
           JFreeChart chart = ChartFactory.createPieChart(
               "Brand Distribution",
               dataset,
               true, // legend
               true, // tooltips
               false // URLs
           );
    
    
    
    
           PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
           plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           plot.setSectionPaint("Section", Color.GREEN);
    
    
    
    
           plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
           chart.addSubtitle(new TextTitle("Brand Distribution of Completed orders:"));
           chart.addSubtitle(new TextTitle("Since: 2022"));
           chart.addSubtitle(new TextTitle("Amount of Orders: 8000"));
           ChartPanel chartPanel = new ChartPanel(chart);
           chartPanel.setPreferredSize(new Dimension(2000, 1600));
           JFrame chartFrame = new JFrame("Ebay completed orders Brand Distribution");
           chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           chartFrame.add(chartPanel, BorderLayout.CENTER);
    
    
    
    
           JButton backButton = new JButton("Back");
           backButton.addActionListener(e -> chartFrame.dispose());
           JPanel buttonPanel = new JPanel();
           buttonPanel.add(backButton);
           chartFrame.add(buttonPanel, BorderLayout.SOUTH);
    
    
    
    
           chartFrame.pack();
           chartFrame.setVisible(true);
       }
    
    
    
    
       //method to insert CSV FILE
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
                            processCSVFile(file.getAbsolutePath(), false);
                            refreshGUI();
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
    
    
    
    
    
    
    //Caclautes and Displays Using stored data fromn the CSV File
    private void calculateAndDisplayAverages(Map<String, List<Double>> brandPrices) {
           Map<String, Double> averagePrices = new HashMap<>();
           Map<String, Integer> brandOccurrences = new HashMap<>();
    
    
    
    
           for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
               String brand = entry.getKey();
               List<Double> prices = entry.getValue();
               if (!prices.isEmpty()) {
                   double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                   averagePrices.put(brand, averagePrice);
                   brandOccurrences.put(brand, prices.size());
                //   System.out.println("Brand: " + brand + ", Average Price: " + averagePrice + ", Occurrences: " + prices.size());
               }
           }
    
    
    
    
           displayBrandAverages(averagePrices, brandOccurrences);
       }
    
    
    
    
       private void displayBrandAverages(Map<String, Double> averagePrices, Map<String, Integer> brandOccurrences) {
           JPanel resultPanel = new JPanel(new BorderLayout());
           DefaultTableModel tableModel = new DefaultTableModel();
           JTable table = new JTable(tableModel);
    
    
    
    
           tableModel.addColumn("Brand");
           tableModel.addColumn("Average Price");
           tableModel.addColumn("Occurrences");
    
    
    
    
           for (Map.Entry<String, Double> entry : averagePrices.entrySet()) {
               String brand = entry.getKey();
               double averagePrice = entry.getValue();
               int occurrences = brandOccurrences.get(brand);
               tableModel.addRow(new Object[]{brand, String.format("%.2f", averagePrice), occurrences});
           }
    
    
    
    
           table.setFillsViewportHeight(true);
           table.setShowGrid(true);
           table.setGridColor(new Color(186, 186, 186)); //  dark grey
    
    
    
    
           DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
               @Override
               public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                   Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                   c.setFont(new Font("Arial", Font.PLAIN, 17));
                   ((JComponent) c).setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(72, 72, 72))); // Dark grey border
                   return c;
               }
           };
    
    
    
    
           for (int i = 0; i < table.getColumnCount(); i++) {
               table.getColumnModel().getColumn(i).setCellRenderer(renderer);
           }
    
    
    
    
           JScrollPane scrollPane = new JScrollPane(table);
           scrollPane.setBorder(BorderFactory.createTitledBorder("Brands, Average Prices, Occurrences"));
    
    
    
    
           resultPanel.add(scrollPane, BorderLayout.CENTER);
    
    
    
    
           frame.add(resultPanel, BorderLayout.CENTER);
           frame.revalidate();
           frame.repaint();
       }
    
    
    
    
       //Loads the average turnover time by looking at the average turnover time file 
       @SuppressWarnings("unused")
    private Map<String, Double> loadAverageTurnoverTimes() {
           Map<String, Double> averageTurnoverTimes = new HashMap<>();
           try (BufferedReader reader = new BufferedReader(new FileReader(AVERAGE_TURNOVER_FILE))) {
               String line;
               while ((line = reader.readLine()) != null) {
                   String[] parts = line.split(":");
                   if (parts.length == 2) {
                       String brand = parts[0].trim();
                       double averageTurnoverTime = Double.parseDouble(parts[1].trim());
                       averageTurnoverTimes.put(brand, averageTurnoverTime);
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(frame, "Failed to load average turnover times: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           }
           return averageTurnoverTimes;
       }
    
    
    
    
       private void showTurnoverView() {
           _08_TunT.createEbayGUI(frame);
           frame.setVisible(false);
       }
    
    
    //Creates a new frame to display a new gui 
       @SuppressWarnings("unused")
    private void showAveragePriceView() {
           JFrame avgPriceFrame = new JFrame("Average Price View");
           avgPriceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           avgPriceFrame.setSize(800, 600);
    
    
    
    
           JPanel buttonPanel = new JPanel();
           avgPriceFrame.add(buttonPanel, BorderLayout.SOUTH);
    
    
    
    
           JButton chooseFileButton = new JButton("Choose CSV File");
           chooseFileButton.addActionListener(e -> openFileChooser());
           buttonPanel.add(chooseFileButton);
    
    
    
    
           JButton infoButton = new JButton("Info");
           infoButton.addActionListener(e -> showInfoDialog());
           buttonPanel.add(infoButton);
    
    
    
    
           JButton turnoverButton = new JButton("Switch to Turnover Time");
           turnoverButton.addActionListener(e -> {
               avgPriceFrame.dispose();
               showTurnoverView();
           });
           buttonPanel.add(turnoverButton);
    
    
    
    
           DefaultTableModel tableModel = new DefaultTableModel();
           JTable table = new JTable(tableModel);
    
    
    
    
           tableModel.addColumn("Brand");
           tableModel.addColumn("Average Price");
           tableModel.addColumn("Predicted Turnover Time");
    
    
    
    
           Map<String, String> predictedTurnoverTimes = _09_BrandList.getBrandsWithTurnoverTimes();
           for (String brand : brands) {
               String predictedTurnover = predictedTurnoverTimes.getOrDefault(brand, "N/A");
               tableModel.addRow(new Object[]{brand, "Data from CSV", predictedTurnover});
           }
    
    
    
    
           table.setFillsViewportHeight(true);
           table.setShowGrid(true);
           table.setGridColor(Color.LIGHT_GRAY);
    
    
    
    
           JScrollPane scrollPane = new JScrollPane(table);
           scrollPane.setBorder(BorderFactory.createTitledBorder("Brands, Average Prices, and Predicted Turnover Times"));
    
    
    
    
           avgPriceFrame.add(scrollPane, BorderLayout.CENTER);
           avgPriceFrame.revalidate();
           avgPriceFrame.repaint();
           avgPriceFrame.setVisible(true);
       }
    
    
    
    
       //If a csv file was inputed Update everything 
       @SuppressWarnings("unused")
    private void updateAveragePriceData() {
           JFileChooser fileChooser = new JFileChooser();
           fileChooser.setDialogTitle("Select CSV File");
           int result = fileChooser.showOpenDialog(frame);
           if (result == JFileChooser.APPROVE_OPTION) {
               File selectedFile = fileChooser.getSelectedFile();
               prefs.put(PREF_CSV_PATH, selectedFile.getAbsolutePath());
               processCSVFile(selectedFile.getAbsolutePath(), true);
               saveItemData(brandPrices, brandItemNumbers, brandSaleDates, brandCategories, itemTitles);
               calculateAndDisplayAverages(brandPrices); 
               JOptionPane.showMessageDialog(frame, "Average Price Data Updated.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
           }
       }
    
    
    
    
       //extensive method to processes the inputed CSV file, Main data processing method of file 
       private void processCSVFile(String filePath, boolean isUpdate) {
        System.out.println("Processing CSV file: " + filePath);
    
    
        boolean titleFound = false;
        boolean soldForFound = false;
        boolean itemNumberFound = false;
        boolean saleDateFound = false;
        int itemTitleColumnIndex = -1;
        int soldForColumnIndex = -1;
        int itemNumberColumnIndex = -1;
        int saleDateIndex = -1;
    
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines().parse(reader);
    
    
            int rowCount = 0;
            for (CSVRecord record : records) {
                rowCount++;
                for (int i = 0; i < record.size(); i++) {
                    String cellValue = record.get(i).trim().toLowerCase();
                    if (cellValue.equals("item title") || cellValue.equals("title")) {
                        itemTitleColumnIndex = i;
                        titleFound = true;
                    }
                    if (cellValue.equals("sold for") || cellValue.equals("price")) {
                        soldForColumnIndex = i;
                        soldForFound = true;
                    }
                    if (cellValue.equals("item number") || cellValue.equals("number")) {
                        itemNumberColumnIndex = i;
                        itemNumberFound = true;
                    }
                    if (cellValue.equals("sale date") || cellValue.equals("date")) {
                        saleDateIndex = i;
                        saleDateFound = true;
                    }
                }
                if (titleFound && soldForFound && itemNumberFound && saleDateFound) break;
                if (rowCount >= 4) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to process CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
    
        if (titleFound && soldForFound && itemNumberFound && saleDateFound) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("totalOrders.txt"), true))) { // Append mode
    
    
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines().parse(reader);
                boolean skipHeader = true;
                for (CSVRecord record : records) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue;
                    }
    
    
                    if (record.size() > itemTitleColumnIndex && record.size() > soldForColumnIndex && record.size() > itemNumberColumnIndex && record.size() > saleDateIndex) {
                        String itemTitle = record.get(itemTitleColumnIndex).trim();
                        String soldForString = record.get(soldForColumnIndex).trim();
                        String itemNumber = record.get(itemNumberColumnIndex).trim();
                        String saleDate = record.get(saleDateIndex).trim();
    
    
                        try {
                            double price = Double.parseDouble(soldForString.replace("$", "").replace(",", ""));
                            String category = getCategoryFromTitle(itemTitle);
                            for (String brand : brands) {
                                if (itemTitle.toLowerCase().contains(brand.toLowerCase())) {
                                    writer.write(brand + "," + price + "," + itemNumber + "," + saleDate + "," + category);
                                    writer.newLine();
                                    break;
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping invalid price: " + soldForString);
                        }
                    }
                }
                System.out.println("Finished writing data to totalOrders.txt");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to process CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } 
        } else {
            JOptionPane.showMessageDialog(frame, "Required columns not found.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Required columns not found.");
        }
    
    
        saveUniqueItemData();
    }
    
    
    
    
    //gets category by searching string for a category in teh 09 class
    private String getCategoryFromTitle(String title) {
        List<String> categories = _09_BrandList.getCategories();
        for (String category : categories) {
            if (title.toLowerCase().contains(category.toLowerCase())) {
                return category;
            }
        }
        System.out.println("Debug715: jul 15 : Unknown category for title: " + title); 
        return "Unknown"; 
    }
    
    
    
    
    
    
    
    
    //If there is no repeats in the CSV file save iit
    private void saveUniqueItemData() {
        Set<String> existingItemNumbers = loadExistingItemNumbersFromFile("average_prices.txt");
    
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("average_prices.txt"), true))) {
            try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("totalOrders.txt")))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String itemNumber = parts[2];
                        if (!existingItemNumbers.contains(itemNumber)) {
                            writer.write(line);
                            writer.newLine();
                            existingItemNumbers.add(itemNumber);
                        } else {
               //             System.out.println("Duplicate item number found: " + itemNumber + ". Skipping this record.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to read data from totalOrders.txt");
            }
            System.out.println("Finished writing unique data to average_prices.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write data to average_prices.txt");
        }
    
    
    try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("totalOrders.txt")))) {
        int totalCount = 0;
        while (reader.readLine() != null) totalCount++;
        System.out.println("Total entries in totalOrders.txt: " + totalCount);
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    
    try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
        int totalCount = 0;
        while (reader.readLine() != null) totalCount++;
        System.out.println("Total unique entries in average_prices.txt: " + totalCount);
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    
    
    
    }
    
    
    
    
    //Loads item numbers to pricesses if they have already been in the program, to disable repeat data 
    @SuppressWarnings("unused")
    private Map<String, List<String>> loadExistingItemNumbers() {
        Map<String, List<String>> itemNumbers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String brand = parts[0];
                    String itemNumber = parts[2];
                    itemNumbers.computeIfAbsent(brand, k -> new ArrayList<>()).add(itemNumber);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load existing item numbers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return itemNumbers;
    }
    
    
    
    
    //Save data if A new item(by checking item number)
    private void saveItemData(Map<String, List<Double>> brandPrices, Map<String, List<String>> brandItemNumbers, Map<String, List<String>> brandSaleDates, Map<String, List<String>> brandCategories, Map<String, List<String>> itemTitles) {
        Set<String> processedItemNumbers = new HashSet<>();
    
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("average_prices.txt"), true))) {
            for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
                String brand = entry.getKey();
                List<Double> prices = entry.getValue();
                List<String> itemNumbers = brandItemNumbers.get(brand);
                List<String> saleDates = brandSaleDates.get(brand);
                List<String> categories = brandCategories.get(brand);
                List<String> titles = itemTitles.get(brand);
    
    
                if (itemNumbers == null || saleDates == null || categories == null || titles == null) continue;
    
    
                for (int i = 0; i < prices.size(); i++) {
                    if (i >= itemNumbers.size() || i >= saleDates.size() || i >= categories.size() || i >= titles.size()) continue;
                    String itemNumber = itemNumbers.get(i);
                    String saleDate = saleDates.get(i);
                    String category = categories.get(i);
                    String title = titles.get(i);
                    if (!processedItemNumbers.contains(itemNumber)) {
                        writer.write(brand + "," + prices.get(i) + "," + itemNumber + "," + saleDate + "," + title + "," + category);
                        writer.newLine();
                        processedItemNumbers.add(itemNumber); 
                    }
                }
            }
         //   System.out.println("Finished writing data to average_prices.txt");
        } catch (IOException e) {
            e.printStackTrace();
         //   System.out.println("Failed to write data to average_prices.txt");
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private Map<String, List<Double>> loadAveragePrices() {
        Map<String, List<Double>> averagePrices = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String brand = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    averagePrices.computeIfAbsent(brand, k -> new ArrayList<>()).add(price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load average prices: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return averagePrices;
    }
    
    

    //Processes file that we Download from the _00_ class automatically 
    public static void processDownloadedCSVFile() {
        String downloadFilepath = System.getProperty("user.home") + "/Downloads";
        File downloadDir = new File(downloadFilepath);
        
        File[] files = downloadDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        
        if (files != null && files.length > 0) {
           
            for (File file : files) {
                System.out.println(file.getName());
            }
    
    
            File ordersReportFile = Arrays.stream(files)
                    .filter(file -> file.getName().toLowerCase().contains("ebay-all-orders-report"))
                    .max(java.util.Comparator.comparingLong(File::lastModified))
                    .orElse(null);
            
            if (ordersReportFile != null) {
             //   System.out.println("Processing the orders report CSV file: " + ordersReportFile.getAbsolutePath());
                _06_EbayDataGUI ebayDataGUI = new _06_EbayDataGUI();
                ebayDataGUI.processCSVFile(ordersReportFile.getAbsolutePath(), false);
                ebayDataGUI.refreshGUI(); // Ensure this method refreshes the GUI components
            } else {
           //     System.out.println("No orders report CSV file found.");
            }
        } else {
            System.out.println("No CSV files found in the download directory.");
        }
    }
    
    
    
    
    
    
    @SuppressWarnings("unused")
    private void saveTotalOrdersData(Map<String, List<Double>> brandPrices, Map<String, List<String>> brandItemNumbers, Map<String, List<String>> brandSaleDates, Map<String, List<String>> brandCategories, Map<String, List<String>> itemTitles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("totalOrders.txt"), true))) { // Append mode
            for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
                String brand = entry.getKey();
                List<Double> prices = entry.getValue();
                List<String> itemNumbers = brandItemNumbers.get(brand);
                List<String> saleDates = brandSaleDates.get(brand);
                List<String> categories = brandCategories.get(brand);
                List<String> titles = itemTitles.get(brand);
    
    
                if (itemNumbers == null || saleDates == null || categories == null || titles == null) continue;
    
    
                for (int i = 0; i < prices.size(); i++) {
                    if (i >= itemNumbers.size() || i >= saleDates.size() || i >= categories.size() || i >= titles.size()) continue;
                    String itemNumber = itemNumbers.get(i);
                    String saleDate = saleDates.get(i);
                    String category = categories.get(i);
                    String title = titles.get(i);
                    writer.write(brand + "," + prices.get(i) + "," + itemNumber + "," + saleDate + "," + category + "," + title);
                    writer.newLine();
                }
            }
           // System.out.println("Finished writing data to totalOrders.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write data to totalOrders.txt");
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
    
    
    
    
    
    
    //error message 
       private void showInfoDialog() {
           String infoMessage = "In order to view your eBay data, go to your eBay sellers hub, download your report, then if you're on Mac, go on File > Export As, CSV file, and then select the file inside the program.";
           JTextArea textArea = new JTextArea(infoMessage);
           textArea.setWrapStyleWord(true);
           textArea.setLineWrap(true);
           textArea.setCaretPosition(0);
           textArea.setEditable(false);
    
    
    
    
           JScrollPane scrollPane = new JScrollPane(textArea);
           scrollPane.setPreferredSize(new Dimension(500, 300));
    
    
    
    
           JOptionPane.showMessageDialog(frame, scrollPane, "Info", JOptionPane.INFORMATION_MESSAGE);
       }
    
    
    
    //Refreshes GUi incase of stale data 
       private void refreshGUI() {
           frame.getContentPane().removeAll(); 
           initializeComponents();
           frame.revalidate(); 
           frame.repaint();
           Map<String, List<Double>> storedPrices = loadAveragePrices(); 
           calculateAndDisplayAverages(storedPrices);
    
    
    
    
           // Debugging: Print the data to ensure it's loaded correctly
           System.out.println("Data after refresh:");
           for (Map.Entry<String, List<Double>> entry : storedPrices.entrySet()) {
               System.out.println("Brand: " + entry.getKey() + ", Prices: " + entry.getValue());
           }
       }
    
    
       
     @SuppressWarnings("unused")
    private void showCategoryChooser() {
           List<String> categories = _09_BrandList.getCategories(); 
           String[] categoryArray = categories.toArray(new String[0]);
           String selectedCategory = (String) JOptionPane.showInputDialog(
               frame,
               "Select a category:",
               "Category Chooser",
               JOptionPane.PLAIN_MESSAGE,
               null,
               categoryArray,
               categoryArray[0]
           );
    
    
    
    
           if (selectedCategory != null && !selectedCategory.isEmpty()) {
               displayDataForCategory(selectedCategory);
           }
       }
    
    

       //Button to delete data in the Folder we stre data, mainly for debugging 
       private void deleteStoredAveragePriceData() {
        int response = JOptionPane.showConfirmDialog(frame,
            "This is permanent. Are you sure you want to delete all saved data?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
    
    
        if (response == JOptionPane.YES_OPTION) {
            ensureAppDataFolderExists(); 
    
    
            String[] filenames = {
                "average_prices.txt",
                "2ndCsvFile.txt",
                "Turnover.txt",
                "average_turnover_times.txt",
                "links.txt",
                "listings.txt",
                "processed_item_numbers.txt",
                "totalOrders.txt",
                "totalListings.txt" 
            };
    
    
            try {
                for (String filename : filenames) {
                    File file = new File(getAppDataFilePath(filename));
                    if (file.exists()) {
                        new BufferedWriter(new FileWriter(file)).close();
                        System.out.println("Cleared " + filename + " successfully");
                    } else {
                        file.createNewFile();
                        System.out.println("Created " + filename + " successfully");
                    }
                }
    
    
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("totalOrders.txt"), false))) {
                    writer.write("");
                    System.out.println("Appended empty string to totalOrders.txt successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
    
    
                JOptionPane.showMessageDialog(frame, "Stored turnover data cleared successfully.", "Clear Successful", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Stored turnover data cleared successfully.");
                refreshGUI();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to clear stored turnover data.", "Clear Failed", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            System.out.println("Deletion cancelled by user.");
        }
    }
    
    
    
    
    
    
    
       private void displayDataForCategory(String category) {
           System.out.println("Selected category: " + category);
           Map<String, Double> filteredAveragePrices = new HashMap<>();
           Map<String, Integer> filteredBrandOccurrences = new HashMap<>();
    
    
    
    
           try (BufferedReader reader = new BufferedReader(new FileReader(AVERAGE_TURNOVER_FILE))) {
               String line;
               boolean inAverageSection = false;
               while ((line = reader.readLine()) != null) {
                   if (line.startsWith("Average Turnover Times per Brand:")) {
                       inAverageSection = true;
                       continue;
                   }
                   if (!inAverageSection) {
                       String[] parts = line.split(",");
                       if (parts.length >= 5 && parts[4].equalsIgnoreCase(category)) {
                           String brand = parts[2];
                           double turnoverTime = Double.parseDouble(parts[6].split(" ")[0]);
                           filteredAveragePrices.put(brand, filteredAveragePrices.getOrDefault(brand, 0.0) + turnoverTime);
                           filteredBrandOccurrences.put(brand, filteredBrandOccurrences.getOrDefault(brand, 0) + 1);
                           System.out.println("Filtered data: " + brand + " - Turnover time: " + turnoverTime);
                       }
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(frame, "Failed to load data for category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           }
    
    
    
    
           displayBrandAverages(filteredAveragePrices, filteredBrandOccurrences);
       }
    
    
    
    
       public static String get1stCSVPath() {
           Preferences prefs = Preferences.userRoot().node(_06_EbayDataGUI.class.getName());
           return prefs.get(PREF_CSV_PATH, null);
       }
    
    
    
    //Loads the number of brand occurences by looking at the text file, 
       private Map<String, Integer> loadBrandOccurrences() {
           Map<String, Integer> brandOccurrences = new HashMap<>();
           try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
               while ((line = reader.readLine()) != null) {
                   String[] parts = line.split(",");
                   if (parts.length >= 5) {
                       String brand = parts[0];
                       brandOccurrences.put(brand, brandOccurrences.getOrDefault(brand, 0) + 1);
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(frame, "Failed to load brand occurrences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           }
           return brandOccurrences;
       }
    
    
    
    //GUI for some stats 
       private void showStatistics() {
           List<Double> prices = loadPrices();
           _31_StatsCalc.calculateAndDisplayStatistics(prices, frame);
       }
    
    
    
    
    
    
    
    
    @SuppressWarnings("unused")
    private void showSalesPerformance() {
        Map<String, Integer> activeListings = loadActiveListings();
        Map<String, Integer> completedOrders = loadCompletedOrders();
    
    
    
        Map<String, Double> conversionRates = new HashMap<>();
        for (String brand : activeListings.keySet()) {
            int activeCount = activeListings.getOrDefault(brand, 0);
            int completedCount = completedOrders.getOrDefault(brand, 0);
            double conversionRate = activeCount > 0 ? (double) completedCount / activeCount : 0;
            conversionRates.put(brand, conversionRate);
        }
    
    
        JFrame resultFrame = new JFrame("Sales Performance");
        resultFrame.setSize(800, 600);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    
        JPanel resultPanel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        tableModel.addColumn("Brand");
        tableModel.addColumn("Conversion Rate");
    
    
        for (Map.Entry<String, Double> entry : conversionRates.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), String.format("%.2f%%", entry.getValue() * 100)});
        }
    
    
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Sales Performance (Conversion Rates)"));
        resultPanel.add(scrollPane, BorderLayout.CENTER);
    
    
        resultFrame.add(resultPanel, BorderLayout.CENTER);
        resultFrame.setVisible(true);
    }
    private Map<String, Integer> loadActiveListings() {
        Map<String, Integer> activeListings = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String brand = parts[0];
                    int count = (int) Double.parseDouble(parts[1].trim());
                    activeListings.put(brand, activeListings.getOrDefault(brand, 0) + count);
                }
            }
            // Debug output for Aristo
           // System.out.println("Total Active Listings for Aristo: " + activeListings.getOrDefault("Aristo", 0));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load active listings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return activeListings;
    }
    
    

    //Loads the completed orders for data purposes 
    private Map<String, Integer> loadCompletedOrders() {
        Map<String, Integer> completedOrders = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFile.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String brand = parts[0];
                    int count = (int) Double.parseDouble(parts[1].trim());
                    completedOrders.put(brand, completedOrders.getOrDefault(brand, 0) + count);
                }
            }
            // Debug output for Aristo
         //   System.out.println("Total Completed Orders for Aristo: " + completedOrders.getOrDefault("Aristo", 0));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load completed orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return completedOrders;
    }
    
    
    
    //Gets the app data file path
    
    private String getAppDataFilePath(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }
    
    
    
    //if it dosent exist make it 
    private void ensureAppDataFolderExists() {
        String appDataFolderPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        File appDataFolder = new File(appDataFolderPath);
        if (!appDataFolder.exists()) {
            appDataFolder.mkdirs();
        }
    }
    
    
    
    
    
    //Themes 
        private void loadAndApplyTheme() {
            Properties themeProperties = new Properties();
            try (FileInputStream in = new FileInputStream(getAppDataFilePath("settings.properties"))) {
                themeProperties.load(in);
                String themeClassName = themeProperties.getProperty("selectedTheme", FlatDarkLaf.class.getName());
                UIManager.setLookAndFeel(themeClassName);
            } catch (Exception e) {
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } catch (UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
            }
            SwingUtilities.updateComponentTreeUI(frame);
        }
    
    
    
    
    
    
    
    
    @SuppressWarnings("unused")
    private Set<String> loadProcessedItemNumbers() {
        Set<String> processedItemNumbers = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("average_prices.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    processedItemNumbers.add(parts[2].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processedItemNumbers;
    }
    
    
    
    
    
    
    }
    
