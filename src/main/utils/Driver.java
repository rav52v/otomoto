package main.utils;

import main.tools.ConfigurationParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static WebDriver driver;

    private void setupDriver() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
            HashMap<String, Object> images = new HashMap<>();
            images.put("images", 2);

            HashMap<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);
            driver = new ChromeDriver(options);
            driver.get(new ConfigurationParser().getLinkAddress());
        }
    }

    public WebDriver getDriver() {
        setupDriver();
        return driver;
    }

    private void closeDriver() {
        driver.close();
        driver = null;
    }

    public void beforeTest() {
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(new ConfigurationParser().getImplicitlyWaitTime(), TimeUnit.SECONDS);
    }


    public void afterTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {/*EXCEPTION IGNORED*/}
        closeDriver();
    }
}