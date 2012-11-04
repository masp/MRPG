package masp.plugins.mlight.gui.widgets;


import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;

public class GenericBar extends GenericContainer {
	protected GenericLabel description;
	protected GenericTexture background, progress;
	protected int maxValue;
	
	/**
	 * Default constructor of the widget
	 * 
	 * @param backgroundTexture
	 *            the background texture used
	 * @param progressTexture
	 *            the progress texture used
	 */
	public GenericBar(String backgroundTexture, String progressTexture,
			int width, int height, Color color) {
		this.width = width;
		
		setLayout(ContainerType.OVERLAY);
		setAlign(WidgetAnchor.TOP_LEFT);
		setWidth(width).setHeight(height).setFixed(true);
	
		background = new GenericTexture(backgroundTexture);
		background.setPriority(RenderPriority.Highest).setWidth(width).setHeight(height).setFixed(true);
		addChild(background);
	
		progress = new GenericTexture(progressTexture);
		progress.setPriority(RenderPriority.High).setWidth(width).setHeight(height).setFixed(true);
		addChild(progress);
	
		description = (GenericLabel) new GenericLabel("0");
		description.setScale(0.85f).setPriority(RenderPriority.Normal);
		description.setTextColor(color);
	
		addChild(description);
	}
	
	/**
	 * Set the texture of the widget
	 * 
	 * @param background
	 *            the background texture
	 * @param progress
	 *            the progress texture
	 */
	public void setTexture(String background, String progress) {
		this.background.setUrl(background);
		this.progress.setUrl(progress);
	}
	
	/**
	 * Set the max value of the counting
	 * 
	 * @param value
	 *            the max value
	 */
	public void setMaxValue(int value) {
		maxValue = value;
	}
	
	/**
	 * Set the value of the counting
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(int value) {
		if (maxValue != 0)
			progress.setWidth((value * getWidth()) / maxValue);
		else
			progress.setWidth(getWidth());
		description.setText(value + "/" + maxValue);
		description.setMarginLeft(getWidth() / 2
				- (description.getText().length() * 5) / 2);
		deferLayout();
	}
	
		
}
