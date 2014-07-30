package org.netapp.epg.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	protected static Connection connect = null;
	protected static Statement statement = null;
	protected static PreparedStatement preparedStatement = null;
	protected static ResultSet resultSet = null;
	protected String driveclass="";

	public boolean createConnection(String url, String username,
			String password) {
		// this will load the MySQL driver, each DB has its own driver
		// setup the connection with the DB.
		try {
			Class.forName(driveclass);
			connect = DriverManager
					.getConnection(url,username,password);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
