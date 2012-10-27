package masp.plugins.mlight.data.gui;

import org.getspout.spoutapi.gui.GenericTexture;

public class ThemeBar extends ThemeElement {

	private int width;
	private int height;
	
	private String label;
	
	private int bColor;
	private int tColor;
	
	public ThemeBar(String url, int x, int y, int width, int height, String label, int bColor, int tColor) {
		super(url, x, y);
		
		this.width = width;
		this.height = height;
		this.label = label;
		this.bColor = bColor;
		this.tColor = tColor;
	}
	
	public int getBottomColor() {
		return bColor;
	}
	
	public int getTopColor() {
		return tColor;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getLabel() {
		return label;
	}
	
	public GenericTexture getLabelTexture() {
		return new GenericTexture(label);
	}
	
}
