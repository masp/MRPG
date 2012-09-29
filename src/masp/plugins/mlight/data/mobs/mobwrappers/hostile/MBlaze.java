package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import masp.plugins.mlight.Utils;
import masp.plugins.mlight.data.mobs.MDangerMob;
import net.minecraft.server.EntityBlaze;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MBlaze extends EntityBlaze implements MDangerMob {
	
	private short dangerLevel;
	
	private boolean changed = false;
	
	public MBlaze(World world) {
		super(world);
	}
	
	public MBlaze(World world, Location spawn) {
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
