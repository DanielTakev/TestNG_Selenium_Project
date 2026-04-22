package org.dani.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    /**
     * Waits until the element is present in the DOM.
     * @param locator the element locator
     */
    protected WebElement waitPresentInDom(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
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

    protected void waitForElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
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
     * Scrolls the element into view before clicking.
     *
     * @param element the target WebElement
     */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        element.click();
    }

    /**
     * Waits until the toast message element contains exactly the expected text.
     * Prints the actual text if there is a mismatch, or a not-found message if
     * the element is absent.
     *
     * @param expectedText the exact text expected in the {@code .toast-message} element
     * @return {@code true} if the toast displays the expected text, {@code false} otherwise
     */
    public boolean isToastMessageVisible(String expectedText) {
        try {
            wait.until(ExpectedConditions.textToBe(By.cssSelector(".toast-message"), expectedText));
            return true;
        } catch (Exception e) {
            try {
                String actualText = driver.findElement(By.cssSelector(".toast-message")).getText();
                System.out.println("Toast message mismatch! Expected: '" + expectedText + "' | Actual: '" + actualText + "'");
            } catch (Exception inner) {
                System.out.println("Toast message with text '" + expectedText + "' >>> NOT FOUND!");
            }
            return false;
        }
    }

    /** Scrolls the browser window to the very bottom of the page using JavaScript. */
    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Waits for a lazy-load spinner to appear and then disappear.
     * Sleeps briefly first to give the spinner time to appear after a scroll,
     * then waits up to the configured timeout for it to become invisible.
     * If the spinner never appears (no more content loading), the wait is skipped silently.
     *
     * @param spinnerLocator the {@link By} locator for the spinner element
     * @throws InterruptedException if the thread sleep is interrupted
     */
    protected void waitForSpinnerToDisappear(By spinnerLocator) throws InterruptedException {
        Thread.sleep(700);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(spinnerLocator));
        } catch (Exception e) {
            // Spinner never appeared or already gone — safe to continue
        }
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
