package main.utils;

import main.tools.ConfigurationParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Driver {
    private static WebDriver driver;

    private void setupDriver() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.get(new ConfigurationParser().getLinkAddress());
        }
    }

    public WebDriver getDriver() {
        setupDriver();
        return driver;
    }

    public void closeDriver() {
        driver.close();
        driver = null;
    }

    public void beforeTest() {
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(new ConfigurationParser().getImplicitlyWaitTime(), TimeUnit.SECONDS);
    }


    public void afterTest() {
        try {
            Thread.sleep(3099900);
        } catch (InterruptedException e) {/*EXCEPTION IGNORED*/}
        closeDriver();
    }
}