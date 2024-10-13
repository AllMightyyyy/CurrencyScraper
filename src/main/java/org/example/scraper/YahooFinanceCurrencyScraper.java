package org.example.scraper;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YahooFinanceCurrencyScraper {
    private static final String BASE_URL = "https://finance.yahoo.com/markets/currencies/";
    private static final Logger logger = Logger.getLogger(YahooFinanceCurrencyScraper.class.getName());

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the scraper to run every 6 hours
        scheduler.scheduleAtFixedRate(() -> {
            logger.info("Starting scraper at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            scrapeCurrencyData();
        }, 0, 6, TimeUnit.HOURS);
    }

    public static void scrapeCurrencyData() {
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckoDriver\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);  // Enable headless mode for background execution

        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String csvFilePath = "currency_data_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + ".csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            String[] headers = {"Currency Pair", "Price", "Change", "Change %", "Timestamp"};
            writer.writeNext(headers);

            driver.get(BASE_URL);

            try {
                WebElement consentOverlay = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector(".consent-overlay")));
                WebElement acceptButton = consentOverlay.findElement(By.cssSelector(".accept-all"));
                acceptButton.click();
                logger.info("Accepted cookie consent.");
            } catch (Exception e) {
                logger.info("No consent form displayed or failed to accept.");
            }

            String tableXPath = "/html/body/div[2]/main/section/section/section/article/section[1]/div/div/div/table";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(tableXPath)));

            for (int i = 1; i <= 23; i++) {
                try {
                    // Currency name
                    String currencyNameXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div/div/table/tbody/tr[%d]/td[1]/span/div/a/div/span[2]", i);
                    String currencyName = driver.findElement(By.xpath(currencyNameXPath)).getText().trim();

                    // Price
                    String priceXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div/div/table/tbody/tr[%d]/td[2]/span/fin-streamer", i);
                    String price = driver.findElement(By.xpath(priceXPath)).getText().trim();

                    // Change
                    String changeXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div/div/table/tbody/tr[%d]/td[3]/span/fin-streamer", i);
                    String change = driver.findElement(By.xpath(changeXPath)).getText().trim();

                    // Change percentage
                    String changePercentXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div/div/table/tbody/tr[%d]/td[4]/span/fin-streamer", i);
                    String changePercent = driver.findElement(By.xpath(changePercentXPath)).getText().trim();

                    // Print and write the data to the CSV
                    logger.info("Currency: " + currencyName + " | Price: " + price + " | Change: " + change + " | Change %: " + changePercent);

                    String[] data = {currencyName, price, change, changePercent, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))};
                    writer.writeNext(data);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error processing row " + i + ": " + e.getMessage());
                }
            }

            logger.info("Data extraction completed successfully and saved to: " + csvFilePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Error: ", e);
        } finally {
            driver.quit();
        }
    }
}
