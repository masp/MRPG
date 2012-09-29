package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MRPG;

public class DatabaseConfiguration extends Configuration {
	
	private String server;
	private String port;
	private String database;
	private String username;
	private String password;
	
	private boolean isMySQLEnabled = false;
	
	public DatabaseConfiguration(MRPG plugin, File dir) {
		super("database", plugin, dir);
	}

	@Override
	public void onCreate() {
		getConfig().set("server", "127.0.0.1");
		getConfig().set("port",  "3306");
		getConfig().set("database", "mrpg");
		getConfig().set("username", "root");
		getConfig().set("password", "password");
		
		getConfig().set("mysql-enabled", false);
		
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRead() {
		this.server = getConfig().getString("server", "127.0.0.1");
		this.port = getConfig().getString("port", "");
		this.database = getConfig().getString("database", "mrpg");
		this.username = getConfig().getString("username", "root");
		this.password = getConfig().getString("password", "password");
		
		this.isMySQLEnabled = getConfig().getBoolean("mysql-enabled", false);
	}
	
	public String getServer() {
		return server;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getDatabase() {
		return database == null ? "mrpg" : database;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean isMySQLEnabled() {
		return isMySQLEnabled;
	}
	
}
