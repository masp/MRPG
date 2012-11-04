package masp.plugins.mlight.gui.widgets.inventory.slot;

import masp.plugins.mlight.gui.menus.MCustomInventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ResultSlot extends InventorySlot {

	public ResultSlot(int xpos, int ypos, MCustomInventory gui) {
		super(xpos, ypos, gui);
		
		this.setReadOnly(true);
	}
	
	@Override
	public boolean onItemExchange(ItemStack item, ItemStack exchange) {
		if (item.getTypeId() == exchange.getTypeId() 
				&& item.getData().getData() == exchange.getData().getData()) {
			((MCustomInventory) this.getGui()).onTake();
		}
		return true;
	}
	
	@Override
	public boolean onItemTake(ItemStack item) {
		((MCustomInventory) this.getGui()).onTake();
		return true;
	}
	
	@Override
	public void onItemShiftClicked() {
		Inventory inv = this.getGui().getPlayer().getInventory();
		while (this.getItem().getTypeId() != 0) {
			if (!((MCustomInventory) this.getGui()).onCraft()) {
				break;
			}
			inv.addItem(this.getItem());
			((MCustomInventory) this.getGui()).onTake();
		}
	}
	
}
