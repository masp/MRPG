package masp.plugins.mlight.utils;

import java.util.HashMap;

import masp.plugins.mlight.Settings;
import net.minecraft.server.IInventory;

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
	
	public static void debugInventory(IInventory inv) {
		debug("Contents: ");
		for (int i = 0; i < inv.getContents().length; i++) {
			net.minecraft.server.ItemStack item = inv.getContents()[i];
			if (item == null) continue;
			debug("\t" + item.getItem().getName() + ":");
			debug("\t\t" + "Count: " + item.count);
			debug("\t\t" + "Index: " + i);
		}
	}
	
}
