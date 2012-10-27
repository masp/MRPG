package masp.plugins.mlight.data.gui;

import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Texture;

public class ThemeElement {

	private String url;
	private int x;
	private int y;
	
	public ThemeElement(String url, int x, int y) {
		this.url = url;
		this.x = x;
		this.y = y;
	}
	
	public String getURL() {
		return url;
	}
	
	public Texture getTexture() {
		return new GenericTexture(getURL()); 
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
