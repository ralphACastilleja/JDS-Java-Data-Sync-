package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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








public class _32_EbayAutomation {

    private static final String EBAY_URL = "https://www.ebay.com/";
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

        _32_EbayAutomation automation = new _32_EbayAutomation();
        automation.loadCredentials();
        automation.downloadReports(false); // Pass true if you want to run in headless mode
    }
        // Load credentials , password and user 

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


    //After the method is downloaded, Find it in the users directories 
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
                
                // Show error message dialog
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

            // Navigate to eBay
            System.out.println("Navigating to eBay...");
            driver.get(EBAY_URL);

            // Click on the sign-in link
            System.out.println("Waiting for the sign-in link to be clickable...");
            WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign in")));
            signInLink.click();

            // Debugging output to check loaded credentials
            System.out.println("Using credentials: username = " + this.username + ", password = " + this.password);

            // Enter username
            System.out.println("Waiting for the username field to be visible...");
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userid")));
            if (username != null && !username.isEmpty()) {
                usernameField.sendKeys(username);
            } else {
                System.out.println("Username is null or empty!");
                return; // Exit the method if username is null or empty
            }

            // Click continue
            System.out.println("Waiting for the continue button to be clickable...");
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin-continue-btn")));
            continueButton.click();

            // Wait for the password field to be visible and enter password
            System.out.println("Waiting for the password field to be visible...");
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));
            if (password != null && !password.isEmpty()) {
                passwordField.sendKeys(password);
            } else {
                System.out.println("Password is null or empty!");
                return; // Exit the method if password is null or empty
            }
// Click sign in
// Click sign in
System.out.println("Waiting for the sign-in button to be clickable...");
WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("sgnBt")));
signInButton.click();
System.out.println("Sign-in button clicked.");

// Check for the "Continue" button after sign-in
try {
    System.out.println("Checking for the 'Continue' button...");
    WebElement continueButtonAfterSignIn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='fake-btn fake-btn--primary' and @data-ebayui]")));
    if (continueButtonAfterSignIn.isDisplayed()) {
        System.out.println("'Continue' button found. Clicking it...");
        continueButtonAfterSignIn.click();
        System.out.println("'Continue' button clicked.");
    }
} catch (Exception e) {
    System.out.println("'Continue' button not found. Proceeding with normal flow...");
}

