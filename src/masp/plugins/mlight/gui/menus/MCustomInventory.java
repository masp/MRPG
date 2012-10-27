package masp.plugins.mlight.gui.menus;

import java.awt.Point;

import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.enums.ItemType;
import masp.plugins.mlight.gui.widgets.inventory.InventoryGui;
import masp.plugins.mlight.gui.widgets.inventory.slot.ArmorSlot;
import masp.plugins.mlight.gui.widgets.inventory.slot.CapeSlot;
import masp.plugins.mlight.utils.Data;

import org.bukkit.event.inventory.InventoryType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Texture;

public class MCustomInventory extends InventoryGui {

	public MCustomInventory(MPlayer player) {
		super(SpoutManager.getPlayer(player.getPlayer()), 192, 176);
		
		int armorX = 16;
		int armorY = 12;
		ItemType[] types = {ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOT};
		for (int i = 0; i < 4; i++) {
			addSlot(new ArmorSlot(armorX, armorY + (18 * i), this, player, types[i]));
		}
		addSlot(new ArmorSlot(armorX + 18, armorY, this, player, ItemType.AMULET));
		addSlot(new CapeSlot(armorX + 18, armorY + 18, this, player));
		addSlot(new ArmorSlot(armorX + 18, armorY + (18 * 2), this, player, ItemType.BELT));
		addSlot(new ArmorSlot(armorX + 18, armorY + (18 * 3), this, player, ItemType.BADGE));
		
		addSlot(new ArmorSlot(armorX + 36, armorY, this, player, ItemType.EARRING, 0));
		addSlot(new ArmorSlot(armorX + 36, armorY + 18, this, player, ItemType.EARRING, 1));
		addSlot(new ArmorSlot(armorX + 36, armorY + (18 * 2), this, player, ItemType.RING, 0));
		addSlot(new ArmorSlot(armorX + 36, armorY + (18 * 3), this, player, ItemType.RING, 1));
		
		// Added something :)
	}

	@Override
	protected Texture getBackground() {
		return Data.getInventoryBackground();
	}

	@Override
	protected Point getInventoryOffset() {
		return new Point(16, 88);
	}

	@Override
	public InventoryType getType() {
		return InventoryType.PLAYER;
	}
	
}
