package main.utils;

import main.tools.ConfigurationParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static WebDriver driver;
    private String path = new File("").toPath().toAbsolutePath().toString();

    public WebDriver getDriver() {
        if (driver == null) {
            switch(new ConfigurationParser().getSystem()){
                case "windows":
                    System.setProperty("webdriver.chrome.driver", path + "/chromedriver.exe");
                    break;
                case "linux":
                    System.setProperty("webdriver.chrome.driver", path + "/chromedriver");
                    break;
            }

            ChromeOptions options = new ChromeOptions();
            HashMap<String, Object> images = new HashMap<>();
            images.put("images", 2);
            HashMap<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            options.setExperimentalOption("prefs", prefs);

            //adblock:
//            options.addExtensions(new File("src\\main\\resources\\adblock.crx"));

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
            boolean headless = new ConfigurationParser().getHeadless();
            options.setHeadless(headless);
            options.addArguments("--disable-gpu");
            if(headless){
                options.addArguments("--window-size=1280,1024");
            }

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