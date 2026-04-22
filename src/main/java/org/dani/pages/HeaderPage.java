package org.dani.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HeaderPage extends BasePage {

    @FindBy(css = "#nav-link-home")
    private WebElement homeNavButton;

    @FindBy(css = "#nav-link-profile")
    private WebElement profileNavButton;

    @FindBy(css = "#nav-link-new-post")
    private WebElement newPostNavButton;

    /**
     * Creates a HeaderPage and initializes all annotated elements.
     *
     * @param driver the WebDriver instance
     */
    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public void clickNewPostNavButton() {
        click(newPostNavButton);
    }
}