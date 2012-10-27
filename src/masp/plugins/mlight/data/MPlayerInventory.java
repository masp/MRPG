package masp.plugins.mlight.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.items.MItem;
import masp.plugins.mlight.data.player.MPlayer;

import org.bukkit.inventory.ItemStack;

public class MPlayerInventory implements EffectCollection {
	
	private MPlayer player;
	
	private ItemStack[] cArmor;
	
	private ItemStack[] pet;
	
	public MPlayerInventory(MPlayer player) {
		this.player = player;
		this.cArmor = new ItemStack[8];
		this.pet = new ItemStack[4];
	}
	
	public ItemStack getAmulet() {
		return cArmor[0];
	}
	
	public ItemStack getCape() {
		return cArmor[1];
	}
	
	public ItemStack getBelt() {
		return cArmor[2];
	}
	
	public ItemStack getBadge() {
		return cArmor[3];
	}
	
	public ItemStack[] getEarrings() {
		return Arrays.asList(cArmor[4], cArmor[5]).toArray(new ItemStack[2]);
	}
	
	public ItemStack[] getRings() {
		return Arrays.asList(cArmor[6], cArmor[7]).toArray(new ItemStack[2]);
	}
	
	public void setAmulet(ItemStack item) {
		cArmor[0] = item;
	}
	
	public void setCape(ItemStack item) {
		cArmor[1] = item;
	}
	
	public void setBelt(ItemStack item) {
		cArmor[2] = item;
	}
	
	public void setBadge(ItemStack item) {
		cArmor[3] = item;
	}
	
	public void setEarringOne(ItemStack item) {
		cArmor[4] = item;
	}
	
	public void setEarringTwo(ItemStack item) {
		cArmor[5] = item;
	}
	
	public void setRingOne(ItemStack item) {
		cArmor[6] = item;
	}
	
	public void setRingTwo(ItemStack item) {
		cArmor[7] = item;
	}
	
	public ItemStack[] getPetInventory() {
		return pet;
	}
	
	public ItemStack getPet() {
		return pet[0];
	}
	
	public ItemStack getPetCollar() {
		return pet[1];
	}
	
	public ItemStack getPetChestplate() {
		return pet[2];
	}
	
	public ItemStack getPetWeapon() {
		return pet[3];
	}
	
	public MPlayer getPlayer() {
		return player;
	}

	@Override
	public double getTotalEffects(MEffect effect) {
		return getTotalEffectsDecimal(effect) + (getTotalEffectsPercent(effect) / 100) * getTotalEffectsDecimal(effect);
	}

	@Override
	public double getTotalEffectsPercent(MEffect effect) {
		double total = 0d;
		for (ItemStack item : cArmor) {
			if (item == null) continue;
			MItem mItem = MRPG.getItemManager().getItem(item);
			total += mItem.getDefense().getTotalEffectsPercent(effect);
		}
		for (ItemStack item : getPlayer().getPlayer().getInventory().getArmorContents()) {
			if (item == null) continue;
			total += MRPG.getItemManager().getItem(item).getDefense().getTotalEffectsPercent(effect);
		}
		return total;
	}

	@Override
	public double getTotalEffectsDecimal(MEffect effect) {
		double total = 0d;
		for (ItemStack item : cArmor) {
			if (item == null) continue;
			MItem mItem = MRPG.getItemManager().getItem(item);
			total += mItem.getDefense().getTotalEffectsDecimal(effect);
		}
		for (ItemStack item : getPlayer().getPlayer().getInventory().getArmorContents()) {
			if (item == null) continue;
			MItem mItem = MRPG.getItemManager().getItem(item);
			total += mItem.getDefense().getTotalEffectsDecimal(effect);
		}
		return total;
	}

	@Override
	public Set<MEffect> getEffects() {
		List<MEffect> effects = new ArrayList<MEffect>();
		for (ItemStack item : cArmor) {
			if (item == null) continue;
			MItem mItem = MRPG.getItemManager().getItem(item);
			effects.addAll(mItem.getDefense().getEffects());
		}
		for (ItemStack item : getPlayer().getPlayer().getInventory().getArmorContents()) {
			if (item == null) continue;
			MItem mItem = MRPG.getItemManager().getItem(item);
			effects.addAll(mItem.getDefense().getEffects());
		}
		return new HashSet<MEffect>(effects);
	}

}
