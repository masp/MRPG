package masp.plugins.mlight.data.effects;

import masp.plugins.mlight.data.effects.types.MEffect;

public interface EffectParticipant extends EffectCollection {
	
	public void setEffectPercent(MEffect effect, double value);
	
	public void setEffectDecimal(MEffect effect, double value);
	
}
