package masp.plugins.mlight.data.mobs.mobwrappers.passive;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalAvoidPlayer;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowOwner;
import net.minecraft.server.PathfinderGoalJumpOnBlock;
import net.minecraft.server.PathfinderGoalLeapAtTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalOzelotAttack;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRandomTargetNonTamed;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.World;

public class MOcelot extends EntityOcelot {
	
	private PathfinderGoalTempt e;
	
	@SuppressWarnings("unchecked")
	public MOcelot(World world) {
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
	        this.goalSelector.a(3, this.e = new PathfinderGoalTempt(this, 0.18F, Item.RAW_FISH.id, true));
	        this.goalSelector.a(4, new PathfinderGoalAvoidPlayer(this, EntityHuman.class, 16.0F, 0.23F, 0.4F));
	        this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 0.3F, 10.0F, 5.0F));
	        this.goalSelector.a(6, new PathfinderGoalJumpOnBlock(this, 0.4F));
	        this.goalSelector.a(7, new PathfinderGoalLeapAtTarget(this, 0.3F));
	        this.goalSelector.a(8, new PathfinderGoalOzelotAttack(this));
	        this.goalSelector.a(9, new PathfinderGoalBreed(this, 0.23F));
	        this.goalSelector.a(10, new PathfinderGoalRandomStroll(this, 0.23F));
	        this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
	        
	        this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed(this, EntityChicken.class, 14.0F, 750, false));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public boolean c(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (this.isTamed()) {
            if (entityhuman.name.equalsIgnoreCase(this.getOwnerName()) && !this.world.isStatic && !this.b(itemstack)) {
                this.d.a(!this.isSitting());
            }
        } else if (this.e.f() && itemstack != null && itemstack.id == Item.RAW_FISH.id && entityhuman.e(this) < 9.0D) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
            }

            if (itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
            }

            if (!this.world.isStatic) {
                if (this.random.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.setCatType(1 + this.world.random.nextInt(3));
                    this.setOwnerName(entityhuman.name);
                    this.e(true);
                    this.d.a(true);
                    this.world.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.e(false);
                    this.world.broadcastEntityEffect(this, (byte) 6);
                }
            }

            return true;
        }

        return super.c(entityhuman);
    }
	
}
