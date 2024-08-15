greetings...


# Project README

This program helps you sort your  your eBay data. Below is a description of each file and its purpose.

## Existing Files



### _00_StartingGUI.java
This file serves as the main entry point for the graphical user interface (GUI) of the application. It initializes the primary window and provides the user with options to interact with various features of the program. The class includes buttons to sort eBay data, manage themes, refresh completed orders and listings, and access information about the application. It also handles user credentials securely, allowing for automated data processing. Additionally, it offers a theme selection dialog and ensures that the chosen theme persists across sessions. This GUI serves as the central hub from which users can access the main functionalities of the program.

### _05_EbayApi.java
This file serves as the entry point for the eBay API-related functionality. It initializes the eBay API GUI.

### _06_EbayApiGUI.java
This file sets up the eBay API GUI, including buttons for choosing CSV files, displaying information, and processing turnover time. It processes CSV files to calculate brand averages and occurrences.

### _07_AvgP.java
This file contains utility methods for processing CSV files to calculate brand occurrences and average prices.

### _08_TunT.java
This file manages the GUI for displaying turnover time data. It includes buttons for file selection, information display, and returning to the average price display.

### _09_BrandList.java
This file provides a list of brands, brands with turnover time, and the categories we can search for used in the application.


### _10_TTCalc.java
This file calculates turnover times for each brand based on current listings and completed orders. It processes CSV data, matches listings to orders, calculates turnover times, and updates average turnover times.

### _11_Cat1.java to _30_PW.java
These files are specialized versions of the data processing and calculation logic, each tailored to handle a specific category of items, such as wristwatch movements, pocket watch cases, bands, parts, and others. Each class is responsible for:

Processing data related to its specified category
Calculating turnover times, sales, and other relevant metrics
Updating the respective category-specific files
These classes share similar logic but are customized for different item categories, ensuring accurate and relevant data processing across various product types.

### _31_StatsCalc.java
This file handles statistical calculations based on the processed data. It is used to calculate and display various metrics, such as averages, medians, and other statistical measures related to item sales and turnover times.

### _32_EbayAutomation.java
This file automates the interaction with eBay, including tasks like logging in, navigating the site, and downloading reports. It uses Selenium WebDriver for browser automation and handles actions such as clicking buttons and entering credentials.

### _33_UserEbayAutomation.java
This file builds on the automation logic by allowing users to interact with eBay through a GUI. It includes additional features like manually loading credentials from a properties file and handling specific user actions for downloading and processing eBay data.

### _34_CompareMethods.java
This file compares different methods of processing and analyzing data, focusing on the efficiency and accuracy of various approaches. It is used to benchmark and optimize the performance of data processing tasks in the application.

### _35_SBAPI.java
This file interacts with the Sellbrite API to fetch sales data, particularly from Shopify. It handles authentication, data retrieval, and processing of sales records, ensuring that new orders are correctly identified and stored.

### _36_ButtonRendererEditor.java
This file customizes the appearance and behavior of buttons within tables in the GUI. It allows buttons to render in table cells and handles user interactions, such as clicking a button to view detailed information about a specific brand or item.

### _37_LISTSB.java
This file fetches product listings from an API, ensuring that duplicate entries are avoided. It handles data retrieval, storage, and updates, focusing on managing and processing large datasets efficiently.

### _38_CREATEDATSBL.java
This file processes Shopify listings, compares them with eBay listings and orders, and updates the Shopify data to include dates when matches are found. It ensures that the complete Shopify file contains accurate and up-to-date information.

### _39_Transfer.java
This file handles the transfer of listing data into the complete file. It processes and filters data to ensure only relevant listings with created dates are transferred, avoiding duplicates and maintaining the integrity of the dataset.

### _40_ListOrderCompare.java
This file compares listings and orders to find matches and calculate turnover times. It processes the data, matches listings to orders based on specific criteria, and updates the turnover times for each matched item.

### _41_ParsedIntoTurnover.java
This file processes parsed turnover data and injects it into both general and category-specific turnover files. It ensures that new data is correctly integrated into the existing datasets, maintaining a comprehensive record of turnover times.

### _42_ShopifyCompiler.java
This file compiles and processes data from Shopify, ensuring that the data is formatted and stored correctly for further analysis. It handles tasks like merging different data sources, formatting records, and preparing the data for use in other parts of the application.

### _43_DiscountData.java
This file manages discount data, including calculating and displaying metrics related to non-discount sales, discount sales, and the overall impact of discounts on sales performance. It provides a GUI for users to view and analyze this data, and it includes functionality for sorting and displaying data by category and brand.



## How to Use

1. Run `__00_StartingGUI.java` to start the main application.
3. To work with eBay data, run `_05_EbayApi.java` to start the eBay API-related functionality.
4. Use `08_TunT.java` to display turnover time data and switch between different views.

