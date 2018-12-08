package test;

import main.poms.ItemPage;
import main.poms.MainPage;
import main.poms.SearchPage;
import main.tools.ConfigurationParser;
import main.utils.Driver;
import main.utils.Log;

public class TestSuite_1 {

    public static void main(String[] args) {
        Log log = new Log();
        log.logInfo("Log created");

        ConfigurationParser config = new ConfigurationParser();
        log.logInfo("Config created");

        Long start = System.currentTimeMillis();
        Driver driver = new Driver();
        log.logInfo("Opened new driver");
        driver.beforeTest();
        log.logInfo("Driver created, starts with {" + config.getLinkAddress() + "}");
        log.logInfo("Driver opened, and loaded in {" + (System.currentTimeMillis()-start) + " milliseconds}");

        new MainPage();
        log.logInfo("Clicked accept cookies button");


        driver.getDriver().get(config.getSearchLinkAddress());
        log.logInfo("Navigated to filtered search page {" + config.getSearchLinkAddress() + "}");

        log.logInfo("Mapping offer ids started...");
        start = System.currentTimeMillis();
        SearchPage searchPage = new SearchPage();
        log.logInfo("Mapped {" + searchPage.getMappedOffersSize() + "} offers, it took {" + (System.currentTimeMillis()-start)/60000
                + " minutes}, which is 1 offer per {" + ((System.currentTimeMillis()-start)/searchPage.getMappedOffersSize()) + "} milliseconds");

        driver.getDriver().get("https://www.otomoto.pl/oferta/renault-captur-renault-captur-zen-tce-90-pojazd-demonstracyjny-ID6AU86r.html");

        ItemPage itemPage = new ItemPage();













        driver.afterTest(0);
        log.logInfo("Driver is closed, program has finished.");
    }


}
