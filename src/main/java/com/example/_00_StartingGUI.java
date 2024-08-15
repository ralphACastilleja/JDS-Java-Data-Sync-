package com.example;


import org.openqa.selenium.SessionNotCreatedException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;


public class _00_StartingGUI {


    private JFrame frame;
    private List<String> brands;
    private float opacity = 0.0f;
    private Properties credentials;
    private JCheckBox headlessCheckBox;
    private boolean isDeveloper;


    public _00_StartingGUI() { //Constructor method
        frame = new JFrame("Main Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());
        loadAndApplyTheme();


        URL iconUrl = getClass().getClassLoader().getResource("SNS-6.png");
        if (iconUrl != null) {
            ImageIcon JDS = new ImageIcon(iconUrl);
            frame.setIconImage(JDS.getImage());
            System.out.println("Icon loaded successfully");
        } else {
            System.err.println("Icon resource not found: JDS.png");
        }


        frame.setLocationRelativeTo(null);
        brands = _09_BrandList.getBrands();


        JLabel gifLabel = new JLabel();
        URL gifUrl = getClass().getClassLoader().getResource("JDS.gif");
        if (gifUrl != null) {
            ImageIcon icon = new ImageIcon(gifUrl);
            gifLabel.setIcon(icon);
        } else {
            System.err.println("GIF resource not found: JDS.gif");
        }
        gifLabel.setPreferredSize(new Dimension(500, 300));


        headlessCheckBox = new JCheckBox("Run in automated mode(keep this)");
        headlessCheckBox.setFont(headlessCheckBox.getFont().deriveFont(headlessCheckBox.getFont().getSize() * 1.2f));
        headlessCheckBox.setSelected(true);


        JButton startAppButton = new JButton("Sort Google sheets data");
        startAppButton.setPreferredSize(new Dimension(250, 50));
        startAppButton.setFont(startAppButton.getFont().deriveFont(startAppButton.getFont().getSize() * 1.4f));
        startAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });


