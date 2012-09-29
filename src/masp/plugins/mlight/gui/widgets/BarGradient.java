package masp.plugins.mlight.gui.widgets;


import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.Widget;

public class BarGradient extends GenericGradient {
	
	private GenericBar bar;
	
	public BarGradient(GenericBar bar) {
		this.bar = bar;
	}
	
	@Override
	public Widget setWidth(int width) {
		bar.setFullWidth(width);
		bar.updateBar();
		return super.setWidth(width);
	}
	
	public void setWidthDirect(int width) {
		super.setWidth(width);
	}
	
}
