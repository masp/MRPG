package masp.plugins.mlight.data.mobs.mobwrappers.hostile;

import java.lang.reflect.Field;
import java.util.List;

import masp.plugins.mlight.Settings;
import masp.plugins.mlight.Utils;
import masp.plugins.mlight.data.mobs.MDangerMob;
import masp.plugins.mlight.data.mobs.ai.PathfinderGoalMSwell;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalAvoidPlayer;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MCreeper extends EntityCreeper implements MDangerMob {
	
	private short dangerLevel;
	private boolean changed = false;
	
	public MCreeper(World world) {
		super(world);
	}
	
	@SuppressWarnings("unchecked")
	public MCreeper(World world, Location spawn) {
		super(world);
		changed = true;
		this.dangerLevel = (short) Utils.getDanger(world.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		float attackSpeed = 0.25f + Settings.MOB_SPEED_INCREASE_RATE * (float) Utils.getDanger(world.getWorld(), spawn.getBlockX(), spawn.getBlockZ());
		
		this.bw = 0.2f;
		
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();

			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();

			 this.goalSelector.a(1, new PathfinderGoalFloat(this));
	        this.goalSelector.a(2, new PathfinderGoalMSwell(this));
	        this.goalSelector.a(3, new PathfinderGoalAvoidPlayer(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
	        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, attackSpeed, false));
	        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, this.bw));
	        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
	        
	        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
	        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
	        
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
