package org.dani.pages;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreatePostPage extends BasePage {

    @FindBy(css = "input[name=\"caption\"]")
    private WebElement postCaptionInput;

    @FindBy(css = ".post-status-label.active")
    private WebElement postStatusLabel;

    @FindBy(css = "#customSwitch2")
    private WebElement postStatusSwitch;

    @FindBy(css = "#create-post")
    private WebElement createPostButton;

    /**
     * Creates a CreatePostPage and initializes all annotated elements.
     *
     * @param driver the WebDriver instance
     */
    public CreatePostPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Uploads a file (image) using a file input element.
     * @param file the file to upload
     */
    public void uploadFile(File file) {
        try {
            waitPresentInDom(By.cssSelector("input.file")).sendKeys(file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("File upload was NOT successful!");
        }
    }

    /** Sets the caption text in the post caption input field.
     *
     * @param captionText the caption to type
     */
    public void setPostCaption(String captionText) {
        typeText(postCaptionInput, captionText);
    }

    /**
     * Toggles the post visibility switch to match the desired state.
     * Clicks the switch only when the current state differs from the target.
     *
     * @param isPrivate {@code true} to make the post private, {@code false} for public
     */
    public void togglePostVisibilityToPrivate(boolean isPrivate) {
        boolean isActive = isElementVisible(By.cssSelector(".post-status-label.active"));
        if (isPrivate && isActive) {
            // click(postStatusSwitch);
            click(postStatusLabel);
        } else if (!isPrivate && !isActive) {
            click(postStatusLabel);
        }
    }

    /** Clicks the create post submit button. */
    private void clickCreatePostButton() {
        click(createPostButton);
    }

    /**
     * Performs the full post creation flow: uploads a file, sets the caption,
     * configures visibility and submits the form.
     *
     * @param file      the image file to upload
     * @param caption   the post caption text
     * @param isPrivate {@code true} to create a private post, {@code false} for public
     */
    public void createPost(File file, String caption, boolean isPrivate) {
        uploadFile(file);
        setPostCaption(caption);
        togglePostVisibilityToPrivate(isPrivate);
        clickCreatePostButton();
    }
}