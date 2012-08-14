package masp.plugins.mlight.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends Database {
	
	 private String connectionString; 

	public MySQL(String server, String port, String database, String user, String password) {
		try {
			connectionString = "jdbc:mysql://" + server + ":" + port + "/" + database + "?user=" + user + "&password=" + password;
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.getConnection(connectionString);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getLocalizedMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getLocalizedMessage());
		}
	}

	@Override
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			System.out.println("Error occurred in trying to connect database.");
		}
		return null;
	}

}
