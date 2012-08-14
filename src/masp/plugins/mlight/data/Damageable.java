package masp.plugins.mlight.data;

import org.bukkit.entity.LivingEntity;

public interface Damageable {

	// Yeah, I know, laziness overtook me
	public void damage(int damage, LivingEntity data);
	
}
