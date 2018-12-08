package main.utils;

import main.tools.ConfigurationParser;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static WebDriver driver;

    public WebDriver getDriver() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
            HashMap<String, Object> images = new HashMap<>();
            images.put("images", 2);
            HashMap<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("start-maximized");
            options.addArguments("disable-boot-animation");
            options.addArguments("--disable-2d-canvas-image-chromium");
            options.addArguments("--disable-javascript-harmony-shipping");
            options.addArguments("--default-wallpaper-is-oem");
            options.addArguments("--fast-start");
            options.addArguments("--disable-renderer-backgrounding");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--incognito");
            options.addArguments("--disable-infobars");
            options.addArguments("--headless"); /// !!
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-gpu-sandbox");
            options.addArguments("--disable-gpu-program-cache");
            options.addArguments("--disable-gpu-watchdog");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);


            driver = new ChromeDriver(options);
            driver.get(new ConfigurationParser().getLinkAddress());
        }
        return driver;
    }

    private void closeDriver() {
        driver.close();
        driver = null;
    }

    public void beforeTest() {
        getDriver().manage().timeouts().implicitlyWait(new ConfigurationParser().getImplicitlyWaitTime(), TimeUnit.SECONDS);
    }


    public void afterTest(int sleepAfter) {
        try {
            Thread.sleep(sleepAfter);
        } catch (InterruptedException e) {/*EXCEPTION IGNORED*/}
        closeDriver();
    }
}