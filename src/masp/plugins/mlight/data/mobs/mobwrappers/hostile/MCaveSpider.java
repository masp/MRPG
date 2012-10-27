package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import masp.plugins.mlight.data.mobs.MDangerMob;
import masp.plugins.mlight.utils.Utils;
import net.minecraft.server.EntityCaveSpider;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MCaveSpider extends EntityCaveSpider implements MDangerMob {
	
	private short dangerLevel;
	private boolean changed = false;
	
	public MCaveSpider(World world) {
		super(world);
	}
	
	public MCaveSpider(World world, Location spawn) {
		super(world);
		this.changed = true;
		this.dangerLevel = (short) Utils.getDanger(spawn.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		Utils.addMobEffects((LivingEntity) this.getBukkitEntity(), dangerLevel);
	}
	
	public boolean isChanged() {
		return changed;
	}

	@Override
	public short getDangerLevel() {
		return dangerLevel;
	}
}
