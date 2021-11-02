package me.vilmu.waterwars.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Ranks {
	
	public static Connection conn;
	
	static String host, port, database, username, password;
	
	
	public static void connect() throws SQLException{
		
		
		host = "172.18.0.1";
		database = "multi-survivals";
		port = "25596";
		username = "multi-survivals";
		password = "UK7tm7U72WXYVrpp";
		
		
		
		if(!isConnected()) {
			conn = DriverManager.getConnection("jdbc:mysql://"
	                + host + ":" + port + "/" + database,
	                username, password);
				
		}
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				conn.close();
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static boolean isConnected() {
		try {
			if((conn == null) || (conn.isClosed()) || (conn.isValid(5))) {
				return false;
			}
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Connection getConnection() {
		return conn;
	}
}