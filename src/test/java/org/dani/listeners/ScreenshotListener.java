package org.dani.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dani.tests.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListener implements ITestListener {

    private static final String SCREENSHOTS_DIR = "src/resources/screenshots";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Captures a full browser window screenshot whenever a test fails.
     * The file is saved to {@code screenshots/<testName>_<timestamp>.png}
     * relative to the project root.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        Object instance = result.getInstance();
        if (!(instance instanceof BaseTest)) {
            return;
        }

        WebDriver driver = ((BaseTest) instance).driver;
        if (driver == null) {
            return;
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = result.getName() + "_" + timestamp + ".png";
        Path screenshotsDir = Paths.get(SCREENSHOTS_DIR);

        try {
            Files.createDirectories(screenshotsDir);

            java.io.File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = screenshotsDir.resolve(fileName);
            Files.copy(srcFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save screenshot for test '" + result.getName() + "': " + e.getMessage());
        }
    }
}
