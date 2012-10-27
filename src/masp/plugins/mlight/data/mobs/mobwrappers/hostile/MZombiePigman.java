package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import masp.plugins.mlight.data.mobs.MDangerMob;
import masp.plugins.mlight.utils.Utils;
import net.minecraft.server.EntityPigZombie;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MZombiePigman extends EntityPigZombie implements MDangerMob {
	
	private short dangerLevel;
	private boolean changed = false;
	
	public MZombiePigman(World world) {
		super(world);
	}
	
	public MZombiePigman(World world, Location spawn) {
		super(world);
		this.changed = true;
		this.dangerLevel = (short) Utils.getDanger(spawn.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		Utils.addMobEffects((LivingEntity) this.getBukkitEntity(), dangerLevel);
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	public short getDangerLevel() {
		return dangerLevel;
	}
	
}
