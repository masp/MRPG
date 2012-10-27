package masp.plugins.mlight.utils;

import java.util.HashMap;

import masp.plugins.mlight.Settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Debugger {
	
	public static void debug(String text) {
		if (Settings.DEBUG) {
			Bukkit.getConsoleSender().sendMessage("[DEBUG] " + ChatColor.RED + text);
		}
	}
	
	public static void debugHashMap(HashMap<?, ?> map) {
		for (Object key : map.keySet()) {
			debug(key.toString() + ": " + map.get(key).toString());
		}
	}
	
}
