package test;

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
        log.logInfo("Driver created {" + config.getLinkAddress() + "}");

        TestBase base = new TestBase();
        base.beforeTest();
        log.logInfo("Opened new driver");















        base.afterTest();
        log.logInfo("Driver is closed, program has finished.");
    }


}
