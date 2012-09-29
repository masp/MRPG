package masp.plugins.mlight.data.mobs.mobwrappers.passive;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalDefendVillage;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalOfferFlower;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class MIronGolem extends EntityIronGolem {

	
	@SuppressWarnings("unchecked")
	public MIronGolem(World world) {
		super(world);
		
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();

			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
		
		 	this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 0.25F, true));
	        this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.22F, 32.0F));
	        this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.16F, true));
	        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 0.16F));
	        this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
	        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.16F));
	        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
	        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	        this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
	        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
	        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityMonster.class, 16.0F, 0, false, true));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
