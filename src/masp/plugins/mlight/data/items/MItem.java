package masp.plugins.mlight.data.items;

import java.util.HashSet;
import java.util.Set;

import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.EffectParticipant;
import masp.plugins.mlight.data.effects.SimpleEffectParticipant;
import masp.plugins.mlight.data.effects.types.MEffect;

import org.bukkit.inventory.ItemStack;

public class MItem implements EffectCollection {
	
	private int id;
	private byte data;
	private boolean custom = false;
	
	private EffectParticipant attack;
	private EffectParticipant defense;
	
	public MItem(int id, byte data, boolean custom) {
		this.id = id;
		this.data = data;
		
		attack = new SimpleEffectParticipant();
		defense = new SimpleEffectParticipant();
	}
	
	public MItem(ItemStack item, boolean custom) {
		this(custom ? item.getDurability() : item.getTypeId(), item.getData().getData(), custom);
	}
	
	public boolean isCustom() {
		return custom;
	}
	
	public int getId() {
		return id;
	}
	
	public short getData() {
		return data;
	}
	
	public EffectParticipant getAttack() {
		return attack;
	}
	
	public EffectParticipant getDefense() {
		return defense;
	}

	@Override
	public double getTotalEffects(MEffect effect) {
		return getAttack().getTotalEffects(effect) + getDefense().getTotalEffects(effect);
	}

	@Override
	public double getTotalEffectsPercent(MEffect effect) {
		return getAttack().getTotalEffectsPercent(effect) + getDefense().getTotalEffectsPercent(effect);
	}

	@Override
	public double getTotalEffectsDecimal(MEffect effect) {
		return getAttack().getTotalEffectsDecimal(effect) + getDefense().getTotalEffectsDecimal(effect);
	}

	@Override
	public Set<MEffect> getEffects() {
		Set<MEffect> effects = new HashSet<MEffect>(getAttack().getEffects());
		effects.addAll(getDefense().getEffects());
		return effects;
	}
}
