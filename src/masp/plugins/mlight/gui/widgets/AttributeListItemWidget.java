package masp.plugins.mlight.gui.widgets;

import masp.plugins.mlight.data.Attribute;

import org.getspout.spoutapi.gui.ListWidgetItem;

public class AttributeListItemWidget extends ListWidgetItem {
	
	private Attribute att;
	
	public AttributeListItemWidget(String title, String description, Attribute att) {
		super(title, description);
		this.att = att;
	}
	
	public Attribute getAttribute() {
		return att;
	}
	
}
