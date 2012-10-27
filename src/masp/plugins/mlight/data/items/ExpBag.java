package masp.plugins.mlight.data.items;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.utils.Data;

import org.bukkit.entity.Item;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.material.item.GenericCustomItem;

public class ExpBag extends GenericCustomItem {
	
	public ExpBag() {
		super(MRPG.getInstance(), "Exp Bag", Data.BAG_URL);
	}
	
	public static double getExp(Item item) {
		if (new SpoutItemStack(item.getItemStack()).isCustomItem()) {
			SpoutItemStack custom = new SpoutItemStack(item.getItemStack());
			CustomItem ci = (CustomItem) custom.getMaterial();
			if (ci instanceof ExpBag) {
				return MRPG.getExpManager().getAttachedSource(item.getUniqueId());
			}
		}
		return -1D;
	}

}
