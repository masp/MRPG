package masp.plugins.mlight.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import masp.plugins.mlight.data.effects.EffectParticipant;
import masp.plugins.mlight.data.effects.SimpleEffectParticipant;

import org.bukkit.entity.LivingEntity;

public class MobEffectManager {
	
	private Map<UUID, EffectParticipant> mobs;
	
	public MobEffectManager() {
		mobs = new HashMap<UUID, EffectParticipant>();
	}
	
	public EffectParticipant getEffects(UUID id) {
		return mobs.get(id);
	}
	
	public EffectParticipant getEffects(LivingEntity entity) {
		return getEffects(entity.getUniqueId());
	}
	
	public EffectParticipant addMobParticipant(LivingEntity entity, EffectParticipant participant) {
		if (participant == null) return participant;
		
		if (!mobs.containsKey(entity.getUniqueId())) {
			mobs.put(entity.getUniqueId(), participant);
		}
		
		return participant;
	}
	
	public EffectParticipant addMobParticipant(LivingEntity entity) {
		return addMobParticipant(entity, new SimpleEffectParticipant());
	}
	
	public boolean hasEffects(UUID id) {
		return mobs.containsKey(id);
	}
	
	public boolean hasEffects(LivingEntity entity) {
		return hasEffects(entity.getUniqueId());
	}
	
	public boolean removeEffects(UUID id) {
		return (mobs.remove(id) != null);
	}
	
	public boolean removeEffects(LivingEntity entity) {
		return (removeEffects(entity.getUniqueId()));
	}
	
}
