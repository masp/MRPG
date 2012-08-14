package masp.plugins.mlight.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends Database {

	private String database;
	
	public SQLite(String database) {
		try {
			this.database = database;
			File dataFile = new File(database + ".db");
			if (!dataFile.getParentFile().exists()) {
				dataFile.getParentFile().mkdirs();
			}
			Class.forName("org.sqlite.JDBC");
			DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
		} catch (SQLException ex) {
			System.out.println("An error occurred in retreiving the connection for SQLite.");
			ex.printStackTrace();
		}
		return null;
	}

}
