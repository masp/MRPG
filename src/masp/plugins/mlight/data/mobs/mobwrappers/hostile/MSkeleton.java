package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import java.lang.reflect.Field;
import java.util.List;

import masp.plugins.mlight.Settings;
import masp.plugins.mlight.data.mobs.MDangerMob;
import masp.plugins.mlight.utils.Utils;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.PathfinderGoalFleeSun;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRestrictSun;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MSkeleton extends EntitySkeleton implements MDangerMob {

	private short dangerLevel;
	private boolean changed = false;
	
	public MSkeleton(World world) {
		super(world);
	}
	
	@SuppressWarnings("unchecked")
	public MSkeleton(World world, Location spawn) {
		super(world);
		
		changed = true;
		
		this.dangerLevel = (short) Utils.getDanger(world.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		float attackSpeed = this.bw + Settings.MOB_SPEED_INCREASE_RATE * (float) Utils.getDanger(world.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();

			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
			
			this.goalSelector.a(1, new PathfinderGoalFloat(this));
	        this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
	        this.goalSelector.a(3, new PathfinderGoalFleeSun(this, attackSpeed));
	        this.goalSelector.a(4, new PathfinderGoalArrowAttack(this, attackSpeed, 1, 60));
	        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, this.bw));
	        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
	        
	        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
	        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
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
            double distance = 16.0D + (Settings.SIGHT_INCREASE * (double) dangerLevel);

            return this.world.findNearbyVulnerablePlayer(this, distance);
        } else {
            return null;
        }
    }

}
