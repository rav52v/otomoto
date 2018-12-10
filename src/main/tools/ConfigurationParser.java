package main.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationParser {
    private String propertiesFileName;
    private String linkAddress;
    private int implicitlyWaitTime;
    private String searchLinkAddress;
    private String system;

    private Properties prop;

    public ConfigurationParser() {
        this.propertiesFileName = "config.properties";
        this.linkAddress = getParameterValue("linkAddress");
        this.implicitlyWaitTime = Integer.parseInt(getParameterValue("implicitlyWaitTime"));
        this.searchLinkAddress = getParameterValue("searchLinkAddress");
        this.system = getParameterValue("system");
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

    private String getParameterValue(String name){
        prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error when processing config file " + propertiesFileName, e);
        }
        return prop.getProperty(name);
    }
}