package com.example;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class _36_ButtonRendererEditor extends DefaultCellEditor implements TableCellRenderer {
    protected JButton button;
    private String label;
    private boolean clicked;
    private JTable table;
    private JFrame parentFrame;

    //Constructor to initialize the editor and renderer
    public _36_ButtonRendererEditor(JCheckBox checkBox, JTable table, JFrame parentFrame) {
        super(checkBox);
        this.table = table;
        this.parentFrame = parentFrame;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    //Returns the component used for drawing the cell
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        button.setText((value == null) ? "View Details" : value.toString());
        return button;
    }

    //Returns the component for editing the cell
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "View Details" : value.toString();
        button.setText(label);
        clicked = true;
        return button;
    }

    //Returns the value in the editor
    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            String brand = table.getValueAt(table.getSelectedRow(), 0).toString();
            showBrandDetails(brand);
        }
        clicked = false;
        return label;
    }

    //Stops cell editing
    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    //Ensures the editing process is properly stopped
    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    //Displays a new frame with detailed information for the selected brand
    private void showBrandDetails(String brand) {
        JFrame brandDetailsFrame = new JFrame("Details for " + brand);
        brandDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int frameWidth = 1280;
        brandDetailsFrame.setSize(frameWidth, 600);
        brandDetailsFrame.setLayout(new BorderLayout());

        DefaultTableModel detailsTableModel = new DefaultTableModel();
        JTable detailsTable = new JTable(detailsTableModel);
        detailsTableModel.addColumn("Item Number");
        detailsTableModel.addColumn("Category");
        detailsTableModel.addColumn("Brand");
        detailsTableModel.addColumn("Start Date");
        detailsTableModel.addColumn("Sale Date");
        detailsTableModel.addColumn("Turnover Time");
        detailsTableModel.addColumn("Title");

        Map<String, String> itemTitlesMap = loadItemTitles(); //Load titles from 2ndCsvFile

        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("Turnover.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[2].equalsIgnoreCase(brand)) {
                    String itemNumber = parts[0];
                    String title = itemTitlesMap.getOrDefault(itemNumber, "Title Not Found");

                    detailsTableModel.addRow(new Object[]{
                        itemNumber, //Item Number
                        parts[1],   //Category
                        parts[2],   //Brand
                        parts[5],   //Start Date
                        parts[4],   //Sale Date
                        parts[6],   //Turnover Time
                        title       //Title from 2ndCsvFile.txt
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load brand details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        //Adjust column widths
        detailsTable.getColumnModel().getColumn(0).setPreferredWidth(100);  //Item Number
        detailsTable.getColumnModel().getColumn(1).setPreferredWidth(100);  //Category
        detailsTable.getColumnModel().getColumn(2).setPreferredWidth(100); //Brand
        detailsTable.getColumnModel().getColumn(3).setPreferredWidth(100); //Start Date
        detailsTable.getColumnModel().getColumn(4).setPreferredWidth(78); //Sale Date
        detailsTable.getColumnModel().getColumn(5).setPreferredWidth(100); //Turnover Time
        detailsTable.getColumnModel().getColumn(6).setPreferredWidth(602);

        JScrollPane scrollPane = new JScrollPane(detailsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Details for Brand: " + brand));
        brandDetailsFrame.add(scrollPane, BorderLayout.CENTER);

        //Add a back button at the bottom
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> brandDetailsFrame.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        brandDetailsFrame.add(buttonPanel, BorderLayout.SOUTH);

        brandDetailsFrame.setLocationRelativeTo(parentFrame);
        brandDetailsFrame.setVisible(true);
    }

    //Loads item titles from the 2ndCsvFile.txt
    private Map<String, String> loadItemTitles() {
        Map<String, String> itemTitlesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAppDataFilePath("2ndCsvFile.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String itemNumber = parts[2];  //Item Number
                    String title = parts[5];       //Title
                    itemTitlesMap.put(itemNumber, title);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, "Failed to load item titles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return itemTitlesMap;
    }

    //Returns the full path to a file in the application data folder
    private String getAppDataFilePath(String filename) {
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }
}
