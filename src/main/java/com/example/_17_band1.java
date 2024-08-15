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
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.prefs.Preferences;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.util.Iterator;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
public class _17_band1 {
 private static  String PREF_CSV_PATH = "csvFilePath";
 private static final String AVERAGE_TURNOVER_FILE = "2ndCsvFileE.txt";
 private static final String TURNOVER_FILE = "TurnoverE.txt";
 private static final String APP_DATA_FOLDER_PATH = System.getProperty("user.home") + "/Downloads/JDSAppData";
 private JFrame frame;
 private Map<String, List<Double>> brandPrices;
 private Map<String, Integer> brandOccurrences;
 private List<String> brands;
 private Preferences prefs;
 private Map<String, List<String>> brandItemNumbers;
 private Map<String, List<String>> brandSaleDates;
 private Map<String, List<String>> brandCategories;
 public static void createEbayGUI(JFrame parentFrame) {
     new _17_band1(parentFrame);
 }
 static {
     try {
FlatLaf.registerCustomDefaultsSource("com/example");
         UIManager.setLookAndFeel(new FlatDarkLaf());
         FlatDarkLaf.setup();
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
 public _17_band1() {
     this(null);
 }
public _17_band1(JFrame parentFrame) {
 prefs = Preferences.userRoot().node(this.getClass().getName());
 frame = new JFrame("band Data");
frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(800, 600);
 frame.setLayout(new BorderLayout());
     URL iconUrl = getClass().getResource("/JDS.png");
     if (iconUrl != null) {
         ImageIcon JDS = new ImageIcon(iconUrl);
         frame.setIconImage(JDS.getImage());
         System.out.println("Icon loaded successfully");
     } else {
         System.err.println("Icon resource not found: /JDS.png");
     }
 frame.setLocationRelativeTo(parentFrame);
 brandPrices = new HashMap<>();
 brandOccurrences = new HashMap<>();
 brandItemNumbers = new HashMap<>();
 brandSaleDates = new HashMap<>();
 brandCategories = new HashMap<>();
 initializeComponents();
 frame.setVisible(true);
  String csvPath = _08_TunT.getCSVPath();
  if (csvPath != null) {
      System.out.println("Processing file from preferences: " + csvPath);
      processCSVFile(csvPath, false);
  } else {
      System.out.println("No CSV file path found in preferences.");
  }
  Map<String, Double> storedTurnoverTimes = loadAverageTurnoverTimes();
calculateAndDisplayAverages(storedTurnoverTimes);
}
 private void initializeComponents() {
     brands = _09_BrandList.getBrands();
     JPanel buttonPanel = new JPanel();
     frame.add(buttonPanel, BorderLayout.SOUTH);
 
     JButton infoButton = new JButton("Info");
     infoButton.addActionListener(e -> showInfoDialog());
     buttonPanel.add(infoButton);
     JButton backButton = new JButton("Back");
backButton.addActionListener(e -> frame.dispose());
     buttonPanel.add(backButton);
  
    /* JButton averagePriceButton = new JButton("Average Price");
     averagePriceButton.addActionListener(e -> showAveragePriceView());
     buttonPanel.add(averagePriceButton);
     JButton updateAveragePriceDataButton = new JButton("Update Average Price Data");
     updateAveragePriceDataButton.addActionListener(e -> updateAveragePriceData());
     buttonPanel.add(updateAveragePriceDataButton); */
}
 public void processCSVFromPrefs() {
     String csvPath = getCSVPath();
     if (csvPath != null) {
         System.out.println("Processing file: " + csvPath);
         processCSVFile(csvPath, false);
     } else {
         System.out.println("No file path found in preferences.");
     }
 }
   
 public void processCSVFile(String filePath, boolean isUpdate) {
    System.out.println("DEBUG: Processing CSV file: " + filePath);
    Map<String, List<Double>> existingPrices = loadAveragePrices();
    Map<String, List<Double>> brandPrices = new HashMap<>(existingPrices);
    Map<String, List<String>> brandItemNumbers = new HashMap<>();
    Map<String, List<String>> brandSaleDates = new HashMap<>();
    Map<String, List<String>> brandCategories = new HashMap<>();
    Map<String, Integer> brandOccurrences = new HashMap<>();
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
        CSVParser parser = new CSVParser(reader, csvFormat);
        List<String> headers = parser.getHeaderNames();
        System.out.println("DEBUG: Headers found: " + headers);
        Map<String, Integer> headerIndices = findHeaderIndices(headers);
        itemTitleColumnIndex = headerIndices.getOrDefault("itemTitle", -1);
        soldForColumnIndex = headerIndices.getOrDefault("soldFor", -1);
        itemNumberColumnIndex = headerIndices.getOrDefault("itemNumber", -1);
        saleDateIndex = headerIndices.getOrDefault("saleDate", -1);
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
                System.out.println("DEBUG: Item Title: " + itemTitle);
                System.out.println("DEBUG: Sold For: " + soldForString);
                System.out.println("DEBUG: Item Number: " + itemNumber);
                System.out.println("DEBUG: Sale Date: " + saleDate);
                if (!itemTitle.isEmpty() && !soldForString.isEmpty() && !processedItemNumbers.contains(itemNumber)) {
processedItemNumbers.add(itemNumber);
                    try {
                        double price = Double.parseDouble(soldForString.replace("$", "").replace(",", ""));
                        for (String brand : brands) {
                            if (itemTitle.toLowerCase().contains(brand.toLowerCase())) {
                                brandPrices.computeIfAbsent(brand, k -> new ArrayList<>()).add(price);
                                brandItemNumbers.computeIfAbsent(brand, k -> new ArrayList<>()).add(itemNumber);
                                brandSaleDates.computeIfAbsent(brand, k -> new ArrayList<>()).add(saleDate);
                                boolean categoryFound = false;
                                for (String category : _09_BrandList.getCategories()) {
                                    String categoryPattern = "\\b" + category.toLowerCase() + "\\b";
                                    if (itemTitle.toLowerCase().matches(".*" + categoryPattern + ".*")) {
                                        brandCategories.computeIfAbsent(brand, k -> new ArrayList<>()).add(category);
                                        categoryFound = true;
System.out.println("DEBUG: Added category: " + category + " for brand: " + brand);
                                        break;
                                    }
                                }
                                if (!categoryFound) {
                                    brandCategories.computeIfAbsent(brand, k -> new ArrayList<>()).add("Unknown");
System.out.println("DEBUG: Added category: Unknown for brand: " + brand);
                                }
                                brandOccurrences.put(brand, brandOccurrences.getOrDefault(brand, 0) + 1);
                                System.out.println("DEBUG: Added price: " + price + " for brand: " + brand);
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("DEBUG: Skipping invalid price: " + soldForString);
                    }
                }
            }
        }
        if (!isUpdate && brandCategories.values().stream().flatMap(List::stream).anyMatch(category -> category.equalsIgnoreCase("band"))) {
saveItemData(brandPrices, brandItemNumbers, brandSaleDates, brandCategories);
        }
calculateAndDisplayAverages(loadAverageTurnoverTimes());
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Failed to process CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
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
    System.out.println("DEBUG: Header indices found: " + headerIndices);
    return headerIndices;
}
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
        System.out.println("DEBUG: Row " + rowCount + ": " + record);
        for (int i = 0; i < record.size(); i++) {
            String cellValue = record.get(i).trim().toLowerCase();
            System.out.println("DEBUG: Header found: " + cellValue + " at index " + i);
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
    System.out.println("DEBUG: Final header indices: " + headerIndices);
    return headerIndices;
}
 private void calculateAndDisplayAverages(Map<String, Double> averageTurnoverTimes) {
     Map<String, Integer> brandOccurrences = calculateBrandOccurrences();
     displayBrandAverages(averageTurnoverTimes, brandOccurrences);
 }
 private Map<String, Integer> calculateBrandOccurrences() {
   Map<String, Integer> brandOccurrences = new HashMap<>();
   try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("TurnoverE.txt")))) {
       String line;
       while ((line = reader.readLine()) != null) {
           String[] parts = line.split(",");
if (parts.length >= 3) {
               String brand = parts[2].trim();
System.out.println("Found brand: " + brand);
               brandOccurrences.put(brand, brandOccurrences.getOrDefault(brand, 0) + 1);
           }
       }
   } catch (IOException e) {
       e.printStackTrace();
       JOptionPane.showMessageDialog(frame, "Failed to load brand occurrences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
   }
System.out.println("Brand occurrences: " + brandOccurrences);
   return brandOccurrences;
}
 /*
 private Map<String, Integer> calculateBrandOccurrences() {
     Map<String, Integer> brandOccurrences = new HashMap<>();
     try (BufferedReader reader = new BufferedReader(new FileReader(TURNOVER_FILE))) {
         String line;
         boolean inAverageSection = false;
         while ((line = reader.readLine()) != null) {
             if (line.startsWith("Average Turnover Times per Brand:")) {
                 inAverageSection = true;
                 continue;
             }
             if (!inAverageSection) {
                 String[] parts = line.split(",");
                 if (parts.length >= 3) {
                     String brand = parts[2];
                     brandOccurrences.put(brand, brandOccurrences.getOrDefault(brand, 0) + 1);
                 }
             }
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
     return brandOccurrences;
 } */
  private void displayBrandAverages(Map<String, Double> averageTurnoverTimes, Map<String, Integer> brandOccurrences) {
     JPanel resultPanel = new JPanel(new BorderLayout());
     DefaultTableModel tableModel = new DefaultTableModel();
     JTable table = new JTable(tableModel);
     tableModel.addColumn("Brand");
     tableModel.addColumn("Average Turnover Time (days)");
     tableModel.addColumn("Occurrences");
     for (Map.Entry<String, Double> entry : averageTurnoverTimes.entrySet()) {
         String brand = entry.getKey();
         double averageTurnoverTime = entry.getValue();
         int occurrences = brandOccurrences.getOrDefault(brand, 0);
         tableModel.addRow(new Object[]{brand, String.format("%.2f", averageTurnoverTime), occurrences});
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
     scrollPane.setBorder(BorderFactory.createTitledBorder("Brands, Average Turnover Times, Occurrences"));
     resultPanel.add(scrollPane, BorderLayout.CENTER);
     frame.add(resultPanel, BorderLayout.CENTER);
     frame.revalidate();
     frame.repaint();
 }
 private Map<String, Double> loadAverageTurnoverTimes() {
  Map<String, Double> averageTurnoverTimes = new HashMap<>();
  try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("TurnoverE.txt")))) {
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
 private void showTurnoverView() {
     new _06_EbayDataGUI(frame);
     frame.setVisible(false);
 }
 private void showAveragePriceView() {
     JFrame avgPriceFrame = new JFrame("Average Price View");
     avgPriceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     avgPriceFrame.setSize(800, 600);
     JPanel buttonPanel = new JPanel();
     avgPriceFrame.add(buttonPanel, BorderLayout.SOUTH);
     JButton chooseFileButton = new JButton("Choose CSV File");
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
private void updateAveragePriceData() {
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setDialogTitle("Select CSV File");
 int result = fileChooser.showOpenDialog(frame);
 if (result == JFileChooser.APPROVE_OPTION) {
     File selectedFile = fileChooser.getSelectedFile();
     prefs.put(PREF_CSV_PATH, selectedFile.getAbsolutePath());
processCSVFile(selectedFile.getAbsolutePath(), true);
saveItemData(brandPrices, brandItemNumbers, brandSaleDates, brandCategories);
Map<String, Double> storedTurnoverTimes = loadAverageTurnoverTimes();
calculateAndDisplayAverages(storedTurnoverTimes);
     JOptionPane.showMessageDialog(frame, "Average Price Data Updated.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
 }
}
private Map<String, List<Double>> loadAveragePrices() {
  Map<String, List<Double>> averagePrices = new HashMap<>();
  try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFileE.txt")))) {
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
 private void showInfoDialog() {
     String infoMessage = "The turnover Data of the chosen category";
     JTextArea textArea = new JTextArea(infoMessage);
     textArea.setWrapStyleWord(true);
     textArea.setLineWrap(true);
     textArea.setCaretPosition(0);
     textArea.setEditable(false);
     JScrollPane scrollPane = new JScrollPane(textArea);
     scrollPane.setPreferredSize(new Dimension(500, 300));
     JOptionPane.showMessageDialog(frame, scrollPane, "Info", JOptionPane.INFORMATION_MESSAGE);
 }
 private void refreshGUI() {
frame.getContentPane().removeAll();
initializeComponents();
frame.revalidate();
frame.repaint();
Map<String, Double> storedTurnoverTimes = loadAverageTurnoverTimes();
calculateAndDisplayAverages(storedTurnoverTimes);
 }
 private void saveItemData(Map<String, List<Double>> brandPrices, Map<String, List<String>> brandItemNumbers, Map<String, List<String>> brandSaleDates, Map<String, List<String>> brandCategories) {
  Set<String> processedItemNumbers = new HashSet<>();
  try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAppDataFilePath("2ndCsvFileE.txt")))) {
      for (Map.Entry<String, List<Double>> entry : brandPrices.entrySet()) {
          String brand = entry.getKey();
          List<Double> prices = entry.getValue();
          List<String> itemNumbers = brandItemNumbers.get(brand);
          List<String> saleDates = brandSaleDates.get(brand);
          List<String> categories = brandCategories.get(brand);
          System.out.println("Processing brand: " + brand);
          if (itemNumbers == null || saleDates == null || categories == null) {
              System.out.println("No item numbers, sale dates or categories for brand: " + brand);
              continue;
          }
          System.out.println("Prices size: " + prices.size());
          System.out.println("Item numbers size: " + itemNumbers.size());
          System.out.println("Sale dates size: " + saleDates.size());
          System.out.println("Categories size: " + categories.size());
          for (int i = 0; i < prices.size(); i++) {
              if (i >= itemNumbers.size() || i >= saleDates.size() || i >= categories.size()) {
                  System.out.println("Skipping index " + i + " due to mismatch in sizes");
                  continue;
              }
              String itemNumber = itemNumbers.get(i);
              String saleDate = saleDates.get(i);
              String category = categories.get(i);
            
              if ("band".equalsIgnoreCase(category) && !processedItemNumbers.contains(itemNumber)) {
                  writer.write(brand + "," + prices.get(i) + "," + itemNumber + "," + saleDate + "," + category);
                  writer.newLine();
processedItemNumbers.add(itemNumber);
                  System.out.println("Stored: " + brand + ", " + prices.get(i) + ", " + itemNumber + ", " + saleDate + ", " + category);
              }
          }
      }
  } catch (IOException e) {
      e.printStackTrace();
  }
 }
 private void deleteStoredTurnovereData() {
ensureAppDataFolderExists();
  String[] filenames = {
      "2ndCsvFile.txt",
      "2ndCsvFileE.txt",
      "2ndCsvFileC.txt",
      "2ndCsvFileD.txt",
      "Turnover.txt",
      "TurnoverE.txt",
      "TurnoverC.txt",
      "TurnoverD.txt",
      "average_prices.txt"
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
}
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
 private void displayDataForCategory(String category) {
     if ("band".equalsIgnoreCase(category)) {
         _17_band1.createEbayGUI(frame);
        
/*
     } else if (("pocket watch".equalsIgnoreCase(category))){
         _13_pocketWCAT1.createEbayGUI(frame);
     } else if (("band Movement".equalsIgnoreCase(category))){
         _15_WWMovementCAT1.createEbayGUI(frame);
     } else if (("pocket watch movement".equalsIgnoreCase(category))){
         _17_PWMovementCAT1.createEbayGUI(frame);
     } else if (("band".equalsIgnoreCase(category))){
         _19_BandCAT1.createEbayGUI(frame);
     } else if (("pocket watch case".equalsIgnoreCase(category))){
         _21_PWCaseCAT1.createEbayGUI(frame);
     } else if (("parts".equalsIgnoreCase(category))){
         _23_partsCAT1.createEbayGUI(frame);
     } else if (("Collet".equalsIgnoreCase(category))){
         _25_ColletCAT1.createEbayGUI(frame);
     } else if (("Balance assembly".equalsIgnoreCase(category))){
         _27_BalanceACAT1.createEbayGUI(frame);
     } else if (("Watch Movement Holder".equalsIgnoreCase(category))){
         _29_WMholderCAT1.createEbayGUI(frame);
         */
  }else {JOptionPane.showMessageDialog(frame, "Selected category is not band: " + category);
  
 }}
 public static String getCSVPath() {
     Preferences prefs = Preferences.userRoot().node(_17_band1.class.getName());
     return prefs.get(PREF_CSV_PATH, null);
 }
 private List<String> loadSaleDates() {
  List<String> saleDates = new ArrayList<>();
  try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFileE.txt")))) {
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
private Map<String, Integer> loadCategoryOccurrences() {
  Map<String, Integer> categoryOccurrences = new HashMap<>();
  try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFileE.txt")))) {
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
 public void showYearDistributionChart() {
     List<String> saleDates = loadSaleDates();
     Map<String, Integer> yearOccurrences = new HashMap<>();
     String[] years = {"2022", "2023", "2024" , "2025"};
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
     chart.addSubtitle(new TextTitle("More than: 4000, orders"));
      PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
     plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
     plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
      ChartPanel chartPanel = new ChartPanel(chart);
     chartPanel.setPreferredSize(new Dimension(2000, 1600));
     JFrame chartFrame = new JFrame(" Listing Orders Year Distribution");
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
chart.addSubtitle(new TextTitle("More than: 4000, orders"));
      PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
     plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
     plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
      ChartPanel chartPanel = new ChartPanel(chart);
     chartPanel.setPreferredSize(new Dimension(2000, 1600));
     JFrame chartFrame = new JFrame(" Listing Orders Brand Distribution");
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
  private List<Double> loadPrices() {
     List<Double> prices = new ArrayList<>();
     try (BufferedReader reader = new BufferedReader(new FileReader(AVERAGE_TURNOVER_FILE))) {
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
chart.addSubtitle(new TextTitle("More than: 4000, orders"));
 PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
 plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
 plot.setSectionPaint("Section", Color.GREEN);
 plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
 ChartPanel chartPanel = new ChartPanel(chart);
 chartPanel.setPreferredSize(new Dimension(2000, 1600));
 JFrame chartFrame = new JFrame("Listing Orders Price Range Distribution");
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
 plot.setSectionPaint("Section", Color.GREEN);
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
private void showStatistics() {
 List<Double> prices = loadPricesFromCSV();
 _31_StatsCalc.calculateAndDisplayStatistics(prices, frame);
}
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
public ChartPanel getBrandDistributionChartPanel() {
 Map<String, Integer> brandOccurrences = new HashMap<>();
 try (BufferedReader reader = new BufferedReader(new FileReader(AVERAGE_TURNOVER_FILE))) {
     String line;
     while ((line = reader.readLine()) != null) {
         String[] parts = line.split(",");
if (parts.length >= 1) {
             String brand = parts[0];
             brandOccurrences.put(brand, brandOccurrences.getOrDefault(brand, 0) + 1);
         }
     }
 } catch (IOException e) {
     e.printStackTrace();
     JOptionPane.showMessageDialog(frame, "Failed to load brand occurrences: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
 }
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
private String getAppDataFilePath(String filename) {
  return APP_DATA_FOLDER_PATH + "/" + filename;
}
private void ensureAppDataFolderExists() {
  File appDataFolder = new File(APP_DATA_FOLDER_PATH);
  if (!appDataFolder.exists()) {
      appDataFolder.mkdirs();
  }
}
}
