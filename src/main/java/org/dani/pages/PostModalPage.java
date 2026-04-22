package org.dani.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class PostModalPage extends BasePage {

    @FindBy(css = ".post-modal-container .post-title")
    private WebElement postTitle;

    public PostModalPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits for the post title element to be visible and returns its text.
     *
     * @return the post title text
     */
    public String getPostTitleText() {
        waitForElementToBeVisible(By.cssSelector(".post-modal-container .post-title"));
        return postTitle.getText();
    }

    /** Closes the modal by sending the ESCAPE key. */
    public void closeModal() {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
    }
}