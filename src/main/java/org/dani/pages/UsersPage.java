package org.dani.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UsersPage extends BasePage {

    private static final By SPINNER_LOCATOR = By.cssSelector("app-spinner .loader");

    @FindBy(css = ".post-filter-buttons .btn-private")
    private WebElement postsFilterPrivateButton;

    @FindBy(css = "app-post-list .gallery-item")
    private List<WebElement> allPostsElements;

    public UsersPage(WebDriver driver) {
        super(driver);
    }

    /** Clicks the private posts filter button to show only private posts. */
    public void clickToFilterPrivatePosts() {
        click(postsFilterPrivateButton);
    }

    /** Clicks the first post in the gallery. */
    public void clickFirstPost() {
        click(allPostsElements.get(0));
    }

    /**
     * Scrolls to the bottom of the page repeatedly until no new posts are loaded.
     * After each scroll it waits for the lazy-load spinner to disappear before
     * comparing the post count. Exits when the count stops growing.
     *
     * @throws InterruptedException if the thread sleep inside the spinner wait is interrupted
     */
    public void waitForAllPostsToLoad() throws InterruptedException {
        int previousCount;
        do {
            previousCount = allPostsElements.size();
            scrollToBottom();
            waitForSpinnerToDisappear(SPINNER_LOCATOR);
        } while (allPostsElements.size() > previousCount);
    }

    /** Clicks the last post in the gallery (newest post when displayed in reverse order). */
    public void clickLastPost() {
        click(allPostsElements.get(allPostsElements.size() - 1));
    }

    /**
     * Clicks the post at the given zero-based index in the gallery.
     *
     * @param index zero-based position of the post to click
     */
    public void clickPostByIndex(int index) {
        click(allPostsElements.get(index));
    }
}