package masp.plugins.mlight.managers;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import masp.plugins.mlight.MLight;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.database.Database;
import masp.plugins.mlight.database.MySQL;
import masp.plugins.mlight.database.SQLite;

import org.bukkit.entity.Player;

public class DataManager {
	
	private Database database;
	private MLight plugin;
	
	public DataManager(MLight plugin) {
		this.plugin = plugin;
	}
	
	public void init() {
		if (plugin.getDataConfig().isMySQLEnabled()) {
			this.database = new MySQL(
					plugin.getDataConfig().getServer(),
					plugin.getDataConfig().getPort(),
					plugin.getDataConfig().getDatabase(),
					plugin.getDataConfig().getUsername(),
					plugin.getDataConfig().getPassword());
		} else {
			this.database = new SQLite(plugin.getDataFolder() 
					+ File.separator 
					+ plugin.getDataConfig().getDatabase());
		}
		
		this.initTables();
	}
	
	public void initTables() {
		Connection conn = database.getConnection();
		if (database instanceof SQLite) {
			database.execute(conn, 
					"CREATE TABLE IF NOT EXISTS `players` (" +
					"`id` INTEGER PRIMARY KEY," +
					"`owner` TEXT NOT NULL," +
					"`exp` REAL NOT NULL DEFAULT '0'," +
					"`health` INTEGER NOT NULL," + 
					"`max_health` INTEGER NOT NULL," +
					"`skill_points` INTEGER NOT NULL DEFAULT '0');");	
			database.execute(conn, 
					"CREATE TABLE IF NOT EXISTS `player_skills` (" +
					"`s_id` INTEGER PRIMARY KEY NOT NULL," +
					"`player_id` INTEGER NOT NULL," +
					"`skill_name` TEXT NOT NULL DEFAULT ''," +
					"`amount` INTEGER NOT NULL DEFAULT '1');");
		} else {
			database.execute(conn, 
					"CREATE TABLE IF NOT EXISTS `players` (" +
					"`id` INT PRIMARY_KEY AUTO_INCREMENT," +
					"`owner` VARCHAR(255) NOT NULL DEFAULT ''," +
					"`exp` DECIMAL(18, 4) NOT NULL DEFAULT '0'," +
					"`health` INT NOT NULL," +
					"`max_health` INT NOT NULL," + 
					"`skill_points` INT NOT NULL DEFAULT '0');");
			database.execute(conn, 
					"CREATE TABLE IF NOT EXISTS `player_skills` (" +
					"`s_id` INT PRIMARY_KEY AUTO_INCREMENT NOT NULL," +
					"`player_id` INT NOT NULL," +
					"`skill_name` VARCHAR(255) PRIMARY_KEY NOT NULL DEFAULT ''," +
					"`amount` INT NOT NULL DEFAULT '1');");
		}
	}
	
	public void savePlayer(MPlayer player) throws SQLException {
		Connection conn = database.getConnection();
		int id = getPlayerId(player.getName(), conn);
		if (id == 0) {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO `players` (owner, exp, skill_points) VALUES (?, ?, ?);");
			stmt.setString(1, player.getName());
			stmt.setDouble(2, player.getExperience());
			stmt.setInt(3, player.getSkillPoints());
			stmt.execute();
			stmt.close();
		} else {
			PreparedStatement stmt = conn.prepareStatement(
					"UPDATE `players` SET exp=?, skill_points=? WHERE id=" + id + ";");
			stmt.setDouble(1, player.getExperience());
			stmt.setInt(2, player.getSkillPoints());
			stmt.executeUpdate();
			stmt.close();
		}
		conn.close();
	}
	
	public MPlayer loadPlayer(Player player) throws SQLException {
		Connection conn = database.getConnection();
		int id = getPlayerId(player.getName(), conn);
		if (id == 0) {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO `players` (owner, exp, skill_points, health, max_health) VALUES (?, ?, ?, ?, ?);");
			stmt.setString(1, player.getName());
			stmt.setDouble(2, 0);
			stmt.setInt(3, Settings.DEFAULT_SKILL_POINTS);
			stmt.setInt(4, Settings.DEFAULT_MAX_HEALTH);
			stmt.setInt(5, Settings.DEFAULT_MAX_HEALTH);
			stmt.execute();
			stmt.close();
		}
		MPlayer mPlayer = new MPlayer(player);
		PreparedStatement stmt = conn.prepareStatement(
				"SELECT exp,skill_points FROM `players` WHERE id=" + id);
		ResultSet set = stmt.executeQuery();
		while (set.next()) {
			mPlayer.setExperience(set.getDouble("exp"));
			mPlayer.setSkillPoints(set.getInt("skill_points"));
		}
		set.close();
		stmt.close();
		conn.close();
		return mPlayer;
	}
	
	public int getPlayerId(String name, Connection conn) throws SQLException {
		ResultSet set = database.query(conn, 
				"SELECT `id` FROM `players` WHERE `owner`='" + name + "';");
		while (set.next()) {
			return set.getInt("id");
		}
		return 0;
	}
	
}
