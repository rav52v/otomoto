package main.tools;

import java.io.*;
import java.util.Properties;

public class ConfigurationParser {
    private InputStream inputStream;
    private Properties prop;
    private String path;
    private String propertiesFileName;
    private String linkAddress;
    private int implicitlyWaitTime;
    private int pageLoadTime;
    private int maxExistingOffers;
    private String searchLinkAddress;
    private String system;

    private String browserType;
    private String headless;

    private String receiverEmail;
    private String senderEmail;
    private String emailLogin;
    private String emailPassword;
    private String sendReport;

    private String conUrl;
    private String sqlLogin;
    private String sqlPassword;

    public ConfigurationParser() {
        this.propertiesFileName = "config.properties";
        prop = new Properties();
        path = new File("").toPath().toAbsolutePath().toString().concat("/config.properties");
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error when processing config file " + propertiesFileName, e);
        }

        this.linkAddress = getParameterValue("linkAddress");
        this.implicitlyWaitTime = Integer.parseInt(getParameterValue("implicitlyWaitTime"));
        this.pageLoadTime = Integer.parseInt(getParameterValue("pageLoadTime"));
        this.maxExistingOffers = Integer.parseInt(getParameterValue("maxExistingOffers"));
        this.searchLinkAddress = getParameterValue("searchLinkAddress");
        this.system = getParameterValue("system");

        this.browserType = getParameterValue("browserType");
        this.headless = getParameterValue("headless");

        this.receiverEmail = getParameterValue("receiverEmail");
        this.senderEmail = getParameterValue("senderEmail");
        this.emailLogin = getParameterValue("emailLogin");
        this.emailPassword = getParameterValue("emailPassword");
        this.sendReport = getParameterValue("sendReport");

        this.conUrl = getParameterValue("conUrl");
        this.sqlLogin = getParameterValue("sqlLogin");
        this.sqlPassword = getParameterValue("sqlPassword");

    }

    private String getParameterValue(String name){
        return prop.getProperty(name);
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

    public boolean getHeadless(){
        return Boolean.parseBoolean(this.headless);
    }

    public String getReceiverEmail() {
        return this.receiverEmail;
    }

    public String getSenderEmail() {
        return this.senderEmail;
    }

    public String getEmailLogin() {
        return this.emailLogin;
    }

    public String getEmailPassword() {
        return this.emailPassword;
    }

    public boolean getSendReport(){
        return Boolean.parseBoolean(this.sendReport);
    }

    public String getConUrl() {
        return this.conUrl;
    }

    public String getSqlLogin() {
        return this.sqlLogin;
    }

    public String getSqlPassword() {
        return this.sqlPassword;
    }

    public int getMaxExistingOffers() {
        return maxExistingOffers;
    }

    public String getBrowserType() {
        return this.browserType;
    }

    public int getPageLoadTime() {
        return this.pageLoadTime;
    }
}