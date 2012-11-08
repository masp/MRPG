package masp.plugins.mlight.gui.widgets.inventory.slot;

import java.util.HashMap;

import masp.plugins.mlight.gui.menus.MCustomInventory;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.Slot;

public class CraftingSlot extends InventorySlot {
	
	private int id;
	
	public CraftingSlot(int xpos, int ypos, int id, MCustomInventory gui) {
		super(xpos, ypos, gui);
		
		this.id = id;
	}
	
	@Override
	public boolean onItemPut(ItemStack item) {
		if (!super.onItemPut(item)) {
			return false;
		}
		this.getCustomInv().setCraftingSlot(this.getSlotId(), item);
		this.getCustomInv().onCraft();
		
		return true;
	}
	
	@Override
	public void onItemShiftClicked() {
		ItemStack item = this.getItem();
		Player player = (Player) this.getGui().getPlayer();
		Inventory pInv = player.getInventory();
		// Returns the left overs
		HashMap<Integer, ItemStack> returns = pInv.addItem(item);
		if (returns != null && returns.size() >= 0 && returns.get(0) != null) {
			ItemStack rItem = returns.get(0);
			
			this.setItem(rItem);
			this.getCustomInv().setCraftingSlot(this.getSlotId(), rItem);
		} else {
			this.setItem(new CraftItemStack(0));
			this.getCustomInv().setCraftingSlot(this.getSlotId(), null);
		}
		this.getCustomInv().onCraft();
		this.getGui().updateContents();
	}
	
	@Override
	public boolean onItemTake(ItemStack item) {
		if (!super.onItemTake(item)) {
			return false;
		}

		this.getCustomInv().setCraftingSlot(this.getSlotId(), null);
		this.getCustomInv().onCraft();
		
		return true;
	}
	
	@Override
	public boolean onItemExchange(ItemStack item, ItemStack exchange) {
		if (!super.onItemExchange(item, exchange)) {
			return false;
		}
		this.getCustomInv().setCraftingSlot(this.getSlotId(), exchange);
		this.getCustomInv().onCraft();
		return true;
	}
	
	@Override
	public Slot setItem(ItemStack item) {
		return super.setItem(item);
	}
	
	public int getSlotId() {
		return id;
	}
	
	public MCustomInventory getCustomInv() {
		return (MCustomInventory) this.getGui();
	}

}
