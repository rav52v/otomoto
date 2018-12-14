package main.tools;

import main.utils.Log;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataBaseReader {

    private Connection con;
    private String conUrl;
    private String pass;
    private String login;
    private Log log;
    private ConfigurationParser config;
    private static Map<String, String> cleanedMap;

    public DataBaseReader() {
        config = new ConfigurationParser();
        conUrl = config.getConUrl();
        login = config.getSqlLogin();
        pass = config.getSqlPassword();
        log = new Log();

        try {
            con = DriverManager.getConnection(conUrl, login, pass);
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfOfferIdExist(String offerId) {
        String query = "SELECT offerId FROM otomoto WHERE offerId = ?";
        ResultSet rs;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, offerId);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {/*Exception Ignored*/}
        return false;
    }

    public void cleanMapFromExistingRecords(Map<String, String> mapToClean) {
        cleanedMap = new HashMap<>();
        long start = System.currentTimeMillis();
        for (String x : mapToClean.keySet()){
            if(!checkIfOfferIdExist(x)){
                cleanedMap.put(x, mapToClean.get(x));
            }
        }
        log.logInfo("Database already contains {" + (mapToClean.size() - cleanedMap.size()) + "}, map with offers now contains {"
                + cleanedMap.size() + "}, operation took {" + (System.currentTimeMillis() - start)/1000 + " seconds}");
    }

    public void executeQuery(String query){
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException e) {/*Exception Ignored*/}
    }

    public Map<String, String> getCleanedMap(){
        return cleanedMap;
    }

}
