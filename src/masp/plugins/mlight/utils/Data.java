package masp.plugins.mlight.utils;

import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Texture;

public class Data {
	
	public static final String HUD_BACKGROUND = "https://dl.dropbox.com/u/26497130/MRPG/mrpg_hud_3_label.png";
	public static final String HUD_BUBBLE = "https://dl.dropbox.com/u/26497130/MRPG/mrpg_hud_3_bubble.png";
	private static final String INV_BACKGROUND = "https://dl.dropbox.com/u/26497130/inventoryarmor.png";
	public static final String BAG_URL = "https://dl.dropbox.com/u/26497130/MRPG/Bag.png";
	
	public static Texture getInventoryBackground() {
		return (Texture) new GenericTexture(INV_BACKGROUND).setWidth(192).setHeight(176).setFixed(true);
	}
	
	public static String getExpTexture(int i) {
		return "https://dl.dropbox.com/u/26497130/MRPG/exp_bar/exp_bar_" + i + ".png";
	}
	
}
