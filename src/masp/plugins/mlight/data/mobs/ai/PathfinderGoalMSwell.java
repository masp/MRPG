package masp.plugins.mlight.data.mobs.ai;

import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoalSwell;

public class PathfinderGoalMSwell extends PathfinderGoalSwell {
	
	private EntityCreeper a;
	private EntityLiving b;
	
	public PathfinderGoalMSwell(EntityCreeper creeper) {
		super(creeper);
		
		this.a = creeper;
		
		this.a(2);
	}
	
	@Override
	public boolean a() {
        EntityLiving entityliving = this.a.az();

        return this.a.p() > 0 || entityliving != null && this.a.e(entityliving) < 9.0D;
    }

	@Override
    public void e() {
        this.a.getNavigation().g();
        this.b = this.a.az();
    }

	@Override
    public void c() {
        this.b = null;
    }
	
	@Override
	public void d() {
        if (this.b == null) {
            this.a.a(-1);
        } else if (this.a.e(this.b) > 49.0D) {
            this.a.a(-1);
        } else if (!this.a.at().canSee(this.b)) {
            this.a.a(-1);
        } else {
            this.a.a(2);
        }
    }

}
