package masp.plugins.mlight.gui.widgets.inventory.slot;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.MPlayerInventory;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.enums.ItemType;
import masp.plugins.mlight.gui.widgets.inventory.InventoryGui;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.getspout.spoutapi.gui.Slot;

public class ArmorSlot extends InventorySlot {

	private ItemType type;
	private MPlayer player;
	
	private int index;
	
	private static final int[] ARMOR_HELMET = {298, 302, 306, 310, 314, 86};
	private static final int[] ARMOR_CHESTPLATE = {299, 303, 307, 311, 315};
	private static final int[] ARMOR_LEGGINGS = {300, 304, 308, 312, 316};
	private static final int[] ARMOR_BOOTS = {301, 305, 309, 313, 317};
	
	public ArmorSlot(int xpos, int ypos, InventoryGui gui, MPlayer player, ItemType type, int... index) {
		super(xpos, ypos, gui);
		
		this.index = 0;
		
		this.player = player;
		this.type = type;
		
		PlayerInventory inv = player.getPlayer().getInventory();
		MPlayerInventory mInv = player.getInventory();
		if (type == ItemType.HELMET && inv.getHelmet() != null) {
			this.setItem(inv.getHelmet());
		} else if (type == ItemType.CHESTPLATE && inv.getChestplate() != null) {
			this.setItem(inv.getChestplate());
		} else if (type == ItemType.LEGGINGS && inv.getLeggings() != null) {
			this.setItem(inv.getLeggings());
		} else if (type == ItemType.BOOT && inv.getBoots() != null) {
			this.setItem(inv.getBoots());
		} else if (type == ItemType.AMULET && mInv.getAmulet() != null) {
			this.setItem(mInv.getAmulet());
		} else if (type == ItemType.CAPE && mInv.getCape() != null) {
			this.setItem(mInv.getCape());
		} else if (type == ItemType.BELT && mInv.getBelt() != null) {
			this.setItem(mInv.getBelt());
		} else if (type == ItemType.BADGE && mInv.getBadge() != null) {
			this.setItem(mInv.getBadge());
		} else if (type == ItemType.EARRING && mInv.getEarrings()[index[0]] != null) {
			this.setItem(mInv.getEarrings()[index[0]]);
			this.index = index[0];
		} else if (type == ItemType.PET && mInv.getPet() != null) {
			this.setItem(mInv.getPet());
		} else if (type == ItemType.RING && mInv.getRings()[index[0]] != null) {
			this.index = index[0];
			this.setItem(mInv.getRings()[index[0]]);
		}
	}
	
	public static ItemType getItemType(ItemStack item) {
		if (ArrayUtils.contains(ARMOR_HELMET, item.getTypeId())) {
			return ItemType.HELMET;
		} else if (ArrayUtils.contains(ARMOR_CHESTPLATE, item.getTypeId())) {
			return ItemType.CHESTPLATE;
		} else if (ArrayUtils.contains(ARMOR_LEGGINGS, item.getTypeId())) {
			return ItemType.LEGGINGS;
		} else if (ArrayUtils.contains(ARMOR_BOOTS, item.getTypeId())) {
			return ItemType.BOOT;
		} else {
			return ItemType.OTHER;
		}
	}
	
	public MPlayer getPlayer() {
		return player;
	}
	
	@Override
	public boolean onItemPut(ItemStack item) {
		if (MRPG.getItemManager().getItem(item).getType() != this.getItemType() && getItemType(item) != getItemType()) {
			return false;
		}
		return super.onItemPut(item);
	}
	
	@Override
	public boolean onItemExchange(ItemStack item, ItemStack change) {
		if (MRPG.getItemManager().getItem(change).getType() != getItemType() && getItemType(change) != getItemType()) {
			return false;
		}
		return super.onItemExchange(item, change);
	}
	
	@Override
	public Slot setItem(ItemStack i) {
		PlayerInventory inv = player.getPlayer().getInventory();
		MPlayerInventory mInv = player.getInventory();
		if (getItemType() == ItemType.HELMET) {
			inv.setHelmet(i);
		} else if (getItemType() == ItemType.CHESTPLATE) {
			inv.setChestplate(i);
		} else if (getItemType() == ItemType.LEGGINGS) {
			inv.setLeggings(i);
		} else if (getItemType() == ItemType.BOOT) {
			inv.setBoots(i);
		} else if (getItemType() == ItemType.AMULET) {
			mInv.setAmulet(i);
		} else if (getItemType() == ItemType.CAPE) {
			mInv.setCape(i);
		} else if (getItemType() == ItemType.BELT) {
			mInv.setBelt(i);
		} else if (getItemType() == ItemType.BADGE) {
			mInv.setBadge(i);
		} else if (getItemType() == ItemType.EARRING) {
			if (index == 0) {
				mInv.setEarringOne(i);
			} else {
				mInv.setEarringTwo(i);
			}
		} else if (getItemType() == ItemType.RING) {
			if (index == 0) {
				mInv.setRingOne(i);
			} else {
				mInv.setRingTwo(i);
			}
		}
		return super.setItem(i);
	}
	
	public ItemType getItemType() {
		return type;
	}

}
