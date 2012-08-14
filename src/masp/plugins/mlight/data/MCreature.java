package masp.plugins.mlight.data;

import masp.plugins.mlight.MLight;
import masp.plugins.mlight.Utils;
import masp.plugins.mlight.data.effects.EffectCollection;
import masp.plugins.mlight.data.effects.SimpleEffectParticipant;

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
	
	public MCreature(EntityType type, int maxHealth) {
		this.setMaxHealth(maxHealth);
		this.setType(type);
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
				knocker = MLight.getInstance().getPlayer(((Player) attacker).getName());
			} else {
				knocker = MLight.getInstance().getMobManager().getCreature(attacker.getType().name());
			}
			Utils.knockback(knocker, attacker, entity);
		}
		
		damage(damage, entity);
	}
	
	@Override
	public void damage(int damage, LivingEntity entity) {
		int tempMaxHealth = maxHealth;
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
		entity.setHealth(convertedReducedHealth);
	}
	
	public void kill(final LivingEntity entity) {
		((CraftWorld) entity.getWorld()).getHandle().broadcastEntityEffect(((CraftLivingEntity) entity).getHandle(), (byte) 3);
		entity.setHealth(0);
		Bukkit.getScheduler().scheduleSyncDelayedTask(MLight.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						entity.getWorld().playEffect(entity.getLocation().add(0, 0.5f, 0), Effect.SMOKE, 10);
						entity.remove();
					}
				}, 20L
		);
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
	
}
