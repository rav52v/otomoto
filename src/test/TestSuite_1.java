package test;

import main.poms.ItemPage;
import main.poms.MainPage;
import main.tools.ConfigurationParser;
import main.utils.Driver;
import main.utils.Log;

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

        driver.getDriver().get("https://www.otomoto.pl/oferta/ford-focus-ford-focus-mk2-1-8-tdci-ghia-ID6BvTkE.html");

        ItemPage itemPage = new ItemPage();
        itemPage.fillParametersMap();














        driver.afterTest();
        log.logInfo("Driver is closed, program has finished.");
    }


}