// Wait for the Sell link to be clickable
System.out.println("Waiting for the Sell link to be clickable...");
WebElement sellLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sell")));
sellLink.click();



            Thread.sleep(1000);

            System.out.println("Waiting for the 'Switch account' element to be clickable...");
            WebElement switchAccountElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Switch account')]")));
            switchAccountElement.click();

            System.out.println("Successfully clicked on the 'Switch account' element.");

            Thread.sleep(1000); 

            //Click on the "dev" element
            System.out.println("Waiting for the 'Ralph Castilleja' element to be clickable...");
            WebElement ralphElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='nav-link-info-name' and contains(text(), 'Ralph Castilleja')]")));
            ralphElement.click();

            System.out.println("Successfully clicked on the 'Ralph Castilleja' element.");

            Thread.sleep(2000); 

            System.out.println("Waiting for the password field to be visible...");
            WebElement passwordField2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));
            passwordField2.sendKeys(password);

            System.out.println("Waiting for the sign-in button to be clickable again...");
            WebElement signInButtonAgain = wait.until(ExpectedConditions.elementToBeClickable(By.id("sgnBt")));
            signInButtonAgain.click();

            // Navigate to the Sell page again
            System.out.println("Waiting for the Sell link to be clickable again...");
            WebElement sellLinkAgain = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sell")));
            sellLinkAgain.click();

            // Navigate to the Reports section
            System.out.println("Waiting for the Reports link to be clickable...");
            WebElement reportsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Reports")));
            reportsLink.click();

            // Navigate to the Downloads section
            System.out.println("Waiting for the Downloads link to be clickable...");
            WebElement downloadsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Downloads")));
            downloadsLink.click();

            Thread.sleep(1000);

            // Click on the "Download report" button
            System.out.println("Waiting for the 'Download report' button to be clickable...");
            WebElement downloadReportButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn--primary[data-ebayui]")));
            downloadReportButton.click();

            System.out.println("Successfully clicked the 'Download report' button.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'se-field-card__right-icon' element to be clickable...");
            WebElement rightIconElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.se-field-card__right-icon")));
            js.executeScript("arguments[0].scrollIntoView(true);", rightIconElement);
            wait.until(ExpectedConditions.elementToBeClickable(rightIconElement)).click();

            System.out.println("Successfully clicked the 'se-field-card__right-icon' element.");

            Thread.sleep(1000); 

            // Click on the label for listings using a more specific selector
            System.out.println("Waiting for the 'field__label' element for listings to be clickable...");
            WebElement listingsLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label.field__label[for='c2-0-11-6-2-undefined-{\"SOURCE\":\"LISTINGS\"}']")));
            js.executeScript("arguments[0].scrollIntoView(true);", listingsLabel);
            wait.until(ExpectedConditions.elementToBeClickable(listingsLabel)).click();

            System.out.println("Successfully clicked the 'field__label' for listings.");

            Thread.sleep(1000);

            // Click on the "Type" element
            System.out.println("Waiting for the 'Type' element to be clickable...");
            WebElement typeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Type']")));
            js.executeScript("arguments[0].scrollIntoView(true);", typeElement);
            wait.until(ExpectedConditions.elementToBeClickable(typeElement)).click();

            System.out.println("Successfully clicked the 'Type' element.");

            Thread.sleep(1000);


            //HEre

            System.out.println("Waiting for the 'field__label' element for orders to be clickable...");
            WebElement ordersLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='c2-0-11-6-2-undefined-{\"SOURCE\":\"ORDERS\"}']")));
            js.executeScript("arguments[0].scrollIntoView(true);", ordersLabel);
            ordersLabel.click();
   
            System.out.println("Successfully clicked the 'field__label' for all active listings.");

            Thread.sleep(1000);

            // Click on the "Download" button
            System.out.println("Waiting for the 'btn btn--primary' download button to be clickable...");
            WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn--primary[data-ebayui][type='button']")));
            js.executeScript("arguments[0].scrollIntoView(true);", downloadButton);
            wait.until(ExpectedConditions.elementToBeClickable(downloadButton)).click();

            System.out.println("Successfully clicked the 'btn btn--primary' download button.");

            Thread.sleep(1000); 
            File downloadDir = new File(downloadFilepath);
            File[] oldFiles = downloadDir.listFiles();
            boolean downloadComplete = false;
            while (!downloadComplete) {
                File[] newFiles = downloadDir.listFiles();
                if (newFiles.length > oldFiles.length) {
                    downloadComplete = true;
                }
                Thread.sleep(1000); // Check every second
            }

            System.out.println("Download completed.");

            driver.navigate().refresh();
            System.out.println("Page refreshed.");

            Thread.sleep(1000); //ensure the page fully reloads

            System.out.println("Navigating to the Downloads section...");
            WebElement downloadsLink2 = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Downloads")));
            downloadsLink2.click();

            // ORDERS

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'Download report' button to be clickable again...");
            WebElement downloadReportButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn--primary[data-ebayui]")));
            js.executeScript("arguments[0].scrollIntoView(true);", downloadReportButton2);
            downloadReportButton2.click();

            System.out.println("Successfully clicked the 'Download report' button again.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'se-field-card__right-icon' element to be clickable for source...");
            WebElement rightIconElementSource = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.se-field-card__right-icon")));
            js.executeScript("arguments[0].scrollIntoView(true);", rightIconElementSource);
            rightIconElementSource.click();

            System.out.println("Successfully clicked the 'se-field-card__right-icon' element for source.");

            Thread.sleep(1000); 


            //HERE

            System.out.println("Waiting for the 'field__label' element for all active listings to be clickable...");
            WebElement allActiveListingsLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label.field__label[for='c2-0-11-6-2-undefined-{\"SOURCE\":\"LISTINGS\",\"TYPE\":\"ALL_LISTINGS\"}']")));
            js.executeScript("arguments[0].scrollIntoView(true);", allActiveListingsLabel);
            wait.until(ExpectedConditions.elementToBeClickable(allActiveListingsLabel)).click();

          

            System.out.println("Successfully clicked the 'field__label' for orders.");

            Thread.sleep(1000); 

            System.out.println("Waiting for the 'Type' element to be clickable...");
            WebElement typeElementOrders = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Type']")));
            js.executeScript("arguments[0].scrollIntoView(true);", typeElementOrders);
            typeElementOrders.click();

            System.out.println("Successfully clicked the 'Type' element for orders.");

            Thread.sleep(1000); 
            System.out.println("Waiting for the 'field__label' element for all orders to be clickable...");
            WebElement allOrdersLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='c2-0-11-6-2-undefined-{\"SOURCE\":\"ORDERS\",\"TYPE\":\"ALL_ORDERS\"}']")));
            js.executeScript("arguments[0].scrollIntoView(true);", allOrdersLabel);
            wait.until(ExpectedConditions.elementToBeClickable(allOrdersLabel)).click();

            System.out.println("Successfully clicked the 'field__label' for all orders.");

          
            Thread.sleep(1000); 
            System.out.println("Waiting for the 'btn btn--primary' download button to be clickable for orders...");
            WebElement downloadButtonOrders = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn--primary[data-ebayui][type='button']")));
            js.executeScript("arguments[0].scrollIntoView(true);", downloadButtonOrders);
            wait.until(ExpectedConditions.elementToBeClickable(downloadButtonOrders)).click();

            System.out.println("Successfully clicked the 'btn btn--primary' download button for orders.");

            Thread.sleep(1000);

            File[] oldFilesOrders = downloadDir.listFiles();
            boolean downloadCompleteOrders = false;
            while (!downloadCompleteOrders) {
                File[] newFilesOrders = downloadDir.listFiles();
                if (newFilesOrders.length > oldFilesOrders.length) {
                    downloadCompleteOrders = true;
                }
                Thread.sleep(1000); 
            }

            System.out.println("Orders download completed.");





    

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed.");
            }
        }
    }
    @SuppressWarnings("unused")
    private File getLatestFileFromDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            return null;
        }
        File lastModifiedFile = files[0];
        for (File file : files) {
            if (file.lastModified() > lastModifiedFile.lastModified()) {
                lastModifiedFile = file;
            }
        }
        return lastModifiedFile;
    }
    
}
