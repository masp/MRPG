package masp.plugins.mlight.data.player;

import java.util.HashSet;
import java.util.Set;

import masp.plugins.mlight.MLight;
import masp.plugins.mlight.data.MItem;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.types.MEffect;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
				return MLight.getInstance().getItemManager().getItem(getPlayer().getInventory().getHelmet().getTypeId());
			}
		}
		return MLight.getInstance().getItemManager().getItem(0);
	}
	
	public MItem getBody() {
		if (getPlayer() != null) {
			if (getPlayer().getInventory().getChestplate() != null) {
				return MLight.getInstance().getItemManager().getItem(getPlayer().getInventory().getChestplate().getTypeId());
			}
		}
		return MLight.getInstance().getItemManager().getItem(0);
	}
	
	public MItem getLeggings() {
		if (getPlayer() != null) {
			if (getPlayer().getInventory().getLeggings() != null) {
				return MLight.getInstance().getItemManager().getItem(getPlayer().getInventory().getLeggings().getTypeId());
			}
		}
		return MLight.getInstance().getItemManager().getItem(0);
	}
	
	public MItem getBoots() {
		if (getPlayer() != null) {
			if (getPlayer().getInventory().getBoots() != null) {
				return MLight.getInstance().getItemManager().getItem(getPlayer().getInventory().getBoots().getTypeId());
			}
		}
		return MLight.getInstance().getItemManager().getItem(0);
	}

	@Override
	public double getTotalEffects(MEffect effect) {
		return getHead().getDefense().getTotalEffects(effect) +
				getBody().getDefense().getTotalEffects(effect) +
				getLeggings().getDefense().getTotalEffects(effect) +
				getBoots().getDefense().getTotalEffects(effect);
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
