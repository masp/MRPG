package masp.plugins.mlight.data.player;

import java.util.HashSet;
import java.util.Set;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.items.MItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorParticipant implements EffectCollection {
	
	private String player;
	
	public ArmorParticipant(String player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}
	
	public MItem getHead() {
		if (getPlayer() != null) {
			if (getPlayer().getInventory().getHelmet() != null) {
				return MRPG.getItemManager().getItem(getPlayer().getInventory().getHelmet());
			}
		}
		return MRPG.getItemManager().getItem(new ItemStack(Material.AIR));
	}
	
	public MItem getBody() {
		if (getPlayer() != null) {
			if (getPlayer().getInventory().getChestplate() != null) {
				return MRPG.getItemManager().getItem(getPlayer().getInventory().getChestplate());
			}
		}
		return MRPG.getItemManager().getItem(new ItemStack(Material.AIR));
	}
	
	public MItem getLeggings() {
		if (getPlayer() != null) {
			if (getPlayer().getInventory().getLeggings() != null) {
				return MRPG.getItemManager().getItem(getPlayer().getInventory().getLeggings());
			}
		}
		return MRPG.getItemManager().getItem(new ItemStack(Material.AIR));
	}
	
	public MItem getBoots() {
		if (getPlayer() != null) {
			if (getPlayer().getInventory().getBoots() != null) {
				return MRPG.getItemManager().getItem(getPlayer().getInventory().getBoots());
			}
		}
		return MRPG.getItemManager().getItem(new ItemStack(Material.AIR));
	}

	@Override
	public double getTotalEffects(MEffect effect) {
		return ((getTotalEffectsPercent(effect) / 100D) * getTotalEffectsDecimal(effect)) + getTotalEffectsDecimal(effect);
	}

	@Override
	public double getTotalEffectsPercent(MEffect effect) {
		return getHead().getDefense().getTotalEffectsPercent(effect) +
				getBody().getDefense().getTotalEffectsPercent(effect) +
				getLeggings().getDefense().getTotalEffectsPercent(effect) +
				getBoots().getDefense().getTotalEffectsPercent(effect);
	}

	@Override
	public double getTotalEffectsDecimal(MEffect effect) {
		return getHead().getDefense().getTotalEffectsDecimal(effect) +
				getBody().getDefense().getTotalEffectsDecimal(effect) +
				getLeggings().getDefense().getTotalEffectsDecimal(effect) +
				getBoots().getDefense().getTotalEffectsDecimal(effect);
	}

	@Override
	public Set<MEffect> getEffects() {
		Set<MEffect> effects = new HashSet<MEffect>();
		effects.addAll(getHead().getDefense().getEffects());
		effects.addAll(getBody().getDefense().getEffects());
		effects.addAll(getLeggings().getDefense().getEffects());
		effects.addAll(getBoots().getDefense().getEffects());
		return effects;
	}	
}
