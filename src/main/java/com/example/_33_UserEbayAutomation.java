package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;






//USER OPTION, this class is very similar to the 32 class but it was personally optomized 
//for my client 


public class _33_UserEbayAutomation {

    private static final String EBAY_URL = "https://www.ebay.com/";        // Load credentials and start automation

    private String username;
    private String password;

    public static void main(String[] args) {
        File credentialsFile = new File("credentials.properties");
        if (credentialsFile.exists()) {
            System.out.println("credentials.properties file found.");
            try (FileInputStream in = new FileInputStream(credentialsFile)) {
                Properties testProperties = new Properties();
                testProperties.load(in);
                System.out.println("Contents of credentials.properties:");
                testProperties.forEach((key, value) -> System.out.println(key + ": " + value));
            } catch (IOException e) {
                System.out.println("Error reading credentials.properties file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("credentials.properties file not found.");
        }

        _33_UserEbayAutomation automation = new _33_UserEbayAutomation();
        automation.loadCredentials();
        automation.downloadReports(false); 
    }

    public void loadCredentials() {
        Properties credentials = new Properties();
        try (FileInputStream in = new FileInputStream("credentials.properties")) {
            credentials.load(in);
            this.username = credentials.getProperty("username");
            this.password = credentials.getProperty("password");

            System.out.println("Loaded credentials: username = " + this.username + ", password = " + this.password);
        } catch (IOException e) {
            System.out.println("Error loading credentials from file.");
            e.printStackTrace();
        }
    }

    public void downloadReports(boolean headless) {
        String userHome = System.getProperty("user.home");
        String chromeDriverPath = userHome + "/chromedriver";
        String downloadFilepath = userHome + "/Downloads";

        WebDriver driver = null;

        try {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", downloadFilepath);
            options.setExperimentalOption("prefs", prefs);

            options.addArguments("start-maximized");
            options.addArguments("disable-infobars");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--remote-allow-origins=*");

            if (headless) {
                options.addArguments("--headless");  // Run in headless mode
            }

            driver = new ChromeDriver(options);

        } catch (Exception e) {
            System.out.println("ChromeDriver not found or failed to initialize. Trying SafariDriver...");

            try {
                driver = new SafariDriver();
            } catch (Exception ex) {
                System.out.println("SafariDriver not found or failed to initialize. Exiting...");
                ex.printStackTrace();
                
                JOptionPane.showMessageDialog(null,
                        "SafariDriver not found or failed to initialize.\n"
                        + "You must enable the 'Allow Remote Automation' option in Safari's Develop menu to control Safari via WebDriver.\n"
                        + "Error message: " + ex.getMessage(),
                        "Initialization Error",
                        JOptionPane.ERROR_MESSAGE);
    
                System.exit(1);
            }
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            System.out.println("Navigating to eBay...");
            driver.get(EBAY_URL);

            System.out.println("Waiting for the sign-in link to be clickable...");
            clickElementWithRetry(wait, By.linkText("Sign in"));

            System.out.println("Using credentials: username = " + this.username + ", password = " + this.password);

            System.out.println("Waiting for the username field to be visible...");
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userid")));
            if (username != null && !username.isEmpty()) {
                usernameField.sendKeys(username);
            } else {
                System.out.println("Username is null or empty!");
                return;
            }

            System.out.println("Waiting for the continue button to be clickable...");
            clickElementWithRetry(wait, By.id("signin-continue-btn"));

            System.out.println("Waiting for the password field to be visible...");
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));
            if (password != null && !password.isEmpty()) {
                passwordField.sendKeys(password);
            } else {
                System.out.println("Password is null or empty!");
                return; 
            }

            System.out.println("Waiting for the sign-in button to be clickable...");
            clickElementWithRetry(wait, By.id("sgnBt"));
            System.out.println("Sign-in button clicked.");

            try {
                System.out.println("Checking for the 'Continue' button...");
                clickElementWithRetry(wait, By.xpath("//a[@class='fake-btn fake-btn--primary' and @data-ebayui]"));
                System.out.println("'Continue' button clicked.");
            } catch (Exception e) {
                System.out.println("'Continue' button not found. Proceeding with normal flow...");
            }

            System.out.println("Waiting for the Sell link to be clickable...");
            clickElementWithRetry(wait, By.linkText("Sell"));

            Thread.sleep(1000);

            System.out.println("Waiting for the Reports link to be clickable...");
            clickElementWithRetry(wait, By.linkText("Reports"));

            System.out.println("Waiting for the Downloads link to be clickable...");
            clickElementWithRetry(wait, By.linkText("Downloads"));

            Thread.sleep(1000);

            System.out.println("Waiting for the 'Download report' button to be clickable...");
            clickElementWithRetry(wait, By.cssSelector("button.btn.btn--primary[data-ebayui]"));
            System.out.println("Successfully clicked the 'Download report' button.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'se-field-card__right-icon' element to be clickable for source...");
            clickElementWithRetry(wait, By.cssSelector("span.se-field-card__right-icon"));
            System.out.println("Successfully clicked the 'se-field-card__right-icon' element for source.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'field__label' element for orders to be clickable...");
            clickElementWithRetry(wait, By.xpath("//label[@for='c2-0-11-6-2-undefined-{\"SOURCE\":\"ORDERS\"}']"));
            System.out.println("Successfully clicked the 'field__label' for orders.");

            Thread.sleep(1000);

            System.out.println("Waiting for the 'Type' element to be clickable...");
            clickElementWithRetry(wait, By.xpath("//div[text()='Type']"));
            System.out.println("Successfully clicked the 'Type' element for orders.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'field__label' element for all orders to be clickable...");
            clickElementWithRetry(wait, By.xpath("//label[@for='c2-0-11-6-2-undefined-{\"SOURCE\":\"ORDERS\",\"TYPE\":\"ALL_ORDERS\"}']"));
            System.out.println("Successfully clicked the 'field__label' for all orders.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'btn btn--primary' download button to be clickable for orders...");
            clickElementWithRetry(wait, By.cssSelector("button.btn.btn--primary[data-ebayui][type='button']"));
            System.out.println("Successfully clicked the 'btn btn--primary' download button for orders.");

            Thread.sleep(1000); 

            File downloadDir = new File(downloadFilepath);
            File[] oldFiles = downloadDir.listFiles();
            boolean downloadComplete = false;
            while (!downloadComplete) {
                File[] newFiles = downloadDir.listFiles();
                if (newFiles.length > oldFiles.length) {
                    downloadComplete = true;
                }
                Thread.sleep(1000); 
            }

            System.out.println("Orders download completed.");

            _06_EbayDataGUI.processDownloadedCSVFile();

            Thread.sleep(1000);

            driver.navigate().refresh();
            System.out.println("Page refreshed.");

            Thread.sleep(1000); 

            System.out.println("Navigating to the Downloads section...");
            clickElementWithRetry(wait, By.linkText("Downloads"));

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'Download report' button to be clickable again...");
            clickElementWithRetry(wait, By.cssSelector("button.btn.btn--primary[data-ebayui]"));
            System.out.println("Successfully clicked the 'Download report' button again.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'se-field-card__right-icon' element to be clickable...");
            clickElementWithRetry(wait, By.cssSelector("span.se-field-card__right-icon"));
            System.out.println("Successfully clicked the 'se-field-card__right-icon' element.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'field__label' element for listings to be clickable...");
            clickElementWithRetry(wait, By.cssSelector("label.field__label[for='c2-0-11-6-2-undefined-{\"SOURCE\":\"LISTINGS\"}']"));
            System.out.println("Successfully clicked the 'field__label' for listings.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'Type' element to be clickable...");
            clickElementWithRetry(wait, By.xpath("//div[text()='Type']"));
            System.out.println("Successfully clicked the 'Type' element.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'field__label' element for all active listings to be clickable...");
            clickElementWithRetry(wait, By.cssSelector("label.field__label[for='c2-0-11-6-2-undefined-{\"SOURCE\":\"LISTINGS\",\"TYPE\":\"ALL_LISTINGS\"}']"));
            System.out.println("Successfully clicked the 'field__label' for all active listings.");

            Thread.sleep(1000);

            System.out.println("Waiting for the 'btn btn--primary' download button to be clickable...");
            clickElementWithRetry(wait, By.cssSelector("button.btn.btn--primary[data-ebayui][type='button']"));
            System.out.println("Successfully clicked the 'btn btn--primary' download button.");

            Thread.sleep(1000); 

            File[] oldFilesListings = downloadDir.listFiles();
            boolean downloadCompleteListings = false;
            while (!downloadCompleteListings) {
                File[] newFilesListings = downloadDir.listFiles();
                if (newFilesListings.length > oldFilesListings.length) {
                    downloadCompleteListings = true;
                }
                Thread.sleep(1000); 
            }

            System.out.println("Listings download completed.");

            _08_TunT.processDownloadedCSVFile2();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed.");
            }
        }
    }

    private void clickElementWithRetry(WebDriverWait wait, By by) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("StaleElementReferenceException caught. Retrying... Attempt: " + attempts);
            } catch (Exception e) {
                attempts++;
                System.out.println("Exception caught. Retrying... Attempt: " + attempts);
            }
        }
        throw new RuntimeException("Failed to click element after 3 attempts: " + by.toString());
    }
}
