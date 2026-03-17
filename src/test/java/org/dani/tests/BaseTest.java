package org.dani.tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    public WebDriver driver;

    /**
     * Initializes the ChromeDriver, maximizes the window and sets page-load timeout.
     * Runs once before all tests in the class.
     */
    @BeforeClass
    public void setUp() {
        // 1. Initialize the driver
        driver = new ChromeDriver();

        // 2. Manage window and timeouts
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        System.out.println(">>> Driver initialized for the test.");
    }

    /**
     * Quits the WebDriver and closes all associated browser windows.
     * Runs once after all tests in the class, even if tests fail.
     */
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        // 3. Close the browser
        if (driver != null) {
            driver.quit();
            System.out.println("<<< Driver closed.");
        }
    }
}
