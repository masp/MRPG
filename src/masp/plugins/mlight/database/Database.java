package masp.plugins.mlight.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

	public ResultSet query(Connection conn, String query) {
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeQuery(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void execute(Connection conn, String sql) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(Connection conn, String sql) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public abstract Connection getConnection();
	
}
