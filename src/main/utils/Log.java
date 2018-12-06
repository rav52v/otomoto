package main.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private final Logger consoleLog = LogManager.getLogger("Console");

    public void logInfo(String info) {
        consoleLog.info(info);
    }
}
