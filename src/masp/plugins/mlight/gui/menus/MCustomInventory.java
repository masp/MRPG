package masp.plugins.mlight.gui.menus;

import java.awt.Point;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.enums.ItemType;
import masp.plugins.mlight.gui.widgets.inventory.InventoryGui;
import masp.plugins.mlight.gui.widgets.inventory.slot.ArmorSlot;
import masp.plugins.mlight.gui.widgets.inventory.slot.BadgeSlot;
import masp.plugins.mlight.gui.widgets.inventory.slot.CapeSlot;
import masp.plugins.mlight.gui.widgets.inventory.slot.CraftingSlot;
import masp.plugins.mlight.gui.widgets.inventory.slot.ResultSlot;
import masp.plugins.mlight.utils.Data;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCraftResult;
import net.minecraft.server.InventoryCrafting;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Texture;

public class MCustomInventory extends InventoryGui {

	private InventoryCrafting crafting;
	private IInventory craftResult;
	
	private CraftingSlot[] crafts = new CraftingSlot[4];
	private ResultSlot result;
	
	public MCustomInventory(MPlayer player) {
		super(SpoutManager.getPlayer(player.getPlayer()), 192, 176);
		
		int armorX = 16;
		int armorY = 12;
		
		ItemType[] types = {ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOT};
		for (int i = 0; i < 4; i++) {
			addSlot(new ArmorSlot(armorX, armorY + (18 * i), this, player, types[i]));
		}
		
		addSlot(new ArmorSlot(armorX + 18, armorY, this, player, ItemType.AMULET));
		addSlot(new CapeSlot(armorX + 18, armorY + 18, this, player));
		addSlot(new ArmorSlot(armorX + 18, armorY + (18 * 2), this, player, ItemType.BELT));
		addSlot(new BadgeSlot(armorX + 18, armorY + (18 * 3), this, player));
		
		addSlot(new ArmorSlot(armorX + 36, armorY, this, player, ItemType.EARRING, 0));
		addSlot(new ArmorSlot(armorX + 36, armorY + 18, this, player, ItemType.EARRING, 1));
		addSlot(new ArmorSlot(armorX + 36, armorY + (18 * 2), this, player, ItemType.RING, 0));
		addSlot(new ArmorSlot(armorX + 36, armorY + (18 * 3), this, player, ItemType.RING, 1));
		
		int craftingX = armorX + 80;
		int craftingY = armorY + 18;
		
		result = new ResultSlot(craftingX + 56, craftingY + 9, this);
		addSlot(result); // Result slot
		
		craftResult = new InventoryCraftResult();
		crafting = new InventoryCrafting(((CraftPlayer) player.getPlayer()).getHandle().defaultContainer, 2, 2);
		crafting.resultInventory = craftResult;
		
		crafts[0] = new CraftingSlot(craftingX, craftingY, 0, this);
		crafts[1] = new CraftingSlot(craftingX + 18, craftingY, 1, this);
		crafts[2] = new CraftingSlot(craftingX, craftingY + 18, 2, this);
		crafts[3] = new CraftingSlot(craftingX + 18, craftingY + 18, 3, this);
		addSlot(crafts[0]);
		addSlot(crafts[1]);
		addSlot(crafts[2]);
		addSlot(crafts[3]);
	}
	
	public void setCraftingSlot(int i, ItemStack itemstack) {
		if (crafting.getItem(i) == null) {
			crafting.setItem(i, itemstack != null ? new CraftItemStack(itemstack).getHandle() : null);
		} else {
			if (itemstack == null) {
				crafting.setItem(i, null);
				return;
			}
			if (itemstack.getTypeId() == crafting.getItem(i).id) {
				crafting.getItem(i).count += itemstack.getAmount();
			} else {
				crafting.setItem(i, new CraftItemStack(itemstack).getHandle());
			}
		}
	}
	
	public void onTake() {
		final HumanEntity player = this.getPlayer();

		// If the person is taking or there is an exchange going on and they're the same type
		if (player.getItemOnCursor() == null || player.getItemOnCursor().getTypeId() == 0) {
			for (int i = 0; i < 4; i++) {
				if (crafting.getItem(i) == null || crafting.getItem(i).count - 1 <= 0) {
					crafting.setItem(i, null);
					crafts[i].setItem(new CraftItemStack(0));
				} else {
					crafting.getItem(i).count -= 1;
					ItemStack nItemStack = crafts[i].getItem().clone();
					nItemStack.setAmount(crafting.getItem(i).count);
					crafts[i].setItem(nItemStack);
				}
			}
			this.onCraft();
		}
	}
	
	public boolean onCraft() {
		final net.minecraft.server.ItemStack crafted = CraftingManager.getInstance().craft(crafting);
		if (crafted != null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(MRPG.getInstance(), new Runnable() {
				@Override
				public void run() {
					result.setItem(new CraftItemStack(crafted));
				}
			}, 1L);
			craftResult.setItem(0, crafted);
			return true;
		} else {
			result.setItem(new CraftItemStack(0));
			craftResult.setItem(0, null);
			return false;
		}
	}

	@Override
	protected Texture getBackground() {
		return Data.getInventoryBackground();
	}

	@Override
	protected Point getInventoryOffset() {
		return new Point(16, 88);
	}

	@Override
	public InventoryType getType() {
		return InventoryType.PLAYER;
	}
	
}
