package main.tools; //place this file in same dir as runnable jar / project dir

import java.io.*;
import java.util.Properties;

public class ConfigurationParser {
    private InputStream inputStream;
    private String propertiesFileName;
    private String linkAddress;
    private int implicitlyWaitTime;
    private String searchLinkAddress;
    private String system;
    private String headless;

    private Properties prop;

    public ConfigurationParser() {
        this.propertiesFileName = "config.properties";
        this.linkAddress = getParameterValue("linkAddress");
        this.implicitlyWaitTime = Integer.parseInt(getParameterValue("implicitlyWaitTime"));
        this.searchLinkAddress = getParameterValue("searchLinkAddress");
        this.system = getParameterValue("system");
        this.headless = getParameterValue("headless");
    }

    public String getLinkAddress() {
        return this.linkAddress;
    }

    public int getImplicitlyWaitTime() {
        return this.implicitlyWaitTime;
    }

    public String getSearchLinkAddress() {
        return this.searchLinkAddress;
    }

    public String getSystem(){
        return this.system;
    }

    public String getHeadless(){
        return this.headless;
    }

    private String getParameterValue(String name){
        prop = new Properties();
        String path = new File("").toPath().toAbsolutePath().toString();
        try {
            inputStream = new FileInputStream(path + "\\config.properties");
        } catch (FileNotFoundException e) {
            try {
                inputStream = new FileInputStream(path + "/config.properties");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error when processing config file " + propertiesFileName, e);
        }
        return prop.getProperty(name);
    }
}