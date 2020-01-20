package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private static WebElement element = null;

    public static WebElement input_email(WebDriver driver){
        element =  driver.findElement(By.name("email"));
        return element;
    }

    public static WebElement input_password(WebDriver driver){
        element =  driver.findElement(By.name("pass"));
        return element;
    }

    public static WebElement btn_login(WebDriver driver){
        element =  driver.findElement(By.id("loginbutton"));
        return element;
    }

}
