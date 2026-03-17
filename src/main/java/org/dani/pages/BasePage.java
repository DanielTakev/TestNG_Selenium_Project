package org.dani.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    /** Shared WebDriver instance. */
    protected WebDriver driver;

    /** Explicit wait used for custom conditions (URL waits, visibility, etc.). */
    protected WebDriverWait wait;

    /** Base URL shared across all pages. */
    protected static final String BASE_URL = "http://training.skillo-bg.com:4300";

    /**
     * Initializes the page using {@link AjaxElementLocatorFactory}.
     * <p>
     * Every {@code @FindBy} field in the subclass becomes a proxy that will poll the DOM
     * for up to {@code timeoutInSeconds} before throwing {@code NoSuchElementException}.
     * </p>
     *
     * <pre>{@code
     * // This is what happens under the hood:
     * PageFactory.initElements(
     *     new AjaxElementLocatorFactory(driver, timeoutInSeconds),
     *     this  // scans THIS object's @FindBy fields
     * );
     * }</pre>
     *
     * @param driver           the WebDriver instance
     * @param timeoutInSeconds max seconds to wait for each element lookup
     */
    public BasePage(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

        // ---- Page Factory initialization with AjaxElementLocatorFactory ----
        // This single line replaces ALL manual findElement() calls in the subclass.
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, timeoutInSeconds), this);
    }

    /**
     * Overloaded constructor with a default 10-second element timeout.
     *
     * @param driver the WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this(driver, 10);
    }

    /**
     * Navigates the browser to {@code BASE_URL + urlSuffix}.
     *
     * @param urlSuffix the path to append, e.g. {@code "/users/login"}
     */
    protected void navigateTo(String urlSuffix) {
        driver.get(BASE_URL + urlSuffix);
    }

    /**
     * Waits until the current browser URL exactly matches the given URL.
     *
     * @param url the expected full URL
     */
    protected void waitForUrl(String url) {
        wait.until(ExpectedConditions.urlToBe(url));
    }

    protected boolean isElementVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            System.out.println("Element: " + locator + " >>> NOT FOUND!");
            return false;
        }
    }

    /**
     * Waits for the {@link WebElement} to be visible, clears it and types text.
     * <p>
     * Note: in Page Factory the element is already a proxy — no need for
     * {@code driver.findElement(By...)}. The proxy resolves on first access.
     * </p>
     *
     * @param element the target WebElement (resolved by Page Factory proxy)
     * @param text    the text to type
     */
    protected void typeText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Waits until the element is clickable and clicks it.
     *
     * @param element the target WebElement
     */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Returns the current URL from the browser's address bar.
     *
     * @return current URL string
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
