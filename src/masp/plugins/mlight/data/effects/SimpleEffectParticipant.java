package masp.plugins.mlight.data.effects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.effects.types.MEffect;

public class SimpleEffectParticipant implements EffectParticipant {

	private Map<MEffect, EffectData> effects = new HashMap<MEffect, EffectData>();
	
	public SimpleEffectParticipant() {
		for (MEffect effect : MRPG
				.getEffectManager()
				.getEffects()) {
			effects.put(effect, new EffectData());
		}
	}
	
	@Override
	public double getTotalEffects(MEffect effect) {
		return (getTotalEffectsDecimal(effect) * (getTotalEffectsPercent(effect) / 100D)) + getTotalEffectsDecimal(effect);
	}

	@Override
	public Set<MEffect> getEffects() {
		Set<MEffect> data = new HashSet<MEffect>();
		for (MEffect effect : effects.keySet()) {
			if (effects.get(effect).getDecimal() != 0 || effects.get(effect).getPercent() != 0) {
				data.add(effect);
			}
		}
		return data;
	}

	@Override
	public void setEffectPercent(MEffect effect, double value) {
		if (!effects.containsKey(effect)) {
			effects.put(effect, new EffectData());
		}
		effects.get(effect).setPercent(value);
	}
	
	@Override
	public void setEffectDecimal(MEffect effect, double value) {
		if (!effects.containsKey(effect)) {
			effects.put(effect, new EffectData());
		}
		effects.get(effect).setDecimal(value);
	}
	
	@Override
	public double getTotalEffectsPercent(MEffect effect) {
		return effects.get(effect).getPercent();
	}

	@Override
	public double getTotalEffectsDecimal(MEffect effect) {
		return effects.get(effect).getDecimal();
	}
	
	private class EffectData {
		
		private double decimal;
		private double percent;
		
		public EffectData() {
			setDecimal(0);
			setPercent(0);
		}

		public double getDecimal() {
			return decimal;
		}

		public void setDecimal(double decimal) {
			this.decimal = decimal;
		}

		public double getPercent() {
			return percent;
		}

		public void setPercent(double percent) {
			this.percent = percent;
		}
		
	}

}
