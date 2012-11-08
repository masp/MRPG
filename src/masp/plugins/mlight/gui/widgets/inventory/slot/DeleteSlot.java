package masp.plugins.mlight.gui.widgets.inventory.slot;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.gui.menus.MCustomInventory;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class DeleteSlot extends InventorySlot {
	
	public DeleteSlot(int xpos, int ypos, MCustomInventory gui) {
		super(xpos, ypos, gui);
	}
	
	@Override
	public boolean onItemPut(ItemStack item) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(MRPG.getInstance(),
		new Runnable() {
			@Override
			public void run() {
				setItem(new CraftItemStack(0));
			}
		}, 1L);
		return true;
	}

}