        JButton themeButton = new JButton("Theme");
        themeButton.setFont(themeButton.getFont().deriveFont(themeButton.getFont().getSize() * 1.4f));
        themeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showThemeSelectionDialog();
            }
        });


        JButton startEbayApiButton = new JButton("Sort eBay data");
        startEbayApiButton.setPreferredSize(new Dimension(400, 50));
        startEbayApiButton.setFont(startEbayApiButton.getFont().deriveFont(startEbayApiButton.getFont().getSize() * 1.4f));
        startEbayApiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                String selectedCategory = "DefaultCategory";
                _06_EbayDataGUI.createEbayGUI(frame, selectedCategory);
            }
        });


        JButton infoButton = new JButton("Info");
        infoButton.setFont(infoButton.getFont().deriveFont(infoButton.getFont().getSize() * 1.4f));
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInfoDialog();
            }
        });


        JButton refreshButton = new JButton("Automatically Refresh Completed Orders and Listings");
        refreshButton.setPreferredSize(new Dimension(600, 50));
        refreshButton.setFont(refreshButton.getFont().deriveFont(refreshButton.getFont().getSize() * 1.4f));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRefreshDialog();
            }
        });


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.PAGE_END;
        frame.add(gifLabel, gbc);


        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(startEbayApiButton, gbc);
        gbc.gridwidth = 0;


        gbc.gridx = 5;
        gbc.gridy = 2;
        frame.add(infoButton, gbc);


        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(infoButton, gbc);


        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 0;
        frame.add(themeButton, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(refreshButton, gbc);


        frame.setVisible(true);
        startFadeInAnimation(gifLabel);
    }


    private void startFadeInAnimation(JLabel gifLabel) { //Method to start fade-in animation
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity > 1.0f) {
                    opacity = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                gifLabel.repaint();
            }
        });
        timer.start();
    }


    private void showThemeSelectionDialog() { //Method to show theme selection dialog
        UIManager.LookAndFeelInfo[] themes = {
            new UIManager.LookAndFeelInfo("Flat Dark", FlatDarkLaf.class.getName()),
            new UIManager.LookAndFeelInfo("Flat Light", FlatLightLaf.class.getName()),
            new UIManager.LookAndFeelInfo("Flat IntelliJ", FlatIntelliJLaf.class.getName()),
            new UIManager.LookAndFeelInfo("Flat Darcula", FlatDarculaLaf.class.getName())
        };


        String[] themeNames = new String[themes.length];
        for (int i = 0; i < themes.length; i++) {
            themeNames[i] = themes[i].getName();
        }


        String selectedTheme = (String) JOptionPane.showInputDialog(
            frame,
            "Choose a theme:",
            "Theme Selector",
            JOptionPane.PLAIN_MESSAGE,
            null,
            themeNames,
            themeNames[0]
        );


        if (selectedTheme != null) {
            try {
                for (UIManager.LookAndFeelInfo theme : themes) {
                    if (theme.getName().equals(selectedTheme)) {
                        UIManager.setLookAndFeel(theme.getClassName());
                        SwingUtilities.updateComponentTreeUI(frame);
                        saveSelectedTheme(theme.getClassName());
                        frame.revalidate();
                        frame.repaint();
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void showRefreshDialog() { //Method to show refresh dialog
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(true);


        String infoMessage = "This works by creating a Google or Safari script using your credentials, "
                + "then using the browser on your computer to download those files for you and process them.\n\n"
                + "~~~~~~~~~~~~~~~~(Important)~~~~~~~~~~~~~~~~\n"
                + "- If the browser is Safari, do not click on anything while the program is running; this will take 2 minutes.\n"
                + "- Your credentials are safe as they are only stored in a text file in this program. By pressing 'Delete Credentials', all sensitive information will be removed.\n"
                + "- If you get a message asking you to authenticate or prove you're human, complete the task and then restart this program.";


        textArea.setText(infoMessage);


        Object[] options = {"Continue", "Continue for Developer"};
        int result = JOptionPane.showOptionDialog(null, new JScrollPane(textArea), "Refresh Completed Orders and Listings",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);


        if (result == 0) {
            isDeveloper = false;
            showCredentialsInputGUI(false, frame);
        } else if (result == 1) {
            isDeveloper = true;
            showCredentialsInputGUI(true, frame);
        }
    }


    private void showCredentialsInputGUI(boolean isDeveloper, JFrame mainFrame) { //Method to show credentials input GUI
        JFrame credentialsFrame = new JFrame("Enter Crdedentials");
        credentialsFrame.setSize(800, 400);
        credentialsFrame.setLayout(new GridBagLayout());
        credentialsFrame.setLocationRelativeTo(null);


        JLabel headerLabel = new JLabel("Enter eBay Login");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));


        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);


        usernameField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));


        loadCredentials(); //Load stored credentials if they exist
        if (credentials != null) {
            usernameField.setText(credentials.getProperty("username", ""));
            passwordField.setText(credentials.getProperty("password", ""));
        }


        JButton enterButton = new JButton("Enter or Continue");
        JButton deleteButton = new JButton("Delete Credentials");
        JButton backButton = new JButton("Back");


        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCredentials(usernameField.getText(), new String(passwordField.getPassword()));
                credentialsFrame.dispose();
                mainFrame.setVisible(true);


                Thread automationThread = new Thread(() -> {
                    boolean headless = headlessCheckBox.isSelected();
                    if (isDeveloper) {
                        _32_EbayAutomation automation = new _32_EbayAutomation();
                        automation.loadCredentials();
                        try {
                            automation.downloadReports(headless);
                        } catch (SessionNotCreatedException ex) {
                            JOptionPane.showMessageDialog(credentialsFrame, "Failed to initialize. Clicking the button again most of the time works.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(credentialsFrame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    } else {
                        _33_UserEbayAutomation automation = new _33_UserEbayAutomation();
                        automation.loadCredentials();
                        try {
                            automation.downloadReports(headless);
                        } catch (SessionNotCreatedException ex) {
                            JOptionPane.showMessageDialog(credentialsFrame, "Failed to initialize. Clicking the button again most of the time works.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(credentialsFrame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                });


                automationThread.start();
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCredentials();
                usernameField.setText("");
                passwordField.setText("");
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                credentialsFrame.dispose();
                mainFrame.setVisible(true);
            }
        });


        enterButton.setBackground(UIManager.getColor("OptionPane.informationDialog.titlePane.background"));
        enterButton.setForeground(Color.WHITE);
        enterButton.setOpaque(true);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        credentialsFrame.add(headerLabel, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        credentialsFrame.add(backButton, gbc);


        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        credentialsFrame.add(headlessCheckBox, gbc);


        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        credentialsFrame.add(new JLabel("Username:"), gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        credentialsFrame.add(usernameField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        credentialsFrame.add(new JLabel("Password:"), gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        credentialsFrame.add(passwordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        credentialsFrame.add(deleteButton, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        credentialsFrame.add(enterButton, gbc);


        JLabel imageLabel = new JLabel();
        URL imageUrl = getClass().getClassLoader().getResource("EOLD.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            imageLabel.setIcon(icon);
        } else {
            System.err.println("PNG resource not found: JDS-5.png");
        }
        imageLabel.setPreferredSize(new Dimension(400, 300));


        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.BOTH;
        credentialsFrame.add(imageLabel, gbc);


        mainFrame.setVisible(false);
        credentialsFrame.setVisible(true);
    }


    private void loadCredentials() { //Method to load credentials from a properties file
        credentials = new Properties();
        try (FileInputStream in = new FileInputStream("credentials.properties")) {
            credentials.load(in);
        } catch (IOException e) {
            System.err.println("No credentials found.");
        }
    }


    private void saveCredentials(String username, String password) { //Method to save credentials to a properties file
        credentials.setProperty("username", username);
        credentials.setProperty("password", password);
        try (FileOutputStream out = new FileOutputStream("credentials.properties")) {
            credentials.store(out, "User Credentials");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteCredentials() { //Method to delete credentials from the properties file
        java.io.File file = new java.io.File("credentials.properties");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Credentials deleted.");
            } else {
                System.err.println("Failed to delete credentials.");
            }
        }
    }


    private void showInfoDialog() { //Method to show the information dialog
        String infoMessage = "Welcome to the Main Application! This program helps you to efficiently manage and sort data from Google Sheets and eBay. "
                + "Here's a brief overview of the functionalities:\n\n"
                + "1. **Sort Google Sheets Data**: This feature allows you to enter a Google Sheets link and sort the data based on predefined criteria. "
                + "You can view the sorted data in an organized manner.\n"
                + "2. **Sort eBay Data**: This feature enables you to process eBay data. You can sort and analyze the data from eBay based on various categories.\n"
                + "3. **Automatically Refresh Completed Orders and Listings**: This feature automates the process of refreshing completed orders and listings. "
                + "It uses your credentials to create scripts and automate data processing through your browser.\n\n"
                + "Additional Features:\n"
                + "For any issues ask me!";


        JTextArea textArea = new JTextArea(10, 40);
        textArea.setText(infoMessage);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(true);


        int result = JOptionPane.showConfirmDialog(null, new JScrollPane(textArea), "Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            infoMessage = textArea.getText();
        }
    }


    private void saveSelectedTheme(String themeClassName) { //Method to save the selected theme to a properties file
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


    private static String getAppDataFilePath(String filename) { //Method to get the file path for app data
        String appDataPath = System.getProperty("user.home") + "/Downloads/JDSAppData";
        return appDataPath + "/" + filename;
    }


    private void loadAndApplyTheme() { //Method to load and apply the selected theme
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


    public void applyTheme(String themeClassName) { //Method to apply the selected theme and update the UI
        try {
            UIManager.setLookAndFeel(themeClassName);
            FlatLaf.updateUI();
            saveSelectedTheme(themeClassName);


            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) { //Main method to launch the application
        String selectedThemeClassName = loadSelectedTheme();
        try {
            UIManager.setLookAndFeel(selectedThemeClassName);
            FlatLaf.setup(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        SwingUtilities.invokeLater(() -> new _00_StartingGUI());
    }


    public static String loadSelectedTheme() { //Method to load the selected theme from the properties file
        Properties themeProperties = new Properties();
        try (FileInputStream in = new FileInputStream(getAppDataFilePath("settings.properties"))) {
            themeProperties.load(in);
            return themeProperties.getProperty("selectedTheme", FlatDarkLaf.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return FlatDarkLaf.class.getName();
        }
    }
}
