package main.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
    private static String logKeeper = "";
    private final Logger consoleLog = LogManager.getLogger("Console");
    private SimpleDateFormat time;
    private Calendar cal;

    public void logInfo(String info) {
        logKeeper += getActualTime() + " INFO  " + info.concat("\n");
        consoleLog.info(info);
    }

    public String getLog() {
        return logKeeper;
    }

    public String getActualTime(){
        cal = Calendar.getInstance();
        time = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss,SSS");
        return time.format(cal.getTime());
    }
}
