package test;

import main.tools.ConfigurationParser;
import main.utils.Driver;

import java.util.concurrent.TimeUnit;

public class TestBase extends Driver {

    public void beforeTest() {
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(new ConfigurationParser().getimplicitlyWaitTime(), TimeUnit.SECONDS);
    }


    public void afterTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {/*EXCEPTION IGNORED*/}
        closeDriver();
    }

}
