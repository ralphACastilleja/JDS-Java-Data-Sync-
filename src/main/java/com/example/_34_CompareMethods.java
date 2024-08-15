package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class _34_CompareMethods {

    public static ChartPanel getYearDistributionChartPanel(List<String> saleDates) {
        Map<String, Integer> yearOccurrences = new HashMap<>();
        String[] years = {"2022", "2023", "2024", "2025"};
        for (String year : years) {
            yearOccurrences.put(year, 0);
        }

        for (String saleDate : saleDates) {
            try {
                String year = saleDate.split("-")[2];
                yearOccurrences.put(year, yearOccurrences.get(year) + 1);
            } catch (Exception e) {
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

    public static ChartPanel getPriceRangeDistributionChartPanel(List<Double> prices) {
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

    public static ChartPanel getBrandDistributionChartPanel(Map<String, Integer> brandOccurrences) {
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

    public static ChartPanel getCategoryDistributionChartPanel(Map<String, Integer> categoryOccurrences) {
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


 

    public static void showChartWithCompareButton(ChartPanel chartPanel1, ChartPanel chartPanel2, JFrame mainFrame) {
        JFrame chartFrame = new JFrame("Chart with Compare Option");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setSize(900, 900);
        chartFrame.setLayout(new BorderLayout());
    
        chartFrame.add(chartPanel1, BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            chartFrame.dispose();
            mainFrame.setVisible(false);
        });
    
        JButton compareButton = new JButton("Compare Chart");
        compareButton.addActionListener(e -> {
            JPanel comparePanel = new JPanel(new GridLayout(1, 2)); 
            comparePanel.add(chartPanel1);
            comparePanel.add(chartPanel2);
    
            chartFrame.getContentPane().removeAll(); 
            chartFrame.add(comparePanel, BorderLayout.CENTER); 
            chartFrame.revalidate(); 
            chartFrame.repaint(); 
        });
    
        buttonPanel.add(backButton);
        buttonPanel.add(compareButton);
    
        chartFrame.add(buttonPanel, BorderLayout.SOUTH);
    
        chartFrame.pack();
        chartFrame.setVisible(true);
    }





    
    
}    