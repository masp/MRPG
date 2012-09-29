package masp.plugins.mlight.data.mobs.mobwrappers.passive;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntitySnowman;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class MSnowGolem extends EntitySnowman {

	@SuppressWarnings("unchecked")
	public MSnowGolem(World world) {
		super(world);
		
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();

			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
		
			this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 0.25F, 2, 20));
	        this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 0.2F));
	        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
	        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
	        
	        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityMonster.class, 16.0F, 0, true));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
