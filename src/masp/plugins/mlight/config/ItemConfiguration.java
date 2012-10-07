package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.enums.ItemType;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;

import org.bukkit.Material;

public class ItemConfiguration extends Configuration {
	
	public ItemConfiguration(MRPG plugin, File dir) {
		super("vanilla-items", plugin, dir);
	}

	@Override
	public void onCreate() {
		for (int i = 256; i <= 385; i++) {
			Material mat = Material.getMaterial(i);
			Item item = Item.byId[i];
			int damage = item.a((net.minecraft.server.Entity) null);
			getConfig().set("items." + mat.name().toLowerCase() + ".attack-effects.decimal.general_damage", 
					damage * Settings.CONVERSION_FACTOR);
			if (item instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) item;
				// Damage reduction
				int dReduce = armor.b;
				getConfig().set("items." + mat.name().toLowerCase() + ".defense-effects.decimal.general_defense", dReduce);
			}
		}
		getConfig().set("items.fist.attack-effects.decimal.general_damage", Settings.CONVERSION_FACTOR);
		getConfig().set("items.fist.attack-effects.percent.general_damage", Settings.CONVERSION_FACTOR);
		getConfig().set("items.shot.attack-effects.decimal.general_damage", 5 * Settings.CONVERSION_FACTOR);
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRead() {
		for (int i = 256; i <= 385; i++) {
			Material mat = Material.getMaterial(i);
			if (mat == null) continue;
			MItem item = new MItem(i, (byte) 0, ItemType.OTHER, false);
			loadItem(item, mat.name().toLowerCase());
			MRPG.getItemManager().addItem(item);
		}
		MItem fist = new MItem(0, (byte) 0, ItemType.OTHER, false);
		loadItem(fist, "fist");
		MItem bow = new MItem(-1, (byte) 0, ItemType.OTHER, false);
		loadItem(bow, "shot");
		
		MRPG.getItemManager().addItem(bow);
		MRPG.getItemManager().addItem(fist);
	}
	
	public void loadItem(MItem item, String name) {
		if (getConfig().getConfigurationSection("items." + name + ".attack-effects") != null) {
			super.loadEffects(item.getAttack(), "items." + name + ".attack-effects");
		}
		
		if (getConfig().getConfigurationSection("items." + name + ".defense-effects") != null) {
			super.loadEffects(item.getAttack(), "items." + name + ".defense-effects");
		}
	}

}
