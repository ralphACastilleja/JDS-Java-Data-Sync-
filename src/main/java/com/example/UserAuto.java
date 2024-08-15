package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class UserAuto {

    private static final String EBAY_URL = "https://www.ebay.com/";
    private static final String USERNAME = "racasti_62";
    private static final String PASSWORD = "Chase@123";

    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");
        String chromeDriverPath = userHome + "/chromedriver";
        String downloadFilepath = userHome + "/Downloads";

        WebDriver driver = null;

        try {
            // Try to set up ChromeDriver
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

            // Initialize ChromeDriver
            driver = new ChromeDriver(options);

        } catch (Exception chromeException) {
            System.out.println("ChromeDriver not found or failed to initialize. Trying SafariDriver...");

            try {
                // Initialize SafariDriver as fallback
                driver = new SafariDriver();
            } catch (Exception safariException) {
                System.out.println("SafariDriver not found or failed to initialize. Exiting...");
                safariException.printStackTrace();
                System.exit(1);
            }
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Navigate to eBay
            driver.get(EBAY_URL);

            // Click on the sign-in link
            WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign in")));
            signInLink.click();

            // Enter username
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userid")));
            usernameField.sendKeys(USERNAME);

            // Click continue
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin-continue-btn")));
            continueButton.click();

            // Wait for the password field to be visible and enter password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));
            passwordField.sendKeys(PASSWORD);

            // Click sign in
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("sgnBt")));
            signInButton.click();

          // Navigate to the Seller Hub
WebElement sellLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sell")));
sellLink.click();

// Add a short wait to ensure the page loads
Thread.sleep(2000);

try {
    // Wait for "Back to account access" button to be visible and clickable
    WebElement backToAccountAccess = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Back to account access')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backToAccountAccess);

    // Wait for "racasti_62" account link to be visible and clickable
    WebElement racastiAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'racasti_62')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", racastiAccountLink);

    // Add a short wait to ensure the account switch is processed
    Thread.sleep(2000);

    // Retry navigating to the Seller Hub
    WebElement sellLinkRetry = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sell")));
    sellLinkRetry.click();

} catch (Exception e) {
    System.out.println("Switch account process failed.");
    e.printStackTrace();
}

// Click on "Reports" under "Sell"
WebElement reportsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Reports")));
reportsLink.click();

// Select the Orders source
WebElement ordersSource = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Orders")));
ordersSource.click();

// Select the "All orders" type
WebElement orderType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Type')]")));
orderType.click();
WebElement allOrders = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'All orders')]")));
allOrders.click();

// Select the "Last month" date range
WebElement dateRange = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Date range')]")));
dateRange.click();
WebElement lastMonth = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Last month')]")));
lastMonth.click();

// Click the download button
WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Download')]")));
downloadButton.click();

// Wait for the download to complete
Thread.sleep(20000); // Adjust the sleep time as needed

// Verify the downloaded file
if (Paths.get(downloadFilepath, "orders.csv").toFile().exists()) {
    System.out.println("Order report downloaded successfully.");
} else {
    System.out.println("Failed to download order report.");
}



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                // Close the browser
                driver.quit();
            }
        }
    }
}
