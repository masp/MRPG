package masp.plugins.mlight;

import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Texture;

public class Data {
	
	public static final String HUD_BACKGROUND = "label1_1.png";
	public static final String FACE_TEXTURE = "http://face.mmo.me.uk/%name%.png";
	private static final String INV_BACKGROUND = "https://dl.dropbox.com/u/26497130/inventoryarmor.png";
	
	public static Texture getInventoryBackground() {
		return (Texture) new GenericTexture(INV_BACKGROUND).setWidth(192).setHeight(176).setFixed(true);
	}
	
}
