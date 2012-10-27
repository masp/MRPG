package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.data.items.MItemData;
import masp.plugins.mlight.enums.ItemType;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.material.MaterialData;

public class CustomItemConfiguration extends Configuration {

	public CustomItemConfiguration(MRPG plugin, File dir) {
		super("custom-items", plugin, dir);
	}

	@Override
	public void onCreate() {
		YamlConfiguration config = getConfig();
		config.set("custom-items.example-item.name", "Item Name");
		config.set("custom-items.example-item.type", "RING, CAPE, BELT, HELMET, CHESTPLATE, LEGGINGS, BOOT, EARRING, AMULET, BADGE, OTHER");
		config.set("custom-items.example-item.badge.player-title", ChatColor.RED.toString() + "[Red Title Here]");
		config.set("custom-items.example-item.badge.chat-title", ChatColor.RED + "[Red Chat Title Here]");
		config.set("custom-items.example-item.cape.url", "http://www.capeurlhere.com");
		config.set("custom-items.example-item.attack-effects.decimal.general_damage", "your general damage");
		config.set("custom-items.example-item.defense-effects.decimal.general_resistance", "your general resistance");
		
		try {
			config.save(getFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onRead() {
		YamlConfiguration config = getConfig();
		if (config.getConfigurationSection("custom-items") != null) {
			for (String itemName : config.getConfigurationSection("custom-items").getKeys(false)) {
				CustomItem item = getCustomItem(config.getString("custom-items." + itemName + ".name", ""));
				if (item != null) {
					String path = "custom-items." + itemName;
					ItemType type = ItemType.getItemType(config.getString(path + ".type", "other"));
					MItem mItem = new MItem(item.getCustomId(), (byte) 0, type, true);
					loadItem(mItem, itemName);
					MItemData data = mItem.getMData();
					if (type == ItemType.BADGE) {
						data.addString("badge-chat-title", config.getString(path + ".badge.chat-title", "[" + item.getName() + "]"));
						data.addString("badge-player-title", config.getString(path + ".badge.player-title", "[" + item.getName() + "]"));
					} else if (type == ItemType.CAPE) {
						data.addString("cape-url", config.getString(path + ".cape.url", ""));
					}
					MRPG.getItemManager().addItem(mItem);
				} else {
					MRPG.getInstance().getLogger().warning("Configuration Error: No custom item found by the name " + config.getString("custom-items." + itemName + ".name"));
				}
			}
		}
	}
	
	public CustomItem getCustomItem(String name) {
		for (CustomItem item : MaterialData.getCustomItems()) {
			if (item.getName().equalsIgnoreCase(name)) {
				return item;
			}
		}
		return null;
	}
	
	public void loadItem(MItem item, String name) {
		if (getConfig().getConfigurationSection("custom-items." + name + ".attack-effects") != null) {
			super.loadEffects(item.getAttack(), "custom-items." + name + ".attack-effects");
		}
		
		if (getConfig().getConfigurationSection("custom-items." + name + ".defense-effects") != null) {
			super.loadEffects(item.getAttack(), "custom-items." + name + ".defense-effects");
		}
	}

}
