package test;

import main.poms.ItemPage;
import main.poms.MainPage;
import main.poms.SearchPage;
import main.tools.ConfigurationParser;
import main.tools.DataBaseReader;
import main.tools.Email;
import main.utils.Driver;
import main.utils.Log;

import javax.mail.Session;

public class TestSuite_1 {

    public static void main(String[] args) {
        Log log = new Log();
        log.logInfo("Log created");
        long programTime = System.currentTimeMillis();

        ConfigurationParser config = new ConfigurationParser();
        log.logInfo("Config created");

        DataBaseReader dataBase = new DataBaseReader();
        log.logInfo("Connected to database");

        long start = System.currentTimeMillis();
        log.logInfo("Opening new driver...");
        Driver driver = new Driver();
        driver.beforeTest();
        log.logInfo("Driver created, starts with {" + config.getLinkAddress() + "}");
        log.logInfo("Driver opened, and loaded in {" + (System.currentTimeMillis() - start) + " milliseconds}");

        new MainPage();
        log.logInfo("Clicked accept cookies button");

        driver.getDriver().get(config.getSearchLinkAddress());
        log.logInfo("Navigated to filtered search page {" + config.getSearchLinkAddress() + "}");

        start = System.currentTimeMillis();
        log.logInfo("Mapping offer ids started...");
        SearchPage searchPage = new SearchPage();
        searchPage.mapAllOffers();
        log.logInfo("Mapped {" + searchPage.getMappedOffersSize() + "} offers, it took {" + (System.currentTimeMillis() - start) / 60000
                + " minutes}, which is 1 offer per {" + ((System.currentTimeMillis() - start) / searchPage.getMappedOffersSize()) + "} milliseconds");

        ItemPage itemPage = new ItemPage();
        itemPage.openMultipleOffersAndSendDataToDataBase();

        driver.afterTest(0);
        log.logInfo("Driver is closed, program has finished.");
        log.logInfo("Total program working time was {" + (System.currentTimeMillis() - programTime) / 60000 + " minutes}.");

        new Email().sendEmail("Automatically generated otomoto report {" + log.getActualTime() + "}", log.getLog());
        log.logInfo("An email with a report has been sent to {" + config.getReceiverEmail() + "}.");
    }
}
