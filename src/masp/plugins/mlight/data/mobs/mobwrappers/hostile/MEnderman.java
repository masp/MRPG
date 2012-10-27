package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.mobs.MDangerMob;
import masp.plugins.mlight.utils.Utils;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityEnderman;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MEnderman extends EntityEnderman implements MDangerMob {

	private short dangerLevel;
	private boolean changed = false;
	
	public MEnderman(World world) {
		super(world);
	}
	
	public MEnderman(World world, Location spawn) {
		super(world);
		this.changed = true;
		this.dangerLevel = (short) Utils.getDanger(world.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		Utils.addMobEffects((LivingEntity) this.getBukkitEntity(), dangerLevel);
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	public short getDangerLevel() {
		return dangerLevel;
	}
	
	@Override
	protected Entity findTarget() {
        float f = this.c(1.0F);

        if (f < 0.5F) {
            double distance = 20.0D + (Settings.SIGHT_INCREASE * (double) dangerLevel);

            return this.world.findNearbyVulnerablePlayer(this, distance);
        } else {
            return null;
        }
    }

}
