package utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    Properties pro;
    public ConfigReader()
    {
        try {
            File source = new File ("config.properties");
            FileInputStream input = new FileInputStream(source);
            pro = new Properties();
            pro.load(input);
        } catch (Exception exp) {
            System.out.println("Exception is: ---" + exp.getMessage());
        }
    }
    public String getChromePath()
    {
        String path = pro.getProperty("ChromeDriver");
        return path;
    }

    public String URL() { return pro.getProperty("URL"); }

    public String WebTitle() { return pro.getProperty("WebTitle"); }

    public String Username() { return pro.getProperty("Username"); }

    public String Password() { return pro.getProperty("Password"); }

    public String GraphURL() { return pro.getProperty("GraphURL"); }

    public String UserID() { return pro.getProperty("UserID"); }

    public String AppID() { return pro.getProperty("AppID"); }

    public String AppToken() { return pro.getProperty("AppToken"); }

    public String PageID() { return pro.getProperty("PageID"); }

    public String PageAccessToken() { return pro.getProperty("PageAccessToken"); }

    public String PageName() { return pro.getProperty("PageName"); }



}