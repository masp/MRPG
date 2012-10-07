package masp.plugins.mlight.gui.widgets.inventory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

/*
 * Borrowed from ExtraFurnaces
 * Original Author: Pamelleos
 */

public abstract class InventoryGui extends InventoryView {
	private SpoutPlayer sp;
	private Container cont;

	private int width;
	private int height;

	private PlayerInventory inventory;

	private List<InventorySlot> slots = new ArrayList<InventorySlot>();

	public InventoryGui(SpoutPlayer p, int width, int height) {
		this.width = width;
		this.height = height;
		sp = p;
		inventory = p.getInventory();
		cont = new GenericContainer();
		for(int i = 0; i < 36; i++) {
			Point loc = getSlotLocation(i);
			InventorySlot s = new InventorySlot((int) loc.getX(), (int) loc.getY(), this);
			s.setItem(inventory.getItem(i), false);
			addSlot(s);
		}
	}

	public boolean makeGui() {
		InventoryOpenEvent ioe = new InventoryOpenEvent(this);
		Bukkit.getPluginManager().callEvent(ioe);
		if(ioe.isCancelled()) return false;
		cont.setLayout(ContainerType.OVERLAY).setAnchor(WidgetAnchor.CENTER_CENTER).setWidth(width).setHeight(height).setX(-width/2).setY(-height/2);
		cont.addChild(getBackground().setPriority(RenderPriority.High));
		for(int i = 0; i < slots.size(); i++) cont.addChild(slots.get(i));
		for(Widget w : getWidgets()) cont.addChild(w);
		sp.getMainScreen().attachPopupScreen((PopupScreen) new GenericPopup() {
			@Override
			public void handleItemOnCursor(ItemStack is) {
				if(is.getTypeId() == 0 || is.getAmount() == 0) return;
				eject(is);
			}
		});
		return true;
	}

	protected abstract Texture getBackground();

	/**
	 * This method returns an offset to the top-left corner of the standard 36 inventory slots.
	 */
	protected abstract Point getInventoryOffset();

	private Point getSlotLocation(int index) {
		if(index < 0 || index > 36) throw new IllegalArgumentException("The inventory only takes up slots 0 to " + (35 + slots.size()) + "!");

		//calculate square in the inventory.
		int ix, iy;
		ix = index%9;
		if(index<9) iy=3;
		else iy = ( (index-ix) / 9) - 1;

		//calculate exact coordinates in the inventory.
		int ax,ay;
		ax = 18*ix;
		if(iy!=3) ay=18*iy;
		else ay = 18*3 + 4;
		Point result = getInventoryOffset();
		result.translate(ax, ay);
		return result;
	}

	/**
	 * Gets the index of a slot.
	 * @param slot The slot who's index to get.
	 * @return The slot's index.
	 */
	public int getIndex(InventorySlot slot) {
		return slots.indexOf(slot);
	}

	/**
	 * Get the contents of an inventory slot.
	 * @param slot The slot who's contents to get.
	 * @return The slot's contents.
	 */
	public ItemStack getContents(InventorySlot slot) {
		int id = slots.indexOf(slot);
		return id >= 0 ? getContents(slot) : null;

	}

	public ItemStack getContents(int id) {
		if(id < 36) return inventory.getItem(id);
		return slots.get(id - 36).getItem();
	}

	/**
	 * Change an inventory slot.
	 * @param id The slot to update
	 * @param contents The contents of the slot to change
	 */
	public void setContents(InventorySlot slot, ItemStack contents) {
		int id = slots.indexOf(slot);
		if(id >= 0) setContents(id, contents);
	}

	public void setContents(int id, ItemStack contents) {
		setContents(id, contents, true);
	}

	public void setContents(int id, ItemStack contents, boolean update) {
		slots.get(id).setItem(contents, false);
		if(update) updateContents(id, null);
	}

	/**
	 * Clears an inventory slot
	 * @param id The slot to update
	 */
	public void clearContents(InventorySlot slot) {
		int id = slots.indexOf(slot);
		if(id >= 0) clearContents(id);
	}

	public void clearContents(int id) {
		clearContents(id, true);
	}

	public void clearContents(int id, boolean update) {
		slots.get(id).setItem(new ItemStack(0), false);
		if(update) updateContents(id, null);
	}

	/**
	 * Copies the ItemStack's contents to a
	 * more permanent location.
	 */
	public void updateContents(InventorySlot slot, ItemStack contents) {
		int id = slots.indexOf(slot);
		if(id >= 0) updateContents(id, contents);
	}

	public void updateContents(int id, ItemStack is) {
		if(id < 36) {
			if(is == null) inventory.clear(id);
			else inventory.setItem(id, is);
		}
	}

	public void eject(ItemStack is) {
		Location l = sp.getLocation();
		l.getWorld().dropItem(l, is);
	}

	/**
	 * Adds a new InventorySlot to the GUI. A slot can't be added twice.
	 * @param slot The InventorySlot to add.
	 * @return The Slot's ID for future reference.
	 */
	protected int addSlot(InventorySlot slot) {
		if(slots.contains(slot)) throw new IllegalArgumentException("You can't register a slot twice!");
		slots.add(slot);
		return slots.indexOf(slot) + 35;
	}

	protected boolean isReadOnly(int slot) {
		if(slot < 36) return false;
		return slots.get(slot - 36).isReadOnly();
	}

	public int getGuiWidth() {
		return width;
	}
	public int getGuiHeight() {
		return height;
	}
	public void setGuiSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void onClose() {
		InventoryCloseEvent icv = new InventoryCloseEvent(this);
		Bukkit.getPluginManager().callEvent(icv);
	}

	public Widget[] getWidgets() { return new Widget[0]; }
	
	@Override
	public Inventory getTopInventory() {
		return inventory;
	}
	
	@Override
	public Inventory getBottomInventory() {
		return inventory;
	}

	@Override
	public HumanEntity getPlayer() {
		return sp;
	}
}
