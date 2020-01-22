package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PagePost {

    private static WebElement element = null;

    public static WebElement date(WebDriver driver){
        element = driver.findElement(By.className("timestampContent"));
        return element;
    }

    public static WebElement author(WebDriver driver){
        element = driver.findElement(By.className("profileLink"));
        return element;
    }

    public static WebElement image(WebDriver driver){
        element = driver.findElement(By.className("uiScaledImageContainer"));
        return element;
    }

//    public static WebElement avatar(WebDriver driver){
//        element = driver.findElement(By.id("account_logout"));
//        return element;
//    }

    public static WebElement message(WebDriver driver){
        element = driver.findElement(By.xpath("//div[@data-testid='post_message']"));
        return element;
    }

    public static WebElement likes(WebDriver driver){
        element = driver.findElement(By.id("//div[@data-testid='UFI2ReactionsCount/root']"));
        return element;
    }

    public static WebElement privacy(WebDriver driver){
        element = driver.findElement(By.className("uiStreamPrivacy"));
        return element;
    }

    public static WebElement chat_close(WebDriver driver){
        element = driver.findElement(By.className("close"));
        return element;
    }

    public static WebElement postsWall(WebDriver driver){
        element = driver.findElement(By.id("pagelet_timeline_main_column"));
        return element;
    }
}
