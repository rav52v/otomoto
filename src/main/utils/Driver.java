package main.utils;

import main.tools.ConfigurationParser;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static WebDriver driver;
    private String path = new File("").toPath().toAbsolutePath().toString();
    private boolean headless;

    public WebDriver getDriver() {
        if (driver == null) {
            setProperties(new ConfigurationParser().getBrowserType());
            driver.manage().timeouts().implicitlyWait(new ConfigurationParser().getImplicitlyWaitTime(), TimeUnit.SECONDS);
            driver.get(new ConfigurationParser().getLinkAddress());
        }
        return driver;
    }

    private void closeDriver() {
        driver.quit();
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

    private void setProperties(String browserType) {
        switch (browserType) {
            case "chrome":
                switch (new ConfigurationParser().getSystem()) {
                    case "windows":
                        System.setProperty("webdriver.chrome.driver", path + "/chromedriver.exe");
                        break;
                    case "linux":
                        System.setProperty("webdriver.chrome.driver", path + "/chromedriver");
                        break;
                }

                ChromeOptions chromeOptions = new ChromeOptions();

                HashMap<String, Object> prefs = new HashMap<>();
                prefs.put("profile.managed_default_content_settings.images", 2);
                chromeOptions.setExperimentalOption("prefs", prefs);


//                String chromeProfile = "C:\\Users\\p_florys\\AppData\\Local\\Google\\Chrome\\User Data\\";
//                chromeOptions.addArguments("chrome.switches");
//                chromeOptions.addArguments("user-data-dir=" + chromeProfile);


//                options.addExtensions(new File("src/main/resources/adblock.crx"));
                
                chromeOptions.addArguments("--disable-boot-animation");
                chromeOptions.addArguments("--disable-2d-canvas-image-chromium");
                chromeOptions.addArguments("--disable-javascript-harmony-shipping");
                chromeOptions.addArguments("--default-wallpaper-is-oem");
                chromeOptions.addArguments("--disable-renderer-backgrounding");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--disable-infobars");

//                chromeOptions.addArguments("enable-automation");
                chromeOptions.addArguments("--disable-javascript");


                chromeOptions.addArguments("--disable-background-networking");
                chromeOptions.addArguments("--enable-fast-unload");
                chromeOptions.addArguments("--no-proxy-server");
                chromeOptions.addArguments("--sync-deferred-startup-timeout-seconds");
                chromeOptions.addArguments("--disable-timeouts-for-profiling");


                headless = new ConfigurationParser().getHeadless();
                chromeOptions.setHeadless(headless);
                if (headless) {
                    chromeOptions.addArguments("--window-size=1280,1024");
                    chromeOptions.addArguments("--disable-gpu");
                } else
                    chromeOptions.addArguments("--start-maximized");

                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                switch (new ConfigurationParser().getSystem()) {
                    case "windows":
                        System.setProperty("webdriver.gecko.driver", path + "/geckodriver.exe");
                        break;
                    case "linux":
                        System.setProperty("webdriver.gecko.driver", path + "/geckodriver");
                        break;
                }

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(new ConfigurationParser().getHeadless());
                firefoxOptions.addPreference("network.image.imageBehavior", 0);

                driver = new FirefoxDriver(firefoxOptions);

                break;
        }
    }
}