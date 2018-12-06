package main.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class DataBaseReader {
	
	private Connection con;
	private final String CON_URL = "jdbc:sqlite:src//test//resources//My_Own_DataBase.db";
	
	public DataBaseReader() {
		try {
			con = DriverManager.getConnection(CON_URL);
			con.setAutoCommit(true);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public String getCity() {
		String query = "SELECT city FROM Cities WHERE id = ?";
		ResultSet rs;
		
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, new Random().nextInt(10)+1);
			rs = pstmt.executeQuery();
			return rs.getString(1);	
		}
		catch (SQLException e){/*Exception Ignored*/}
		return null;
	}
	
}
