package utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private Properties prop;
    public ConfigReader()
    {
        try {
            File source = new File ("config.properties");
            FileInputStream input = new FileInputStream(source);
            prop = new Properties();
            prop.load(input);
        } catch (Exception exp) {
            System.out.println("Exception is: ---" + exp.getMessage());
        }
    }
    public String getChromePath()
    {
        return prop.getProperty("ChromeDriver");
    }

    public String URL() { return prop.getProperty("URL"); }

    public String WebTitle() { return prop.getProperty("WebTitle"); }

    public String Username() { return prop.getProperty("Username"); }

    public String Password() { return prop.getProperty("Password"); }

    public String GraphURL() { return prop.getProperty("GraphURL"); }

    public String UserID() { return prop.getProperty("UserID"); }

    public String AppID() { return prop.getProperty("AppID"); }

    public String AppToken() { return prop.getProperty("AppToken"); }

    public String PageID() { return prop.getProperty("PageID"); }

    public String PageAccessToken() { return prop.getProperty("PageAccessToken"); }

    public String PageName() { return prop.getProperty("PageName"); }



}