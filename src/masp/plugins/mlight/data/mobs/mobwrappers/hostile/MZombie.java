package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import java.lang.reflect.Field;
import java.util.List;

import masp.plugins.mlight.Settings;
import masp.plugins.mlight.Utils;
import masp.plugins.mlight.data.mobs.MDangerMob;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalBreakDoor;
import net.minecraft.server.PathfinderGoalFleeSun;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MZombie extends EntityZombie implements MDangerMob {
	
	private short dangerLevel;
	private boolean changed = false;
	
	public MZombie(World world) {
		super(world);
	}
	
	@SuppressWarnings("unchecked")
	public MZombie(World world, Location spawn) {
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

			this.goalSelector.a(0, new PathfinderGoalFloat(this));
			this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
			this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, attackSpeed, false));
			this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, attackSpeed, true));
	        this.goalSelector.a(3, new PathfinderGoalFleeSun(this, attackSpeed));
			this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, this.bw));
			this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, this.bw, false));
			this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, this.bw));
			this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));

			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
		} catch (Exception e) {
			e.printStackTrace();
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
