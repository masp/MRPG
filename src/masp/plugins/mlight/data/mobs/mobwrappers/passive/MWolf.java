package masp.plugins.mlight.data.mobs.mobwrappers.passive;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalBeg;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowOwner;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLeapAtTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalOwnerHurtByTarget;
import net.minecraft.server.PathfinderGoalOwnerHurtTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRandomTargetNonTamed;
import net.minecraft.server.World;

public class MWolf extends EntityWolf {
	
	@SuppressWarnings("unchecked")
	public MWolf(World world) {
		super(world);
		
		try {
			Field goala = this.goalSelector.getClass().getDeclaredField("a");
			goala.setAccessible(true);
			((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();

			Field targeta = this.targetSelector.getClass().getDeclaredField("a");
			targeta.setAccessible(true);
			((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
		
		 	this.goalSelector.a(1, new PathfinderGoalFloat(this));
	        this.goalSelector.a(2, this.d);
	        this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
	        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, this.bw, true));
	        this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, this.bw, 10.0F, 2.0F));
	        this.goalSelector.a(6, new PathfinderGoalBreed(this, this.bw));
	        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, this.bw));
	        this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
	        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	        this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
	        
	        this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
	        this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
	        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
	        this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntitySheep.class, 16.0F, 200, false));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
