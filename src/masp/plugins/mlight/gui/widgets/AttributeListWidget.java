package masp.plugins.mlight.gui.widgets;

import java.util.List;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.gui.menus.AttributeMenu;
import masp.plugins.mlight.utils.Utils;

import org.bukkit.ChatColor;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class AttributeListWidget extends GenericListWidget {

	private AttributeMenu parent;
	
	public AttributeListWidget(List<Attribute> attributes, SpoutPlayer player, AttributeMenu parent, int page) {
		this.parent = parent;
	}
	
	public void updateList(List<Attribute> attributes, final MPlayer player, int page) {
		int posPage = page - 1;
		for (int i = posPage * 8; i < page * 8 && i < attributes.size(); i++) {
			Attribute att = attributes.get(i);
			AttributeListItemWidget item = new AttributeListItemWidget(ChatColor.DARK_GRAY + Utils.getNiceName(att.getName()), 
					ChatColor.GRAY + "(" + player.getSkillValue(att.getName()) + "/" + att.getMaxLevel() + ") - " + att.getCost() + " LP",
					att);
			this.addItem(item);
			
			AttributeAddButton button = new AttributeAddButton(att, parent);
			button.setEnabled(player.getSkillPoints() >= att.getCost());
			button.setAnchor(WidgetAnchor.CENTER_CENTER)
				  .setWidth(18)
				  .setHeight(18)
				  .setX(getX() + getWidth() + 8)
				  .setY(getY() + 6 + ((i - (posPage * 8)) * 20))
				  .setFixed(true);
			
			parent.attachWidget(MRPG.getInstance(), button);
		}
	}
	
	@Override
	public void onSelected(int item, boolean doubleClick) {
		Attribute att = ((AttributeListItemWidget) getItem(item)).getAttribute();
		if (!doubleClick) {
			parent.updateInfo(att);
		}
	}

}
