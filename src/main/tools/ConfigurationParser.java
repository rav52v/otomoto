package main.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationParser {
    private String propertiesFileName;
    private String linkAddress;
    private int implicitlyWaitTime;

    private Properties prop;

    public ConfigurationParser() {
        this.propertiesFileName = "config.properties";
        this.linkAddress = getParameterValue("linkAddress");
        this.implicitlyWaitTime = Integer.parseInt(getParameterValue("implicitlyWaitTime"));
    }

    public String getLinkAddress() {
        return this.linkAddress;
    }

    public int getimplicitlyWaitTime() {
        return this.implicitlyWaitTime;
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