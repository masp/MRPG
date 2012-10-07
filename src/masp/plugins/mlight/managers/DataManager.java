package masp.plugins.mlight.managers;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.database.Database;
import masp.plugins.mlight.database.MySQL;
import masp.plugins.mlight.database.SQLite;

public class DataManager {
	
	private Database database;
	private MRPG plugin;
	
	public DataManager(MRPG plugin) {
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
					"CREATE TABLE IF NOT EXISTS `mrpg_attributes` (" +
					"`s_id` PRIMARY KEY," +
					"`p_id` INTEGER NOT NULL," +
					"`att_name` TEXT NOT NULL," +
					"`amount` INTEGER NOT NULL DEFAULT '0.0'," +
					"UNIQUE (att_name, p_id) ON CONFLICT REPLACE);");
		} else {
			database.execute(conn, 
					"CREATE TABLE IF NOT EXISTS `players` (" +
					"`id` INT(6) PRIMARY_KEY AUTO_INCREMENT," +
					"`owner` VARCHAR(255) NOT NULL DEFAULT ''," +
					"`exp` DECIMAL(18, 4) NOT NULL DEFAULT '0'," +
					"`health` INT(6) NOT NULL," +
					"`max_health` INT(12) NOT NULL," + 
					"`skill_points` INT(12) NOT NULL DEFAULT '0');");
			database.execute(conn,
					"CREATE TABLE IF NOT EXISTS `mrpg_attributes` (" +
					"`s_id` PRIMARY_KEY AUTO_INCREMENT," +
					"`p_id` INT(15) NOT NULL," +
					"`att_name` VARCHAR(255) NOT NULL DEFAULT ''," +
					"`amount` FLOAT(9, 2) NOT NULL DEFAULT '0.0'," +
					"UNIQUE (att_name, p_id) ON CONFLICT REPLACE);");
		}
	}
	
	public void savePlayer(MPlayer player) throws SQLException {
		Connection conn = database.getConnection();
		System.out.println("REACHED");
		int id = getPlayerId(player.getName(), conn);
		if (id == 0) {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO `players` (owner, exp, skill_points, health, max_health) VALUES (?, ?, ?, ?, ?);");
			stmt.setString(1, player.getName());
			stmt.setDouble(2, player.getExperience());
			stmt.setInt(3, player.getSkillPoints());
			stmt.setInt(4, player.getHealth());
			stmt.setInt(5, player.getMaxHealth());
			stmt.execute();
			stmt.close();
			
			id = getPlayerId(player.getName(), conn);
			
		} else {
			PreparedStatement stmt = conn.prepareStatement(
					"UPDATE `players` SET exp=?, skill_points=?, health=?, max_health=? WHERE id=" + id + ";");
			stmt.setDouble(1, player.getExperience());
			stmt.setInt(2, player.getSkillPoints());
			stmt.setInt(3, player.getHealth());
			stmt.setInt(4, player.getMaxHealth());
			stmt.executeUpdate();
			stmt.close();
		}
		
		for (Attribute skill : MRPG.getAttributeManager().getSkills()) {
			System.out.println("REACHED SKILL " + skill.getName());
			PreparedStatement skillStatement = conn.prepareStatement(
					"INSERT INTO `mrpg_attributes` (p_id, att_name, amount) VALUES (?, ?, ?);");
			skillStatement.setInt(1, id);
			skillStatement.setString(2, skill.getName());
			skillStatement.setFloat(3, player.getSkillValue(skill.getName()));
			skillStatement.execute();
			skillStatement.close();
		}
		conn.close();
	}
	
	public MPlayer loadPlayer(MPlayer mPlayer) throws SQLException {
		Connection conn = database.getConnection();
		int id = getPlayerId(mPlayer.getName(), conn);
		if (id == 0) {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO `players` (owner, exp, skill_points, health, max_health) VALUES (?, ?, ?, ?, ?);");
			stmt.setString(1, mPlayer.getName());
			stmt.setDouble(2, 0);
			stmt.setInt(3, Settings.DEFAULT_SKILL_POINTS);
			stmt.setInt(4, Settings.DEFAULT_MAX_HEALTH);
			stmt.setInt(5, Settings.DEFAULT_MAX_HEALTH);
			stmt.execute();
			stmt.close();
			
			id = getPlayerId(mPlayer.getName(), conn);
		}
		
		PreparedStatement stmt = conn.prepareStatement(
				"SELECT exp,skill_points,health,max_health FROM `players` WHERE id=" + id);
		ResultSet set = stmt.executeQuery();
		while (set.next()) {
			mPlayer.setExperience(set.getDouble("exp"));
			mPlayer.setSkillPoints(set.getInt("skill_points"));
			mPlayer.setHealth(set.getInt("health"));
			mPlayer.setMaxHealth(set.getInt("max_health"));
		}
		set.close();
		stmt.close();
		
		for (Attribute skill : MRPG.getAttributeManager().getSkills()) {
			PreparedStatement skillStmt = conn.prepareStatement(
					"SELECT amount FROM `mrpg_attributes` WHERE p_id=" + id + " AND att_name='" + skill.getName() + "';");
			ResultSet skillSet = skillStmt.executeQuery();
			while (skillSet.next()) {
				mPlayer.setSkillValue(skill, skillSet.getInt("amount"));
			}
			skillSet.close();
			skillStmt.close();
		}
		
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
