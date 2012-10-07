package masp.plugins.mlight.gui.menus;

import java.awt.Point;

import masp.plugins.mlight.Data;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.gui.widgets.inventory.InventoryGui;

import org.bukkit.event.inventory.InventoryType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Texture;

public class MCustomInventory extends InventoryGui {

	public MCustomInventory(MPlayer player) {
		super(SpoutManager.getPlayer(player.getPlayer()), Data.getInventoryBackground().getWidth(), Data.getInventoryBackground().getHeight());
	}

	@Override
	protected Texture getBackground() {
		return Data.getInventoryBackground();
	}

	@Override
	protected Point getInventoryOffset() {
		return new Point(13, 86);
	}

	@Override
	public InventoryType getType() {
		return InventoryType.PLAYER;
	}
	
}
