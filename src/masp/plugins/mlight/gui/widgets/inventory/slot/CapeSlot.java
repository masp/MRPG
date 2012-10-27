package masp.plugins.mlight.gui.widgets.inventory.slot;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.enums.ItemType;
import masp.plugins.mlight.gui.widgets.inventory.InventoryGui;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Slot;

public class CapeSlot extends ArmorSlot {

	public CapeSlot(int xpos, int ypos, InventoryGui gui, MPlayer player) {
		super(xpos, ypos, gui, player, ItemType.CAPE);
	}
	
	@Override
	public boolean onItemTake(ItemStack item) {
		if (!super.onItemTake(item)) {
			return false;
		}
		if (this.getItemType() == ItemType.CAPE) {
			SpoutManager.getPlayer(getPlayer().getPlayer())
						.resetCape();
		}
		return true;
	}
	
	@Override
	public Slot setItem(ItemStack is) {
		if (is != null) {
			MItem item = MRPG.getItemManager().getItem(is);
			if (item.getType() == ItemType.CAPE) {
				String url = item.getMData().getString("cape-url");
				SpoutManager.getPlayer(getPlayer().getPlayer())
							.setCape(url);
			}
		}
		return super.setItem(is);
	}

}
