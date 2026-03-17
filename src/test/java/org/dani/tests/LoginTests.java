package org.dani.tests;

import org.dani.pages.HomePage;
import org.dani.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test
    public void loginWithCorrectCredentialsSuccessful() {
        // 1. Page Object Initialization
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        // 2. Navigation
        loginPage.navigateToPage();

        // 3. Business Logic (Action)
        loginPage.login("daniel11", "1qaz!QAZ");
        homePage.verifyPageLoaded();

        // 4. Assertion
        Assert.assertTrue(homePage.isUrlLoaded(), "User was not redirected to the Home Page!");
    }
}
