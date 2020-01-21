package test;

import model.PagePost;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
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

    @BeforeTest
    public void openBrowser() throws IOException {

        config = new ConfigReader();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-notifications");

        System.setProperty("webdriver.chrome.driver", config.getChromePath());

        driver = new ChromeDriver(options);
    }

    @Test
    public void login() throws InterruptedException {

        timeStamp = System.currentTimeMillis();
        RequestHandler requestHandler = new RequestHandler();
        CreatePostResponse createPostResponse = requestHandler.postMessage("FIRST POST " + timeStamp);
        messageId = String.valueOf(createPostResponse.id);

        driver.get(config.URL());

        LoginPage.input_email(driver).sendKeys(config.Username());

        LoginPage.input_password(driver).sendKeys(config.Password());

        LoginPage.btn_login(driver).click();

        String webTitle = driver.getTitle();

        Assert.assertEquals(webTitle, config.WebTitle(), "Title comparison to verify login");

    }

    @Test
    public void readPost() throws InterruptedException {

        String postMessage = PagePost.message(driver).getText();
        String postDate = PagePost.date(driver).getText();
        String postAuthor = PagePost.author(driver).getText();

        Assert.assertEquals(postMessage, "FIRST POST " + timeStamp, "Post message should be the same I sent");
        Assert.assertEquals(postDate, "Just now", "Time of post should be just now");
        Assert.assertEquals(postMessage, config.PageName(), "Post author should be as configuration");

    }

    @Test
    public void updatePostTest() throws InterruptedException {

        timeStamp = System.currentTimeMillis();
        RequestHandler requestHandler = new RequestHandler();
        UpdatePostResponse updatePost = requestHandler.updateMessage("UPDATED POST " + timeStamp, messageId);

        String postMessage = PagePost.message(driver).getText();
        String postAuthor = PagePost.author(driver).getText();

        driver.navigate().refresh();

        Assert.assertEquals(postMessage, "UPDATED POST " + timeStamp, "Post message should be the same I sent");
        Assert.assertEquals(postMessage, config.PageName(), "Post author should be as configuration");

    }

    @Test
    public void deletePostTest() throws InterruptedException {

        RequestHandler requestHandler = new RequestHandler();

        UpdatePostResponse deletePost = requestHandler.deleteMessage(messageId);

        String postMessage = PagePost.message(driver).getText();
        String postAuthor = PagePost.author(driver).getText();

        driver.navigate().refresh();

        Assert.assertEquals(postMessage, "UPDATED POST " + timeStamp, "Post message should be the same I sent");
        Assert.assertEquals(postMessage, config.PageName(), "Post author should be as configuration");

    }

    @AfterClass
    public void theEnd(){
        driver.close();
        driver.quit();
    }

}