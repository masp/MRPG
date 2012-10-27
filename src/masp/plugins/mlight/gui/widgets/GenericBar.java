package masp.plugins.mlight.gui.widgets;


import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Gradient;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.RenderPriority;

public class GenericBar extends GenericContainer implements Bar {

	private float maxValue;
	private float currValue;
	
	private BarGradient gradient;
	private BarGradient bgGradient;
	
	private Label text;
	
	private float scale;
	
	// We record the width if it is full, so we can adjust accordingly based on specified width
	private int fullWidth;
	
	public GenericBar(int maxValue, int currValue) {
		super();
		setFixed(true);
		setLayout(ContainerType.OVERLAY);
		
		text = new GenericLabel();
		
		gradient = new BarGradient(this);
		bgGradient = new BarGradient(this);
		
		this.maxValue = maxValue;
		this.currValue = currValue;
		// Initial recording
		this.fullWidth = getWidth();
	}
	
	@Override
	public void init() {
		gradient.setFixed(true)
			.setWidth(getWidth())
			.setHeight(getHeight());
		
		bgGradient.setFixed(true)
				  .setWidth(getWidth())
				  .setHeight(getHeight())
				  .setPriority(RenderPriority.Highest);
		
		bgGradient.setBottomColor(
						gradient.getBottomColor().clone().setAlpha(0.3f)
				  )
				  .setTopColor(
						gradient.getTopColor().clone().setAlpha(0.3f)
				  );

		gradient.setFixed(true)
			.setPriority(RenderPriority.High);
		
		scale = (float) getHeight() / 10f;
		text.setScale(scale);
		
		
		
		this.updateBar();

		addChildren(gradient,bgGradient, text);
	}
	
	public GenericBar(int maxValue) {
		this(maxValue, maxValue);
	}
	
	public GenericBar() {
		this(100, 100);
	}
	
	@Override
	public int getFullWidth() {
		return fullWidth;
	}
	
	@Override
	public void setFullWidth(int fullWidth) {
		this.fullWidth = fullWidth;
	}
	
	@Override
	public Label getLabel() {
		return text;
	}
	
	@Override
	public Gradient getGradient() {
		return gradient;
	}

	@Override
	public float getMaxValue() {
		return maxValue;
	}

	@Override
	public float getCurrentValue() {
		return currValue;
	}

	@Override
	public void setCurrentValue(float value) {
		if (value >= 0) {
			currValue = value;
			this.updateBar();
		}
	}

	@Override
	public void setMaxValue(float value) {
		if (value >= 0) {
			maxValue = value;
			this.updateBar();
		}
	}

	@Override
	public void updateBar() {
		gradient.setWidthDirect((int) Math.floor((currValue * fullWidth) / maxValue));
		text.setText((int) currValue + "/" + (int) maxValue);
		text.setWidth((int) Math.floor(GenericLabel.getStringWidth(text.getText()) * text.getScale()));
		text.setHeight((int) Math.floor(GenericLabel.getStringHeight(text.getText()) * text.getScale()));
		text.setMarginLeft((fullWidth / 2) + -(text.getWidth() / 2))
			.setMarginTop(((getHeight() + 2) - text.getHeight()) / 2);
	}
	
}
