package masp.plugins.mlight.gui.widgets.inventory.slot;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.data.player.Title;
import masp.plugins.mlight.enums.ItemType;
import masp.plugins.mlight.gui.widgets.inventory.InventoryGui;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.Slot;

public class BadgeSlot extends ArmorSlot {

	public BadgeSlot(int xpos, int ypos, InventoryGui gui, MPlayer player) {
		super(xpos, ypos, gui, player, ItemType.BADGE);
	}
	
	@Override
	public boolean onItemTake(ItemStack is) {
		if (!super.onItemPut(is)) {
			return false;
		}
		getPlayer().removeTitle("badge");
		return true;
	}
	
	@Override
	public Slot setItem(ItemStack is) {
		MItem item = MRPG.getItemManager().getItem(is);
		if (is != null) {
			if (item.getType() == ItemType.BADGE) {
				getPlayer().addTitle(new Title(item.getMData().getString("badge-player-title"), "badge", 3));
			}
		}
		return super.setItem(is);
	}

}
