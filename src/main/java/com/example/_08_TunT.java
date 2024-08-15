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
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.prefs.Preferences;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.*;
import java.util.Iterator;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.formdev.flatlaf.FlatDarkLaf;

//create the eBay GUI
public class _08_TunT {
    private static String PREF_CSV_PATH = "csvFilePath";
    @SuppressWarnings("unused")
    private static final String AVERAGE_TURNOVER_FILE = "2ndCsvFile.txt";
    @SuppressWarnings("unused")
    private static final String TURNOVER_FILE = "Turnover.txt";
    private static final String APP_DATA_FOLDER_PATH = System.getProperty("user.home") + "/Downloads/JDSAppData";
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
    private String selectedCategory; 


    //create the eBay GUI
    public static void createEbayGUI(JFrame parentFrame) {
        new _08_TunT(parentFrame);
    }

    //load the selected theme from properties file
    @SuppressWarnings("unused")
    private static String loadSelectedTheme() {
        Properties themeProperties = new Properties();
        try (FileInputStream in = new FileInputStream(get("settings.properties"))) {
            themeProperties.load(in);
            return themeProperties.getProperty("selectedTheme", FlatDarkLaf.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
            // Return default theme class name if loading fails
            return FlatDarkLaf.class.getName();
        }
    }

    //get the full path for a given filename in the app data folder
    private static String get(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }

    //save the selected theme to a properties file
    @SuppressWarnings("unused")
    private void saveSelectedTheme(String themeClassName) {
        try {
            Properties themeProperties = new Properties();
            themeProperties.setProperty("selectedTheme", themeClassName);
            FileOutputStream out = new FileOutputStream(getAppDataFilePath("settings.properties"));
            themeProperties.store(out, "Theme Settings");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // No-argument constructor
    public _08_TunT() {
        this(null);
    }

    // Constructor with JFrame parameter
    public _08_TunT(JFrame parentFrame) {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        frame = new JFrame("Turnover/ Listing Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 600);
        frame.setLayout(new BorderLayout());
        applySavedTheme();

        // Set the icon image
        URL iconUrl = getClass().getResource("/JDS.png");
        if (iconUrl != null) {
            ImageIcon JDS = new ImageIcon(iconUrl);
            frame.setIconImage(JDS.getImage());
            System.out.println("Icon loaded successfully");
        } else {
            System.err.println("Icon resource not found: /JDS.png");
        }
        // Center the frame relative to the parent frame
        frame.setLocationRelativeTo(parentFrame);
        brandPrices = new HashMap<>();
        brandOccurrences = new HashMap<>();
        brandItemNumbers = new HashMap<>();
        brandSaleDates = new HashMap<>();
        brandCategories = new HashMap<>();
        initializeComponents();
        frame.setVisible(true);
        // Load stored data on startup without reprocessing CSV file
        Map<String, Double> storedTurnoverTimes = loadAverageTurnoverTimes();
        calculateAndDisplayAverages(storedTurnoverTimes);  // Correct method call
    }

    //initialize components in the GUI
    private void initializeComponents() {
        brands = _09_BrandList.getBrands();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton chooseFileButton = new JButton("Manually Choose File");
        chooseFileButton.addActionListener(e -> openFileChooser());
        buttonPanel.add(chooseFileButton);

        JButton infoButton = new JButton("Info");
        infoButton.addActionListener(e -> showInfoDialog());
        buttonPanel.add(infoButton);

        JButton categoryChooserButton = new JButton("Choose Category");
        categoryChooserButton.addActionListener(e -> showCategoryChooser());
        buttonPanel.add(categoryChooserButton);

        JButton turnoverButton = new JButton("Switch to Average Price Time");
        turnoverButton.setFont(new Font("Arial", Font.BOLD, 16));
        turnoverButton.addActionListener(e -> showTurnoverView());
        buttonPanel.add(turnoverButton);

        JButton discountDataButton = new JButton("Discount Data");
        discountDataButton.setFont(new Font("Arial", Font.BOLD, 14));
        discountDataButton.addActionListener(e -> new _43_DiscountData());
        buttonPanel.add(discountDataButton);

        JButton yearDistributionButton = new JButton("Year Distribution Chart");
        yearDistributionButton.addActionListener(e -> showYearDistributionChart());
        buttonPanel.add(yearDistributionButton);

        JButton priceRangeDistributionButton = new JButton("Price Range Distribution Chart");
        priceRangeDistributionButton.addActionListener(e -> showPriceRangeDistributionChart());
        buttonPanel.add(priceRangeDistributionButton);

        JButton categoryDataChartButton = new JButton("Category Data Chart");
        categoryDataChartButton.addActionListener(e -> showCategoryDataChart());
        buttonPanel.add(categoryDataChartButton);

        JButton statsButton = new JButton("Show Statistics");
        statsButton.addActionListener(e -> showStatistics());
        buttonPanel.add(statsButton);

        JButton updateShopifyDataButton = new JButton("Update Shopify Data");
        updateShopifyDataButton.setFont(updateShopifyDataButton.getFont().deriveFont(updateShopifyDataButton.getFont().getSize() * 0.8f));
        updateShopifyDataButton.addActionListener(e -> {
            _42_ShopifyCompiler compiler = new _42_ShopifyCompiler();
            compiler.compileShopifyData("2024-07-01T00:00:00Z", "2030-07-19T23:59:59Z");
        });
        buttonPanel.add(updateShopifyDataButton);

        JButton deleteTurnover = new JButton("Delete Stored Data");
        deleteTurnover.addActionListener(e -> deleteStoredTurnovereData());
        buttonPanel.add(deleteTurnover);

        // Wrap the button panel in a JScrollPane with horizontal scrolling
        JScrollPane scrollPane = new JScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(frame.getWidth(), 40)); // Adjust size as needed

        frame.add(scrollPane, BorderLayout.SOUTH);
    }

    //open a file chooser for selecting a CSV file
    private void openFileChooser() {
        JFrame dropFrame = new JFrame("Drag and Drop CSV File");
        dropFrame.setSize(400, 200);
        dropFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel dropPanel = new JPanel();
        dropPanel.setLayout(new BorderLayout());
        JLabel dropLabel = new JLabel("Drag and drop your CSV file here", SwingConstants.CENTER);
        dropPanel.add(dropLabel, BorderLayout.CENTER);

        dropPanel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @SuppressWarnings("unchecked")
            @Override
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

    //get the CSV path from preferences
    public static String getCSVPath() {
        Preferences prefs = Preferences.userRoot().node(_08_TunT.class.getName());
        return prefs.get(PREF_CSV_PATH, null);
    }

    //process a downloaded CSV file
    public static void processDownloadedCSVFile2() {
        String downloadFilepath = System.getProperty("user.home") + "/Downloads";
        File downloadDir = new File(downloadFilepath);

        File[] files = downloadDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        System.out.println("Download directory: " + downloadFilepath);
        System.out.println("Number of CSV files found: " + (files != null ? files.length : 0));

        if (files != null && files.length > 0) {
            System.out.println("CSV files found:");
            for (File file : files) {
                System.out.println(file.getName());
            }

            File ordersReportFile = Arrays.stream(files)
                    .filter(file -> file.getName().toLowerCase().contains("ebay-all-active-listings-report"))
                    .max(java.util.Comparator.comparingLong(File::lastModified))
                    .orElse(null);

            if (ordersReportFile != null) {
                System.out.println("Processing the orders report CSV file: " + ordersReportFile.getAbsolutePath());
                _08_TunT instance = new _08_TunT();
                instance.processCSVFile(ordersReportFile.getAbsolutePath(), false);
                instance.refreshGUI();
                System.out.println("Processed CSV file: " + ordersReportFile.getName());
            } else {
                System.out.println("No orders report CSV file found.");
            }
        } else {
            System.out.println("No CSV files found in the download directory.");
        }
    }

    //process the CSV file from preferences
    public void processCSVFromPrefs() {
        String csvPath = getCSVPath();
        if (csvPath != null) {
            System.out.println("Processing file: " + csvPath);
            processCSVFile(csvPath, false);
        } else {
            System.out.println("No file path found in preferences.");
        }
    }

    //process a CSV file
    @SuppressWarnings("static-access")
    public void processCSVFile(String filePath, boolean isUpdate) {
        System.out.println("DEBUG: Processing CSV file: " + filePath);
        Map<String, List<Double>> existingPrices = loadAveragePrices();

        Map<String, List<Double>> brandPrices = new HashMap<>(existingPrices);
        Map<String, List<String>> brandItemNumbers = new HashMap<>();
        Map<String, List<String>> brandSaleDates = new HashMap<>();
        Map<String, List<String>> brandCategories = new HashMap<>();
        Map<String, Integer> brandOccurrences = new HashMap<>();
        itemTitles = new HashMap<>();

        Set<String> processedItemNumbers = new HashSet<>();

        for (String brand : brands) {
            brandOccurrences.put(brand, brandPrices.getOrDefault(brand, new ArrayList<>()).size());
        }

        int itemTitleColumnIndex = -1;
        int soldForColumnIndex = -1;
        int itemNumberColumnIndex = -1;
        int saleDateIndex = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreSurroundingSpaces().withAllowMissingColumnNames().withIgnoreEmptyLines();
            try (CSVParser parser = new CSVParser(reader, csvFormat)) {
                List<String> headers = parser.getHeaderNames();
                Map<String, Integer> headerMap = parser.getHeaderMap();

                System.out.println("DEBUG: Headers found: " + headers);

                Map<String, Integer> headerIndices = findHeaderIndices(headers);

                itemTitleColumnIndex = headerIndices.getOrDefault("itemTitle", -1);
                soldForColumnIndex = headerIndices.getOrDefault("soldFor", -1);
                itemNumberColumnIndex = headerIndices.getOrDefault("itemNumber", -1);
                saleDateIndex = headerIndices.getOrDefault("saleDate", -1);
                itemTitleColumnIndex = headerMap.getOrDefault("Title", -1);

                if (itemTitleColumnIndex == -1 || soldForColumnIndex == -1 || itemNumberColumnIndex == -1 || saleDateIndex == -1) {
                    System.out.println("DEBUG: One or more columns not found using the first method. Falling back to the second method.");
                    headerIndices = findHeaderIndices(parser.iterator());

                    itemTitleColumnIndex = headerIndices.getOrDefault("itemTitle", -1);
                    soldForColumnIndex = headerIndices.getOrDefault("soldFor", -1);
                    itemNumberColumnIndex = headerIndices.getOrDefault("itemNumber", -1);
                    saleDateIndex = headerIndices.getOrDefault("saleDate", -1);
                }

                System.out.println("DEBUG: Columns found: itemTitleColumnIndex=" + itemTitleColumnIndex + ", soldForColumnIndex=" + soldForColumnIndex + ", itemNumberColumnIndex=" + itemNumberColumnIndex + ", saleDateIndex=" + saleDateIndex);

                if (itemNumberColumnIndex == -1) {
                    System.out.println("DEBUG: Item Number column not found. Defaulting to the first column.");
                    itemNumberColumnIndex = 0;
                }

                for (CSVRecord record : parser) {
                    if (record.size() > itemTitleColumnIndex && record.size() > soldForColumnIndex && record.size() > itemNumberColumnIndex && record.size() > saleDateIndex) {
                        String itemTitle = record.get(itemTitleColumnIndex).trim();
                        String soldForString = record.get(soldForColumnIndex).trim();
                        String itemNumber = record.get(itemNumberColumnIndex).trim();
                        String saleDate = record.get(saleDateIndex).trim();

                        if (!itemTitle.isEmpty() && !soldForString.isEmpty() && !processedItemNumbers.contains(itemNumber)) {
                            processedItemNumbers.add(itemNumber);
                            try {
                                double price = Double.parseDouble(soldForString.replace("$", "").replace(",", ""));
                                for (String brand : brands) {
                                    if (itemTitle.toLowerCase().contains(brand.toLowerCase())) {
                                        brandPrices.computeIfAbsent(brand, k -> new ArrayList<>()).add(price);
                                        brandItemNumbers.computeIfAbsent(brand, k -> new ArrayList<>()).add(itemNumber);
                                        brandSaleDates.computeIfAbsent(brand, k -> new ArrayList<>()).add(saleDate);
                                        itemTitles.computeIfAbsent(itemNumber, k -> new ArrayList<>()).add(itemTitle);

                                        boolean categoryFound = false;
                                        for (String category : _09_BrandList.getCategories()) {
                                            String categoryPattern = "\\b" + category.toLowerCase() + "\\b";
                                            if (itemTitle.toLowerCase().matches(".*" + categoryPattern + ".*")) {
                                                brandCategories.computeIfAbsent(brand, k -> new ArrayList<>()).add(category);
                                                categoryFound = true;
                                                break;
                                            }
                                        }
                                        if (!categoryFound) {
                                            brandCategories.computeIfAbsent(brand, k -> new ArrayList<>()).add("Unknown");
                                        }
                                        brandOccurrences.put(brand, brandOccurrences.getOrDefault(brand, 0) + 1);
                                        break;
                                    }
                                }
                            } catch (NumberFormatException e) {
                                // Skipping invalid price
                            }
                        }
                    }
                }
            }

            if (!isUpdate) {
                saveItemData(brandPrices, brandItemNumbers, brandSaleDates, brandCategories, itemTitles);
            }

            _10_Tcalc.calculateTurnoverTimes();
          
            _11_Cat1 eleven = new _11_Cat1();
            eleven.processCSVFromPrefs();
            _12_Cat2 twelve = new _12_Cat2();
            twelve.calculateTurnoverTimes();
   
            _13_WWMov1 thirteen = new _13_WWMov1();
            thirteen.processCSVFromPrefs();
            _14_WWMov2 fourteen = new _14_WWMov2();
            fourteen.calculateTurnoverTimes();
   
            _15_PWmov1 fifteen = new _15_PWmov1();
            fifteen.processCSVFromPrefs();
            _16_PWmov2 sixteen = new _16_PWmov2();
            sixteen.calculateTurnoverTimes();
   
            _17_band1 seventeen = new _17_band1();
            seventeen.processCSVFromPrefs();
            _18_band2 eighteen = new _18_band2();
            eighteen.calculateTurnoverTimes();
   
            _19_PWcase1 nineteen = new _19_PWcase1();
            nineteen.processCSVFromPrefs();
            _20_PWcase2 twenty = new _20_PWcase2();
            twenty.calculateTurnoverTimes();
   
            _21_parts1 twentyOne = new _21_parts1();
            twentyOne.processCSVFromPrefs();
            _22_parts2 twentyTwo = new _22_parts2();
            twentyTwo.calculateTurnoverTimes();
   
            _23_collet1 twentyThree = new _23_collet1();
            twentyThree.processCSVFromPrefs();
            _24_collet2 twentyFour = new _24_collet2();
            twentyFour.calculateTurnoverTimes();
   
            _25_balance1 twentyFive = new _25_balance1();
            twentyFive.processCSVFromPrefs();
            _26_balance2 twentySix = new _26_balance2();
            twentySix.calculateTurnoverTimes();

            _27_WWMovHold1 twentySeven = new _27_WWMovHold1();
            twentySeven.processCSVFromPrefs();
            _28_WWMovHold2 twentyEight = new _28_WWMovHold2();
            twentyEight.calculateTurnoverTimes();

            _29_PW twentyNine = new _29_PW();
            twentyNine.processCSVFromPrefs();
            _30_PW thirty = new _30_PW();
            thirty.calculateTurnoverTimes();

            calculateAndDisplayAverages(loadAverageTurnoverTimes()); // Load and display averages and occurrences

            System.out.println("DEBUG: Successfully processed and saved data from CSV file: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to process CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //find header indices in the CSV file based on a list of headers
    private Map<String, Integer> findHeaderIndices(List<String> headers) {
        Map<String, Integer> headerIndices = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).toLowerCase();
            if (header.contains("item number")) {
                headerIndices.put("itemNumber", i);
            } else if (header.contains("title")) {
                headerIndices.put("itemTitle", i);
            } else if (header.contains("sold for") || header.contains("start price")) {
                headerIndices.put("soldFor", i);
            } else if (header.contains("start date")) {
                headerIndices.put("saleDate", i);
            }
        }
        return headerIndices;
    }

    //find header indices in the CSV file based on an iterator over the records
    @SuppressWarnings("unused")
    private Map<String, Integer> findHeaderIndices(Iterator<CSVRecord> records) {
        Map<String, Integer> headerIndices = new HashMap<>();
        boolean titleFound = false;
        boolean soldForFound = false;
        boolean itemNumberFound = false;
        boolean saleDateFound = false;

        int rowCount = 0;
        while (records.hasNext()) {
            CSVRecord record = records.next();
            rowCount++;
            for (int i = 0; i < record.size(); i++) {
                String cellValue = record.get(i).trim().toLowerCase();
                if ((cellValue.equals("item title") || cellValue.equals("title")) && !titleFound) {
                    headerIndices.put("itemTitle", i);
                    titleFound = true;
                }
                if ((cellValue.equals("start price") || cellValue.equals("price") || cellValue.equals("sold for")) && !soldForFound) {
                    headerIndices.put("soldFor", i);
                    soldForFound = true;
                }
                if ((cellValue.equals("item number") || cellValue.equals("number")) && !itemNumberFound) {
                    headerIndices.put("itemNumber", i);
                    itemNumberFound = true;
                }
                if ((cellValue.equals("start date") || cellValue.equals("date") || cellValue.equals("sale date")) && !saleDateFound) {
                    headerIndices.put("saleDate", i);
                    saleDateFound = true;
                }
            }
            if (titleFound && soldForFound && itemNumberFound && saleDateFound) break;
        }
        return headerIndices;
    }

    //save item data to a file
    private void saveItemData(Map<String, List<Double>> brandPrices, Map<String, List<String>> brandItemNumbers, Map<String, List<String>> brandSaleDates, Map<String, List<String>> brandCategories, Map<String, List<String>> itemTitles) {
        Set<String> processedItemNumbers = new HashSet<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("2ndCsvFile.txt")))) {
            for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
                String brand = entry.getKey();
                List<Double> prices = entry.getValue();
                List<String> itemNumbers = brandItemNumbers.get(brand);
                List<String> saleDates = brandSaleDates.get(brand);
                List<String> categories = brandCategories.get(brand);

                if (itemNumbers == null || saleDates == null || categories == null || itemTitles == null) continue;

                for (int i = 0; i < prices.size(); i++) {
                    if (i >= itemNumbers.size() || i >= saleDates.size() || i >= categories.size() || i >= itemTitles.size()) continue;
                    String itemNumber = itemNumbers.get(i);
                    if (!processedItemNumbers.contains(itemNumber)) {
                        String saleDate = saleDates.get(i);
                        String category = categories.get(i);
                        String title = itemTitles.get(itemNumber).get(0);

                        writer.write(brand + "," + prices.get(i) + "," + itemNumber + "," + saleDate + "," + category + "," + title);
                        writer.newLine();
                        processedItemNumbers.add(itemNumber);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //calculate and display average turnover times
    private void calculateAndDisplayAverages(Map<String, Double> averageTurnoverTimes) {
        Map<String, Integer> brandOccurrences = calculateBrandOccurrences();
        displayBrandAverages(averageTurnoverTimes, brandOccurrences);
    }

    //calculate brand occurrences
    private Map<String, Integer> calculateBrandOccurrences() {
        Map<String, Integer> brandOccurrences = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("Turnover.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String brand = parts[2].trim();
                    brandOccurrences.put(brand, brandOccurrences.getOrDefault(brand, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load brand occurrences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return brandOccurrences;
    }

    //display brand averages in a table
    private void displayBrandAverages(Map<String, Double> averageTurnoverTimes, Map<String, Integer> brandOccurrences) {
        JPanel resultPanel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        tableModel.addColumn("Brand");
        tableModel.addColumn("Average Turnover Time (days)");
        tableModel.addColumn("Occurrences");
        tableModel.addColumn("Action");

        for (Map.Entry<String, Double> entry : averageTurnoverTimes.entrySet()) {
            String brand = entry.getKey();
            double averageTurnoverTime = entry.getValue();
            int occurrences = brandOccurrences.getOrDefault(brand, 0);
            tableModel.addRow(new Object[]{brand, String.format("%.2f", averageTurnoverTime), occurrences, "View Details"});
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

        _36_ButtonRendererEditor buttonRendererEditor = new _36_ButtonRendererEditor(new JCheckBox(), table, frame);
        table.getColumnModel().getColumn(3).setCellRenderer(buttonRendererEditor);
        table.getColumnModel().getColumn(3).setCellEditor(buttonRendererEditor);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Brands, Average Turnover Times, Occurrences"));
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(resultPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    //load average turnover times from a file
    private Map<String, Double> loadAverageTurnoverTimes() {
        Map<String, Double> averageTurnoverTimes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("Turnover.txt")))) {
            String line;
            boolean inAverageSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Average Turnover Times per Brand:")) {
                    inAverageSection = true;
                    continue;
                }
                if (inAverageSection) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String brand = parts[0].trim();
                        double averageTurnoverTime = Double.parseDouble(parts[1].trim().split(" ")[0]);
                        averageTurnoverTimes.put(brand, averageTurnoverTime);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load average turnover times: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return averageTurnoverTimes;
    }

    //switch to the turnover view in the GUI
    private void showTurnoverView() {
        String selectedCategory = "DefaultCategory";
        _06_EbayDataGUI.createEbayGUI(frame, selectedCategory);
        frame.setVisible(false);
    }

    //switch to the average price view in the GUI
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

    //update average price data in the GUI
    @SuppressWarnings("unused")
    private void updateAveragePriceData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            prefs.put(PREF_CSV_PATH, selectedFile.getAbsolutePath());
            processCSVFile(selectedFile.getAbsolutePath(), true); // Process CSV file as an update
            saveItemData(brandPrices, brandItemNumbers, brandSaleDates, brandCategories, itemTitles);
            Map<String, Double> storedTurnoverTimes = loadAverageTurnoverTimes(); // Load the updated data
            calculateAndDisplayAverages(storedTurnoverTimes); // Refresh the display
            JOptionPane.showMessageDialog(frame, "Average Price Data Updated.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //load average prices from a file
    private Map<String, List<Double>> loadAveragePrices() {
        Map<String, List<Double>> averagePrices = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFile.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String brand = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    averagePrices.computeIfAbsent(brand, k -> new ArrayList<>()).add(price);
                    System.out.println("Loaded price: " + price + " for brand: " + brand);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load average prices: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return averagePrices;
    }

    //display an information dialog
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

    //refresh the GUI components
    private void refreshGUI() {
        frame.getContentPane().removeAll();
        initializeComponents();
        frame.revalidate();
        frame.repaint();
        Map<String, Double> storedTurnoverTimes = loadAverageTurnoverTimes();
        calculateAndDisplayAverages(storedTurnoverTimes);
    }

    //delete stored turnover data
    private void deleteStoredTurnovereData() {
        int response = JOptionPane.showConfirmDialog(frame,
            "This is permanent. Are you sure you want to delete all saved data?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            ensureAppDataFolderExists();

            String[] filenames = {
                "2ndCsvFile.txt", "2ndCsvFileB.txt", "2ndCsvFileC.txt", "2ndCsvFileD.txt", "2ndCsvFileF.txt",
                "2ndCsvFileG.txt", "2ndCsvFileH.txt", "2ndCsvFileI.txt", "2ndCsvFileJ.txt", "2ndCsvFileK.txt",
                "links.txt", "listings.txt", "Turnover.txt", "TurnoverB.txt", "TurnoverC.txt", "TurnoverD.txt",
                "TurnoverE.txt", "TurnoverF.txt", "TurnoverG.txt", "TurnoverH.txt", "TurnoverI.txt",
                "TurnoverJ.txt", "TurnoverK.txt", "average_prices.txt"
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

    //show the category chooser dialog
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
            this.selectedCategory = selectedCategory;
            displayDataForCategory(selectedCategory);
        }
    }

    //display data for the selected category
    private void displayDataForCategory(String category) {
        if ("Wristwatch".equalsIgnoreCase(category)) {
            _11_Cat1.createEbayGUI(frame);
        } else if ("Wristwatch Movement".equalsIgnoreCase(category)) {
            _13_WWMov1.createEbayGUI(frame);
        } else if ("pocket watch movement".equalsIgnoreCase(category)) {
            _15_PWmov1.createEbayGUI(frame);
        } else if ("band".equalsIgnoreCase(category)) {
            _17_band1.createEbayGUI(frame);
        } else if ("pocket watch case".equalsIgnoreCase(category)) {
            _19_PWcase1.createEbayGUI(frame);
        } else if ("parts".equalsIgnoreCase(category)) {
            _21_parts1.createEbayGUI(frame);
        } else if ("Collet".equalsIgnoreCase(category)) {
            _23_collet1.createEbayGUI(frame);
        } else if ("Balance assembly".equalsIgnoreCase(category)) {
            _25_balance1.createEbayGUI(frame);
        } else if ("Watch Movement Holder".equalsIgnoreCase(category)) {
            _27_WWMovHold1.createEbayGUI(frame);
        } else if ("pocket watch".equalsIgnoreCase(category)) {
            _29_PW.createEbayGUI(frame);
        } else {
            JOptionPane.showMessageDialog(frame, "Selected category is not Wristwatch: " + category);
        }
    }

    //load sale dates from the CSV file
    private List<String> loadSaleDates() {
        List<String> saleDates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFile.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String saleDate = parts[3];
                    saleDates.add(saleDate.split(" ")[0]);
                    System.out.println("Loaded sale date: " + saleDate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load sale dates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return saleDates;
    }

    //load category occurrences from the CSV file
    private Map<String, Integer> loadCategoryOccurrences() {
        Map<String, Integer> categoryOccurrences = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFile.txt")))) {
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

    //show the year distribution chart
    @SuppressWarnings("unchecked")
    public void showYearDistributionChart() {
        List<String> saleDates = loadSaleDates();
        Map<String, Integer> yearOccurrences = new HashMap<>();
        String[] years = {"2022", "2023", "2024", "2025"};
        for (String year : years) {
            yearOccurrences.put(year, 0);
        }
        DateTimeFormatter.ofPattern("MMM-dd-yy HH:mm:ss z");
        for (String saleDate : saleDates) {
            try {
                LocalDate date = LocalDate.parse(saleDate.split(" ")[0], DateTimeFormatter.ofPattern("MMM-dd-yy"));
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
            true,
            true,
            false
        );
        chart.addSubtitle(new TextTitle("Since: 2022"));
        chart.addSubtitle(new TextTitle("More than: 4000 orders"));

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(2000, 1600));
        JFrame chartFrame = new JFrame("Listing Orders Year Distribution");
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

    //show the brand distribution chart
    @SuppressWarnings({ "unchecked", "unused" })
    private void showBrandDistributionChart() {
        Map<String, Integer> brandOccurrences = calculateBrandOccurrences();
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : brandOccurrences.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(
            "Brand Distribution",
            dataset,
            true,
            true,
            false
        );
        chart.addSubtitle(new TextTitle("Since: 2022"));
        chart.addSubtitle(new TextTitle("More than: 4000 orders"));

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(2000, 1600));
        JFrame chartFrame = new JFrame("Listing Orders Brand Distribution");
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

    //load prices from the CSV file
    private List<Double> loadPrices() {
        List<Double> prices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFile.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    double price = Double.parseDouble(parts[1]);
                    prices.add(price);
                    System.out.println("Loaded price: " + price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load prices: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return prices;
    }

    //show the price range distribution chart
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
            true,
            true,
            false
        );
        chart.addSubtitle(new TextTitle("Since: 2022"));
        chart.addSubtitle(new TextTitle("More than: 4000 orders"));

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

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

    //show the category distribution chart
    //show the category distribution chart
    @SuppressWarnings({ "unused", "unchecked" })
    private void showCategoryDistributionChart() {
        Map<String, Integer> categoryOccurrences = loadCategoryOccurrences();

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : categoryOccurrences.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Category Distribution",
            dataset,
            true,
            true,
            false
        );

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(2000, 1600));
        JFrame chartFrame = new JFrame("Listing Orders Category Distribution");
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

    //load prices from CSV using preferences
    private List<Double> loadPricesFromCSV() {
        List<Double> prices = new ArrayList<>();
        String csvPath = prefs.get(PREF_CSV_PATH, null);

        if (csvPath != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
                boolean foundSalesPriceHeader = false;
                int priceColumnIndex = -1;

                for (CSVRecord record : records) {
                    if (!foundSalesPriceHeader) {
                        for (int i = 0; i < record.size(); i++) {
                            if (record.get(i).equalsIgnoreCase("Start price")) {
                                foundSalesPriceHeader = true;
                                priceColumnIndex = i;
                                break;
                            }
                        }
                    } else {
                        if (priceColumnIndex != -1 && record.size() > priceColumnIndex) {
                            String priceString = record.get(priceColumnIndex);
                            if (priceString != null && !priceString.isEmpty()) {
                                try {
                                    double price = Double.parseDouble(priceString.replace("$", "").replace(",", ""));
                                    prices.add(price);
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid price format: " + priceString);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load prices from CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return prices;
    }

    //get the year distribution chart panel
    @SuppressWarnings({ "unused", "unchecked" })
    public ChartPanel getYearDistributionChartPanel() {
        List<String> saleDates = loadSaleDates();

        Map<String, Integer> yearOccurrences = new HashMap<>();
        String[] years = {"2022", "2023", "2024", "2025"};
        for (String year : years) {
            yearOccurrences.put(year, 0);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yy HH:mm:ss z");
        for (String saleDate : saleDates) {
            try {
                LocalDate date = LocalDate.parse(saleDate.split(" ")[0], DateTimeFormatter.ofPattern("MMM-dd-yy"));
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
            true,
            true,
            false
        );
        chart.addSubtitle(new TextTitle("Since: 2022"));
        chart.addSubtitle(new TextTitle("More than: 4000 orders"));

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        return new ChartPanel(chart);
    }

    //show statistics in the GUI
    private void showStatistics() {
        List<Double> prices = loadPricesFromCSV();
        _31_StatsCalc.calculateAndDisplayStatistics(prices, frame);
    }

    //show a comparison chart
    @SuppressWarnings("unused")
    private void showCompareChart() {
        ChartPanel chartPanel1 = getYearDistributionChartPanel();
        ChartPanel chartPanel2 = getBrandDistributionChartPanel();

        JFrame compareFrame = new JFrame("Compare Charts");
        compareFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        compareFrame.setSize(2000, 1600);
        compareFrame.setLayout(new GridLayout(2, 1));

        compareFrame.add(chartPanel1);
        compareFrame.add(chartPanel2);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            compareFrame.dispose();
            frame.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        compareFrame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(false);
        compareFrame.setVisible(true);
    }

    //get the price range distribution chart panel
    @SuppressWarnings("unchecked")
    public ChartPanel getPriceRangeDistributionChartPanel() {
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
            true,
            true,
            false
        );
        chart.addSubtitle(new TextTitle("Since: 2022"));
        chart.addSubtitle(new TextTitle("More than: 4000 orders"));

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        return new ChartPanel(chart);
    }

    //get the category distribution chart panel
    @SuppressWarnings("unchecked")
    public ChartPanel getCategoryDistributionChartPanel() {
        Map<String, Integer> categoryOccurrences = loadCategoryOccurrences();

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : categoryOccurrences.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Category Distribution",
            dataset,
            true,
            true,
            false
        );

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        return new ChartPanel(chart);
    }

    //get the brand distribution chart panel
    @SuppressWarnings("unchecked")
    public ChartPanel getBrandDistributionChartPanel() {
        Map<String, Integer> brandOccurrences = calculateBrandOccurrences();

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : brandOccurrences.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Brand Distribution",
            dataset,
            true,
            true,
            false
        );
        chart.addSubtitle(new TextTitle("Since: 2022"));
        chart.addSubtitle(new TextTitle("More than: 4000 orders"));

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        return new ChartPanel(chart);
    }

    //show category data chart
    @SuppressWarnings("unchecked")
    public void showCategoryDataChart() {
        Map<String, Integer> categoryOccurrences = loadCategoryOccurrences();

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : categoryOccurrences.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Category Data Distribution",
            dataset,
            true,
            true,
            false
        );

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(2000, 1600));
        JFrame chartFrame = new JFrame("Category Data Distribution");
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

    //get the file path in the application data folder
    private String getAppDataFilePath(String filename) {
        return APP_DATA_FOLDER_PATH + "/" + filename;
    }

    //ensure the application data folder exists
    private void ensureAppDataFolderExists() {
        File appDataFolder = new File(APP_DATA_FOLDER_PATH);
        if (!appDataFolder.exists()) {
            appDataFolder.mkdirs();
        }
    }

    //apply the saved theme to the GUI
    public void applySavedTheme() {
        String selectedThemeClassName = _00_StartingGUI.loadSelectedTheme();
        try {
            UIManager.setLookAndFeel(selectedThemeClassName);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to default theme if loading fails
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

