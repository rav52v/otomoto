package test;

import main.poms.ItemPage;
import main.poms.MainPage;
import main.poms.SearchPage;
import main.tools.ConfigurationParser;
import main.utils.Driver;
import main.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class TestSuite_1 {

    public static void main(String[] args) {
        Log log = new Log();
        log.logInfo("Log created");

        ConfigurationParser config = new ConfigurationParser();
        log.logInfo("Config created");

        Driver driver = new Driver();
        log.logInfo("Driver created, starts with {" + config.getLinkAddress() + "}");

        driver.beforeTest();
        log.logInfo("Opened new driver");

        MainPage mainPage = new MainPage();
        mainPage.acceptCookiesClick();
        log.logInfo("Clicked accept cookies button");

//        driver.getDriver().get(config.getSearchLinkAddress());
//        log.logInfo("Navigated to filtered search page {" + config.getSearchLinkAddress() + "}");
//
//        SearchPage searchPage = new SearchPage();
//        searchPage.mapAllOffers();
//        log.logInfo("Mapped all ids with their links");

        driver.getDriver().get("https://www.otomoto.pl/oferta/chrysler-grand-voyager-grand-voyager-2-8-crd-7-miejsc-navi-dvd-webasto-komplet-kol-ID6BuQWs.html#de0c1237ce");

        ItemPage itemPage = new ItemPage();
        System.out.println(itemPage.getDateOfIssue());













        driver.afterTest();
        log.logInfo("Driver is closed, program has finished.");
    }


}
