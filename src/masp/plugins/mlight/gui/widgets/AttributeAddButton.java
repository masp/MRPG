package masp.plugins.mlight.gui.widgets;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.gui.menus.AttributeMenu;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;

public class AttributeAddButton extends GenericButton {
	
	private Attribute att;
	private AttributeMenu menu;
	
	public AttributeAddButton(Attribute att, AttributeMenu menu) {
		this.setText("+").setTextColor(new Color(34, 139, 34));
		
		this.att = att;
		this.menu = menu;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		MPlayer player = MRPG.getPlayer(event.getPlayer());
		if (player.getSkillPoints() >= att.getCost()) {
			player.setSkillValue(att, player.getSkillValue(att.getName()) + 1);
			player.setSkillPoints(player.getSkillPoints() - 1 < 0 || player.getSkillPoints() == 0 ? 0 : player.getSkillPoints() - 1);
			this.setEnabled(player.getSkillPoints() >= att.getCost());
			menu.updateAll();
		}
	}
	
	public Attribute getAttribute() {
		return att;
	}
	
	public AttributeMenu getMenu() {
		return menu;
	}
	
}
