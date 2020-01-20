package test;

import com.google.gson.Gson;
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
import java.util.*;

public class FacebookPostVerification extends RequestHandler {

    public static WebDriver driver;
    public static ConfigReader config;
    private String accessToken;
    private String messageId;
    private long timeStamp;

    @BeforeClass
    public static void setDriver() {
    }

    @BeforeTest
    public void openBrowser() throws IOException {

        config = new ConfigReader();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-notifications");

        System.setProperty("webdriver.chrome.driver", config.getChromePath());

        driver = new ChromeDriver(options);
    }

    public RequestUserAccessToken getUserAccessToken() {

        RequestUserAccessToken userToken = getUserAccessToken();

        for (Datum data : userToken.getData()) {
            accessToken = data.getAccessToken();
        }

        return accessToken; //TODO Fix with correct resolution
    }

    public RequestPageAccessToken getPageAccessToken() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", String.valueOf(getUserAccessToken()));
        RequestPageAccessToken pageToken = getPageAccessToken(parameters);

        for (Datum data : pageToken.getData()) {
            accessToken = data.getAccessToken();
        }

        return accessToken;
    }

    public CreatePostResponse createPost() {

        timeStamp = System.currentTimeMillis();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("message", "FIRST POST " + timeStamp);
        parameters.put("access_token", String.valueOf(getPageAccessToken()));
        CreatePostResponse postID = postMessage(parameters);

        messageId = String.valueOf(postID.id);

        return new CreatePostResponse(postID.id);
    }

    public void updateMessage(){

        timeStamp = System.currentTimeMillis();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("message", "UPDATED POST " + timeStamp);
        parameters.put("access_token", String.valueOf(getPageAccessToken()));

    }

    public void deleteMessage(){

        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", String.valueOf(getPageAccessToken()));

    }


    @Test
    public void login() throws InterruptedException {

        createPost();

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
    public void updatePost() throws InterruptedException {

        updateMessage();

        String postMessage = PagePost.message(driver).getText();
        String postAuthor = PagePost.author(driver).getText();

        driver.navigate().refresh();

        Assert.assertEquals(postMessage, "UPDATED POST " + timeStamp, "Post message should be the same I sent");
        Assert.assertEquals(postMessage, config.PageName(), "Post author should be as configuration");

    }

    @Test
    public void deletePost() throws InterruptedException {

        deleteMessage();

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