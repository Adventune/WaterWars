package me.vilmu.waterwars.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQL {
	
	public static Connection conn;
	
	static String host, port, database, username, password;
	
	
	public static void connect() {
		
		
		host = "localhost";
		port = "3360";
		database = null;
		username = null;
		password = null;
		
		
		
		if(!isConnected()) {
			try {
				conn = DriverManager.getConnection("jdbc:mysql://"
		                + host + ":" + port + "/" + database,
		                username, password);
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
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