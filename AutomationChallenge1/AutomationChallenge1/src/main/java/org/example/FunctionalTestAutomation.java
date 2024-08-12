package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class FunctionalTestAutomation {

    public static void main(String[] args) {
        WebDriver driver = setupDriver();
        try {
            // Navigate to URL
            driver.get("https://demo.dealsdray.com/");

            // Log in
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("prexo.mis@dealsdray.com");
            driver.findElement(By.name("password")).sendKeys("prexo.mis@dealsdray.com");
            driver.findElement(By.xpath("//button[text()='Login']")).click();

            // Wait for the order section to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='MuiButtonBase-root has-submenu compactNavItem css-46up3a']"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='sidenavHoverShow MuiBox-root css-i9zxpg' and text()='Orders']"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root  css-vwfva9']"))).click();

            // Wait for the file upload input to be visible and upload the file
            WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='MuiOutlinedInput-input MuiInputBase-input MuiInputBase-inputSizeSmall css-1imb3v5']")));
            uploadButton.sendKeys("/Users/yogeshwar.pichewar/Downloads/demo-data.xlsx");

            // Wait for the upload button and click it
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root  css-6aomwy']"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root  css-6aomwy']"))).click();

            // Handle alert
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            alert.accept();

            // Prepare for screenshot capture
            WebElement element = driver.findElement(By.xpath("//button[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root Mui-disabled  css-1e9sayh'][2]"));
            String outputFolder = createDirectoryStructure("output");

            // Scroll the element into view and take screenshots
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Take screenshot before scrolling
            takeScreenshot(driver, outputFolder + "/screenshot1.png");

            // Scroll the element into view
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            takeScreenshot(driver, outputFolder + "/screenshot2.png");

            // Scroll further down and take another screenshot
            js.executeScript("window.scrollBy(0, document.body.scrollHeight);");
            takeScreenshot(driver, outputFolder + "/screenshot3.png");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static WebDriver setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Additional options can be set here if needed
        return new ChromeDriver(options);
    }

    private static String createDirectoryStructure(String baseDir) {
        File directory = new File(baseDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath();
    }

    private static void takeScreenshot(WebDriver driver, String path) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File srcFile = ts.getScreenshotAs(OutputType.FILE);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        File destFile = new File(path, "screenshot-" + timestamp + ".png");

        try {
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("Saved screenshot to " + destFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
