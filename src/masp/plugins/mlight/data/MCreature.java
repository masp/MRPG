package masp.plugins.mlight.data;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.effects.EffectParticipant;
import masp.plugins.mlight.data.effects.SimpleEffectParticipant;
import masp.plugins.mlight.managers.MobEffectManager;
import masp.plugins.mlight.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;

public class MCreature extends SimpleEffectParticipant implements Damageable {
	
	private int maxHealth;
	private EntityType type;
	private double exp;
	
	public MCreature(EntityType type, int maxHealth, double exp) {
		this.setMaxHealth(maxHealth);
		this.setType(type);
		this.setExp(exp);
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}
	
	public void damage(int damage, LivingEntity attacker, LivingEntity entity) {
		
		if (damage > 0) {
			EffectCollection knocker = null;
			if (attacker instanceof Player) {
				knocker = MRPG.getPlayer(((Player) attacker).getName());
			} else {
				knocker = MRPG.getMobManager().getCreature(attacker.getType().name());
			}
			Utils.knockback(knocker, attacker, entity);
		}
		
		damage(damage, entity);
	}
	
	@Override
	public void damage(int damage, LivingEntity entity) {
		final MobEffectManager mEffManager = MRPG.getMobEffectManager();
		final EffectManager effManager = MRPG.getEffectManager();
		final EffectParticipant mobPart = mEffManager.getEffects(entity);
		
		int tempMaxHealth = maxHealth;
		if (mobPart != null) {
			tempMaxHealth += mobPart.getTotalEffects(effManager.getEffect(EffectManager.HEALTH));
		}
		
		if (damage <= 0) {
			return;
		}
		
		/*// Use animations
		Packet18ArmAnimation damagePacket = new Packet18ArmAnimation();
		damagePacket.a = entity.getEntityId();
		damagePacket.b = 2;
		for (Entity lEntity : entity.getNearbyEntities(100D, 100D, 100D)) {
			if (lEntity instanceof Player) {
				((CraftPlayer) ((Player) lEntity)).getHandle().netServerHandler.sendPacket(damagePacket);
			}
		}*/
		((CraftWorld) entity.getWorld()).getHandle().broadcastEntityEffect(((CraftLivingEntity) entity).getHandle(), (byte) 2);
		
		if (entity instanceof Slime) {
			tempMaxHealth *= ((Slime) entity).getSize() ^ 2;
		} else if (entity instanceof Wolf) {
			if (!((Wolf) entity).isTamed()) {
				tempMaxHealth /= 2;
			}
		}
		
		int currentCreatureHealth = (entity.getHealth() * tempMaxHealth) / entity.getMaxHealth();
		if (currentCreatureHealth - damage <= 0) {
			this.kill(entity);
			return;
		}
		
		currentCreatureHealth -= damage;
		
		int convertedReducedHealth = (currentCreatureHealth * entity.getMaxHealth()) / tempMaxHealth;
		if (convertedReducedHealth <= 0) {
			this.kill(entity);
			return;
		}
		
		entity.setHealth(convertedReducedHealth);
	}
	
	public void kill(final LivingEntity entity) {
		((CraftWorld) entity.getWorld()).getHandle().broadcastEntityEffect(((CraftLivingEntity) entity).getHandle(), (byte) 3);
		Bukkit.getScheduler().scheduleSyncDelayedTask(MRPG.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						entity.getWorld().playEffect(entity.getLocation().add(0, 0.5f, 0), Effect.SMOKE, 10);
						entity.remove();
					}
				}, 20L
		);
		entity.setHealth(0);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (o instanceof MCreature) {
			return ((MCreature) o).getType().equals(getType());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getType().hashCode();
	}

	public double getExp() {
		return exp;
	}

	public void setExp(double exp) {
		this.exp = exp;
	}
	
}
