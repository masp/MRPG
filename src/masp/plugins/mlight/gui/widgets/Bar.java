package masp.plugins.mlight.gui.widgets;

import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.Gradient;
import org.getspout.spoutapi.gui.Label;

public interface Bar extends Container {
	
	public float getMaxValue();
	
	public float getCurrentValue();
	
	public void setCurrentValue(float value);
	
	public void setMaxValue(float value);
	
	public void updateBar();
	
	public Label getLabel();
	
	public Gradient getGradient();
	
	public void setFullWidth(int fullWidth);
	
	public int getFullWidth();
	
	public void init();

}
