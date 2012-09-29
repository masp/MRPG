package masp.plugins.mlight.managers;

import java.util.ArrayList;
import java.util.List;

import masp.plugins.mlight.data.items.MItem;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;

public class ItemManager {
	
	private List<MItem> items;
	
	public ItemManager() {
		items = new ArrayList<MItem>();
	}
	
	public void addItem(MItem item) {
		items.add(item);
	}
	
	private void removeItem(int id) {
		for (MItem item : items) {
			if (item.getId() == id) {
				items.remove(item);
			}
		}
	}
	
	public void removeItem(ItemStack item) {
		if (new SpoutItemStack(item).isCustomItem()) {
			removeItem(item.getDurability());
		} else {
			removeItem(item.getTypeId());
		}
	}
	
	/*
	 * You must use custom ID's if it is a custom item!
	 */
	public MItem getItem(int id) {
		for (MItem item : items) {
			if (item.getId() == id) {
				return item;
			}
		}
		return getItem(0);
	}
	
	public MItem getItem(ItemStack check) {
		if (new SpoutItemStack(check).isCustomItem()) {
			return getItem(check.getDurability());
		} else {
			return getItem(check.getTypeId());
		}
	}
	
	public MItem[] getItems() {
		return items.toArray(new MItem[items.size()]);
	}
	
}
