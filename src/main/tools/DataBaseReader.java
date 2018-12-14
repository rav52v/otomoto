package main.tools;

import main.utils.Log;

import java.sql.*;
import java.util.Map;

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

    public boolean checkIfOfferIdExist(String offerId) {
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

    public void executeQuery(String query) {
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException e) {/*Exception Ignored*/}
    }
}
