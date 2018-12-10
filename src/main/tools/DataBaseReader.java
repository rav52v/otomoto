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

    public DataBaseReader() {
        conUrl = "jdbc:mysql://185.28.102.242/otomoto5?useUnicode=true&characterEncoding=utf8";
        login = "otomoto9";
        pass = "9rmvqx5nympfviy";
        log = new Log();

        try {
            con = DriverManager.getConnection(conUrl, login, pass);
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfOfferIdExist(String offerId) {
        String query = "SELECT offerId FROM otomoto WHERE id = ?";
        ResultSet rs;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, offerId);
            rs = pstmt.executeQuery();
            return rs.getBoolean(1);
        } catch (SQLException e) {/*Exception Ignored*/}
        return false;
    }

    public Map<String, String> cleanMapFromExistingRecords(Map<String, String> mapToClean) {
        log.logInfo("Map filtering from existing records...");
        Map<String, String> cleanedMap = new HashMap<>();
        long start = System.currentTimeMillis();
        for (String x : mapToClean.keySet()){
            if(!checkIfOfferIdExist(x)){
                cleanedMap.put(x, mapToClean.get(x));
            }
        }
        log.logInfo("Database already contains {" + (mapToClean.size() - cleanedMap.size()) + "}, map with offers now contains {"
                + cleanedMap.size() + "}, operations took {" + (System.currentTimeMillis() - start)/1000 + " seconds}");
            return cleanedMap;
    }

    public void executeQuery(String query){
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException e) {/*Exception Ignored*/}
    }

}
