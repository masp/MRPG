package masp.plugins.mlight.managers;

import java.util.ArrayList;
import java.util.List;

import masp.plugins.mlight.data.MItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemManager {
	
	private List<MItem> items;
	
	public ItemManager() {
		items = new ArrayList<MItem>();
	}
	
	public void addItem(MItem item) {
		items.add(item);
	}
	
	public void removeItem(int id) {
		for (MItem item : items) {
			if (item.getId() == id) {
				items.remove(item);
			}
		}
	}
	
	public MItem getItem(int id) {
		for (MItem item : items) {
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}
	
	public MItem getItem(Material mat) {
		for (MItem item : items) {
			if (item.getMaterial() != null) {
				if (item.getMaterial().equals(mat)) {
					return item;
				}
			}
		}
		return null;
	}
	
	public MItem[] getItems() {
		return items.toArray(new MItem[items.size()]);
	}
	
	public MItem getItem(ItemStack item) {
		return getItem(item.getType());
	}
	
}
