package org.dani.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    private static final String LOGIN_URL = "/users/login";

    // ---- @FindBy + @CacheLookup examples ----

    /**
     * Username input field.
     * <p>
     * {@code @CacheLookup} is safe here because the field is rendered on the initial
     * page load and is never removed / re-created by JavaScript.
     * </p>
     */
    @FindBy(css = "#defaultLoginFormUsername")
    @CacheLookup
    private WebElement usernameField;

    /**
     * Password input field (cached — static element).
     */
    @FindBy(css = "#defaultLoginFormPassword")
    @CacheLookup
    private WebElement passwordField;

    /**
     * Sign-in button located by {@code id} (cached — static element).
     */
    @FindBy(css = "#sign-in-button")
    @CacheLookup
    private WebElement signInButton;

    /**
     * Creates a LoginPageFactory instance.
     * <p>
     * The parent constructor ({@link BasePage}) calls
     * {@code PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this)}
     * which scans <em>this</em> class for {@code @FindBy} fields and creates proxies.
     * </p>
     *
     * @param driver the WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates the browser to the login page.
     */
    public void navigateToPage() {
        navigateTo(LOGIN_URL);
    }

    /**
     * Types the given username into the username input field.
     *
     * @param username the username to enter
     */
    public void enterUsername(String username) {
        typeText(usernameField, username);
    }

    /**
     * Types the given password into the password input field.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    /**
     * Clicks the Sign In button.
     */
    public void clickSignIn() {
        click(signInButton);
    }

    /**
     * Performs the complete login flow: enters credentials and clicks Sign In.
     *
     * @param username the username
     * @param password the password
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
    }

    /**
     * Returns the expected full URL of the login page.
     *
     * @return expected login page URL
     */
    public String getExpectedUrl() {
        return BASE_URL + LOGIN_URL;
    }
}
