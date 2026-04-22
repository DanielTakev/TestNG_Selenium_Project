package org.dani.tests;

import java.io.File;

import org.dani.pages.CreatePostPage;
import org.dani.pages.HeaderPage;
import org.dani.pages.HomePage;
import org.dani.pages.LoginPage;
import org.dani.pages.PostModalPage;
import org.dani.pages.UsersPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.bytebuddy.utility.RandomString;

public class PostTests extends BaseTest {

    File postPicture = new File("src/resources/fileUploads/cat-good-job.jpeg");
    String postCaption = "test ID testCreateNewPost_" + RandomString.make(5);

    HeaderPage headerPage;
    CreatePostPage createPostPage;
    UsersPage usersPage;
    PostModalPage postModalPage;

    @BeforeClass
    public void loginWithCorrectCredentialsSuccessful() {
        // 1. Page Object Initialization
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        headerPage = new HeaderPage(driver);
        createPostPage = new CreatePostPage(driver);
        usersPage = new UsersPage(driver);
        postModalPage = new PostModalPage(driver);

        // 2. Navigation
        loginPage.navigateToPage();

        // 3. Business Logic (Action)
        loginPage.login("daniel11", "1qaz!QAZ");
        homePage.verifyPageLoaded();

        // 4. Assertion
        Assert.assertTrue(homePage.isUrlLoaded(), "User was not redirected to the Home Page!");
        Assert.assertTrue(homePage.isToastMessageVisible("Successful login!"), "The success toast message did not appear!");
    }

    @Test
    public void testCreateNewPost() throws InterruptedException {
        headerPage.clickNewPostNavButton();
        createPostPage.createPost(postPicture, postCaption, true);
        Assert.assertTrue(createPostPage.isToastMessageVisible("Post created!"), "The success toast message did not appear!");

        usersPage.clickToFilterPrivatePosts();
        usersPage.waitForAllPostsToLoad();

        usersPage.clickLastPost();
        Assert.assertEquals(postModalPage.getPostTitleText(), postCaption, "The post title is not correct!");
    }
}
