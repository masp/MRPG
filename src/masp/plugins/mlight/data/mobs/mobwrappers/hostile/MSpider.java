package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.mobs.MDangerMob;
import masp.plugins.mlight.utils.Utils;
import net.minecraft.server.Entity;
import net.minecraft.server.EntitySpider;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MSpider extends EntitySpider implements MDangerMob {
	
	private short dangerLevel;
	private boolean changed = false;
	
	public MSpider(World world) {
		super(world);
	}
	
	public MSpider(World world, Location spawn) {
		super(world);
		
		this.changed = true;

		dangerLevel = (short) Utils.getDanger(world.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		this.bw = this.bw + Settings.MOB_SPEED_INCREASE_RATE * (float) Utils.getDanger(world.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		Utils.addMobEffects((LivingEntity) this.getBukkitEntity(), dangerLevel);
	}
	
	public short getDangerLevel() {
		return dangerLevel;
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	@Override
	protected Entity findTarget() {
        float f = this.c(1.0F);

        if (f < 0.5F) {
            double distance = 16.0D + (Settings.SIGHT_INCREASE * (double) dangerLevel);

            return this.world.findNearbyVulnerablePlayer(this, distance);
        } else {
            return null;
        }
    }

}
