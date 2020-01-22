package test;

import model.PagePost;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import model.LoginPage;
import rest.*;
import utility.ConfigReader;
import java.io.IOException;

public class FacebookPostVerification extends RequestHandler {

    private static WebDriver driver;
    private static ConfigReader config;
    private String messageId;
    private String accessToken;
    private long timeStamp;
    private int defaultTimeout = 60;

    @BeforeTest
    public void openBrowser() {

        config = new ConfigReader();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-notifications");

        System.setProperty("webdriver.chrome.driver", config.getChromePath());

        driver = new ChromeDriver(options);
    }

    @Test(priority=1)
    public void login(){

        driver.get(config.URL());

        LoginPage.input_email(driver).sendKeys(config.Username());
        LoginPage.input_password(driver).sendKeys(config.Password());
        LoginPage.btn_login(driver).click();

        String webTitle = driver.getTitle();

        Assert.assertEquals(webTitle, config.WebTitle(), "Title comparison to verify login");

    }

    @Test(priority=2)
    public void createPostAPI(){

        timeStamp = System.currentTimeMillis();

        RequestHandler requestHandler = new RequestHandler();

        CreatePostResponse createPostResponse = requestHandler.postMessage("FIRST POST " + timeStamp);

        System.out.println("Creating message with text: " + "FIRST POST " + timeStamp);

        messageId = String.valueOf(createPostResponse.id);

    }

    @Test(priority=3)
    public void createPostUI(){

        driver.navigate().refresh();

        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("profileLink")));

        String postMessage = PagePost.message(driver).getText();
        String postDate = PagePost.date(driver).getText();
        String postAuthor = PagePost.author(driver).getText();

        Assert.assertEquals(postMessage, "FIRST POST " + timeStamp, "Post message should be the same I sent");
        Assert.assertEquals(postDate, "Just now", "Time of post should be just now");
        Assert.assertEquals(postAuthor, config.PageName(), "Post author should be as configuration");

    }

    @Test(priority=4)
    public void updatePostAPI(){

        timeStamp = System.currentTimeMillis();

        RequestHandler requestHandler = new RequestHandler();

        UpdatePostResponse updatePost = requestHandler.updateMessage("UPDATED POST " + timeStamp, messageId);

        System.out.println("Updating message to text: " + "FIRST POST " + timeStamp);

    }

    @Test(priority=5)
    public void updatePostUI(){

        driver.navigate().refresh();

        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("profileLink")));

        String postMessage = PagePost.message(driver).getText();
        String postAuthor = PagePost.author(driver).getText();

        Assert.assertEquals(postMessage, "UPDATED POST " + timeStamp, "Post message should be the same I sent");
        Assert.assertEquals(postAuthor, config.PageName(), "Post author should be as configuration");

    }

    @Test(priority=6)
    public void deletePostAPI(){

        RequestHandler requestHandler = new RequestHandler();

        UpdatePostResponse deletePost = requestHandler.deleteMessage(messageId);

    }

    @Test(priority=7)
    public void deletePostUI(){

        driver.navigate().refresh();

        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pagelet_timeline_main_column")));

        String postMessage = null;

        try {
            postMessage = PagePost.message(driver).getText();
        }
            catch (NoSuchElementException ignored) {
        }

        if (postMessage != null) {

            Assert.assertNotEquals(postMessage, "UPDATED POST " + timeStamp, "Message was not deleted");

        } else {

            Assert.assertNull(postMessage, "Message was not deleted");

        }

    }

    @AfterClass
    public void theEnd(){

        driver.close();
        driver.quit();

    }

}