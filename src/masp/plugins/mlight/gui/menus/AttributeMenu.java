package masp.plugins.mlight.gui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.Attribute;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.gui.widgets.AttributeAddButton;
import masp.plugins.mlight.gui.widgets.AttributeListItemWidget;
import masp.plugins.mlight.gui.widgets.AttributeListWidget;
import masp.plugins.mlight.utils.Utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.gui.Orientation;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.ScrollBarPolicy;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class AttributeMenu extends GenericPopup {
	
	private final int COLUMN_WIDTH = 90;
	
	private int page;
	
	private Label title;
	
	private AttributeListWidget attList;
	
	private List<AttributeAddButton> buttons = new ArrayList<AttributeAddButton>();
	
	private Container infoContent;
	private Texture background;
	private Label infoTitle;
	private Label infoDescription;
	
	private Button nextButton;
	private Button prevButton;
	
	private Button closeButton;
	
	private List<Label> benefits = new ArrayList<Label>();
	
	public AttributeMenu(SpoutPlayer player) {
		MPlayer mPlayer = MRPG.getPlayer(player);
		page = 1;
		
		closeButton = new GenericButton(ChatColor.RED + "Close") {
			@Override
			public void onButtonClick(ButtonClickEvent event) {
				getPlayer().getMainScreen().closePopup();
			}
		};
		closeButton.setAnchor(WidgetAnchor.BOTTOM_CENTER)
				   .setWidth(64)
				   .setHeight(20)
				   .setX(-(closeButton.getWidth() / 2))
				   .setY(-(closeButton.getHeight() / 2) - getHeight() / 2)
				   .setFixed(true);
		
		title = new GenericLabel("Attribute Menu");
		title.setTextColor(new Color(107, 142, 35))
			 .setWidth(GenericLabel.getStringWidth(title.getText()))
			 .setHeight(GenericLabel.getStringHeight(title.getText()))
			 .setAnchor(WidgetAnchor.TOP_CENTER)
			 .setX(-(title.getWidth() / 2))
			 .setY(25 - (title.getHeight() / 2))
			 .setFixed(true);
		
		List<Attribute> listAttributes = Arrays.asList(
				mPlayer.getAttributes().toArray(
						new Attribute[mPlayer.getAttributes().size()]));
		
		attList = new AttributeListWidget(listAttributes, player, this, page);
		attList.setScrollBarPolicy(Orientation.VERTICAL, ScrollBarPolicy.SHOW_NEVER);
		attList.setBackgroundColor(new Color(0, 0, 0, 0))
			   .setAnchor(WidgetAnchor.CENTER_CENTER)
			   .setWidth(150)
			   .setHeight(175)
			   .setX(-attList.getWidth() - 29)
			   .setY(-(attList.getHeight() / 2) + 10)
			   .setFixed(true);
		
		attList.updateList(listAttributes, MRPG.getPlayer(player), 1);
		
		prevButton = new GenericButton(ChatColor.GRAY + "<--") {
			@Override
			public void onButtonClick(ButtonClickEvent event) {
				updatePage(MRPG.getPlayer(event.getPlayer()), page - 1);
			}
		};
		prevButton.setEnabled(page > 1)
				  .setAnchor(WidgetAnchor.CENTER_CENTER)
				  .setX(attList.getX() + (attList.getWidth() - 20) / 2 - 20)
				  .setY(attList.getY() + attList.getHeight() + 1)
				  .setWidth(36)
				  .setHeight(18)
				  .setFixed(true);
		
		nextButton = new GenericButton(ChatColor.GRAY + "-->") {
			@Override
			public void onButtonClick(ButtonClickEvent event) {
				updatePage(MRPG.getPlayer(event.getPlayer()), page + 1);
			}
			
		};
		nextButton.setEnabled(page < getMaxPage())
				  .setAnchor(WidgetAnchor.CENTER_CENTER)
				  .setX(prevButton.getX() + prevButton.getWidth() + 1)
				  .setY(attList.getY() + attList.getHeight() + 1)
				  .setWidth(36)
				  .setHeight(18)
				  .setFixed(true);
		
		infoContent = new GenericContainer();
		infoContent.setLayout(ContainerType.OVERLAY);
		infoContent.setAnchor(WidgetAnchor.CENTER_CENTER)
				   .setWidth(220)
				   .setHeight(220)
				   .setX(-14)
				   .setY(-(infoContent.getHeight() / 2))
				   .setFixed(true);
		
		background = new GenericTexture("attribute_background.png");
		background.setPriority(RenderPriority.Highest)
				  .setAnchor(WidgetAnchor.CENTER_CENTER)
				  .setWidth(380 + 60)
				  .setHeight(200 + 60)
				  .setX(-(background.getWidth() / 2))
				  .setY(-(background.getHeight() / 2))
				  .setFixed(true);
		
		infoTitle = new GenericLabel("Select an attribute!");
		infoTitle.setTextColor(new Color(107, 142, 35))
				 .setAnchor(WidgetAnchor.TOP_LEFT)
				 .setWidth(GenericLabel.getStringWidth(infoTitle.getText()))
				 .setHeight(GenericLabel.getStringHeight(infoTitle.getText()))
				 .setMarginLeft((infoContent.getWidth() / 2) - (infoTitle.getWidth() / 2))
				 .setMarginTop(20 - (infoTitle.getHeight() / 2));
		
		infoDescription = new GenericLabel(WordUtils.wrap("", (infoContent.getWidth() + 20) / GenericLabel.getStringWidth("q")));
		infoDescription.setTextColor(new Color(84, 84, 84))
					   .setAnchor(WidgetAnchor.CENTER_LEFT)
					   .setWidth(GenericLabel.getStringWidth(infoDescription.getText()))
					   .setHeight(GenericLabel.getStringHeight(infoDescription.getText()))
					   .setMarginLeft(20)
					   .setMarginTop(40);
		
		infoContent.addChildren(infoTitle, infoDescription);
		
		for (Button button : buttons) {
			this.attachWidget(MRPG.getInstance(), button);
		}
		
		this.attachWidgets(MRPG.getInstance(), title, attList, infoContent, closeButton, background, nextButton, prevButton);
		
	}
	
	public void updatePage(MPlayer player, int page) {
		attList.clear();
		attList.setDirty(true);
		
		for (Widget widget : getAttachedWidgets()) {
			if (widget instanceof AttributeAddButton) {
				this.removeWidget(widget);
			}
		}
		
		attList.updateList(Arrays.asList(
				player.getAttributes().toArray(
						new Attribute[player.getAttributes().size()])), player, page);
		
		prevButton.setEnabled(page > 1);
		nextButton.setEnabled(page < getMaxPage());
		
		attList.setScrollPosition(0);
		
		this.page = page;
	}
	
	public int getMaxPage() {
		return (int) Math.ceil((double) MRPG.getAttributeManager().getSkills().size() / 8D);
	}
	
	public void updateAll() {
		for (ListWidgetItem item : attList.getItems()) {
			if (item instanceof AttributeListItemWidget) {
				AttributeListItemWidget nItem = (AttributeListItemWidget) item;
				Attribute att = nItem.getAttribute();
				nItem.setTitle(ChatColor.DARK_GRAY + Utils.getNiceName(att.getName()));
				nItem.setText(ChatColor.GRAY + "(" + MRPG.getPlayer(getPlayer()).getAttributeValue(att.getName()) + "/" + att.getMaxLevel() + ") - " + att.getCost() + " LP");
			}
		}
		attList.setDirty(true);
	}
	
	public void updateInfo(Attribute att) {
		benefits.clear();
		for (Widget widget : infoContent.getChildren()) {
			if (widget instanceof Label) {
				if (widget != infoDescription && widget != infoTitle) {
					infoContent.removeChild(widget);
				}
			}
		}
		
		infoTitle.setText(Utils.getNiceName(att.getName()));
		infoTitle.setWidth(GenericLabel.getStringWidth(att.getName())).setHeight(GenericLabel.getStringHeight(att.getName()));
		infoTitle.setMarginLeft((infoContent.getWidth() / 2) - (infoTitle.getWidth() / 2));
		
		infoDescription.setText(WordUtils.wrap(att.getDescription(), (infoContent.getWidth() + 20) / GenericLabel.getStringWidth("q")));
		
		int i = 0;
		Set<MEffect> effects = att.getEffects();
		for (MEffect effect : effects) {
			int column = (int) Math.floor(i / 5);
			int row = i % 5;
			String text;
			text = Utils.getNiceName(effect.getName()) + ": " + Utils.shorten(att.getTotalEffects(effect));
			Color color = att.getTotalEffects(effect) > 0 ? new Color(107, 142, 35) : new Color(205, 51, 51);
			Label eLabel = new GenericLabel(text);
			eLabel.setTextColor(color)
				  .setScale(0.7f)
				  .setAnchor(WidgetAnchor.CENTER_LEFT)
				  .setWidth((int) Math.floor((float) GenericLabel.getStringWidth(text) * 0.7f))
				  .setHeight((int) Math.floor((float) GenericLabel.getStringHeight(text) * 0.7f))
				  .setMarginLeft(20 + (column * COLUMN_WIDTH))
				  .setMarginTop(120 + (eLabel.getHeight() * row));
			
			benefits.add(eLabel);
			i++;
		}
		
		for (Label label : benefits) {
			infoContent.addChild(label);
		}
	}
	
}
