package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.safari.SafariDriver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotAutomation {

    public static void main(String[] args) {
        // List of resolutions to test
        int[][] resolutions = {
                {1920, 1080},
                {1366, 768},
                {1536, 864}
        };

        // List of browsers to test
        String[] browsers = {"chrome", "safari"};

        // List of URLs to test
        String[] urls = {
                "https://www.getcalley.com/",
                "https://www.getcalley.com/calley-lifetime-offer/",
                "https://www.getcalley.com/see-a-demo/",
                "https://www.getcalley.com/calley-teams-features/",
                "https://www.getcalley.com/calley-pro-features/"
        };

        // Directory to save screenshots
        String baseDir = "screenshots";

        for (String browserName : browsers) {
            for (int[] resolution : resolutions) {
                WebDriver driver = setupDriver(browserName);
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(resolution[0], resolution[1]));

                for (String url : urls) {
                    driver.get(url);

                    // Create folder structure
                    String path = createDirectoryStructure(baseDir, browserName, resolution, url);

                    // Take screenshot
                    takeScreenshot(driver, path);
                }

                driver.quit();
            }
        }
    }

    private static WebDriver setupDriver(String browserName) {
        WebDriver driver;
        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "safari":
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        return driver;
    }

    private static String createDirectoryStructure(String baseDir, String browserName, int[] resolution, String url) {
        // Clean URL to create a valid folder name
        String urlFolder = url.replaceAll("[^a-zA-Z0-9]", "_");
        File directory = new File(baseDir, browserName + "/" + resolution[0] + "x" + resolution[1] + "/" + urlFolder);
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
