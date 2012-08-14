package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MLight;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.MItem;
import masp.plugins.mlight.data.effects.types.MEffect;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;

import org.bukkit.Material;

public class ItemConfiguration extends Configuration {
	
	public ItemConfiguration(MLight plugin, File dir) {
		super("vanilla-items", plugin, dir);
	}

	@Override
	public void onCreate() {
		for (int i = 256; i <= 388; i++) {
			Material mat = Material.getMaterial(i);
			Item item = Item.byId[i];
			int damage = item.a((net.minecraft.server.Entity) null);
			getConfig().set("items." + mat.name().toLowerCase() + ".attack-effects.general_damage", 
					damage * Settings.CONVERSION_FACTOR);
			if (item instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) item;
				// Damage reduction
				int dReduce = armor.b;
				getConfig().set("items." + mat.name().toLowerCase() + ".defense-effects.general_defense", dReduce);
			}
		}
		getConfig().set("items.fist.attack-effects.general_damage", Settings.CONVERSION_FACTOR);
		getConfig().set("items.shot.attack-effects.general_damage", 5 * Settings.CONVERSION_FACTOR);
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRead() {
		for (int i = 256; i <= 388; i++) {
			Material mat = Material.getMaterial(i);
			MItem item = new MItem(i, (byte) 0);
			loadItem(item, mat.name().toLowerCase());
			getPlugin().getItemManager().addItem(item);
		}
		MItem fist = new MItem(0, (byte) 0);
		loadItem(fist, "fist");
		MItem bow = new MItem(-1, (byte) 0);
		loadItem(bow, "shot");
		
		getPlugin().getItemManager().addItem(bow);
		getPlugin().getItemManager().addItem(fist);
	}
	
	public void loadItem(MItem item, String name) {
		if (getConfig().getConfigurationSection("items." + name + ".attack-effects") != null) {
			for (String effectName : getConfig().getConfigurationSection("items." + name + ".attack-effects").getKeys(false)) {
				MEffect effect = getPlugin().getEffectManager().getEffect(effectName);
				if (effect == null) {
					getPlugin().getLogger().info("Configuration Error: No effect by name " + effectName + " in item config " + name);
					return;
				}
				
				String effectValue = getConfig().getString("items." + name + ".attack-effects." + effectName);
				if (effectValue.contains("%")) {
					try {
						item.getAttack().setEffectPercent(effect, Double.parseDouble(effectValue.replaceAll("%", "")));
					} catch (NumberFormatException ex) {
						getPlugin().getLogger().info("Configuration Error: Effect value must be a number for effect " + effectName + " for item " + name);
					}
				} else {
					try {
						item.getAttack().setEffectDecimal(effect, Double.parseDouble(effectValue));
					} catch (NumberFormatException ex) {
						getPlugin().getLogger().info("Configuration Error: Effect value must be a number for effect " + effectName + " for item " + name);
					}
				}
			}
		}
		
		if (getConfig().getConfigurationSection("items." + name + ".defense-effects") != null) {
			for (String effectName : getConfig().getConfigurationSection("items." + name + ".defense-effects").getKeys(false)) {
				MEffect effect = getPlugin().getEffectManager().getEffect(effectName);
				if (effect == null) {
					getPlugin().getLogger().info("Configuration Error: No effect by name " + effectName + " in item config " + name);
					return;
				}
				
				String effectValue = getConfig().getString("items." + name + ".defense-effects." + effectName);
				if (effectValue.contains("%")) {
					try {
						item.getDefense().setEffectPercent(effect, Double.parseDouble(effectValue.replaceAll("%", "")));
					} catch (NumberFormatException ex) {
						getPlugin().getLogger().info("Configuration Error: Effect value must be a number for effect " + effectName + " for item " + name);
					}
				} else {
					try {
						item.getDefense().setEffectDecimal(effect, Double.parseDouble(effectValue));
					} catch (NumberFormatException ex) {
						getPlugin().getLogger().info("Configuration Error: Effect value must be a number for effect " + effectName + " for item " + name);
					}
				}
			}
		}
	}

}
