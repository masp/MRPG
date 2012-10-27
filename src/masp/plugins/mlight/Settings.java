package masp.plugins.mlight;

public class Settings {
	
	public static int DEFAULT_MAX_HEALTH = 100;
	public static int DEFAULT_MAX_MANA = 100;
	public static int DEFAULT_MAX_STAMINA = 100;
	
	public static float DEFAULT_EXP_MULTIPLIER = 1.f;

	public static int DEFAULT_SKILL_POINTS = 1;
	public static long DEFAULT_ATTACK_SPEED = 600;
	public static int DANGER_HEALTH_INCREASE = 3;
	public static int DANGER_DAMAGE_INCREASE = 3;
	
	public static int AP_PER_LEVEL = 3;
	
	public static boolean DROP_EXP = true;
	
	public static int DEFAULT_MAX_WEIGHT = 100;
	
	public static boolean DEBUG = true;
	
	public static int EXP_RATE = 300;
	
	public static int HEALTH_CONVERSION = DEFAULT_MAX_HEALTH / 20;
	public static int STAMINA_CONVERSION = DEFAULT_MAX_STAMINA / 20;
	public static int MANA_CONVERSION = DEFAULT_MAX_MANA / 20;
	
	public static int STAMINA_ZERO_DAMAGE = 5;
	
	public static float STAMINA_DAMAGE_DECREASE_LIMIT = 20; // If player's stamina is this or lower, they will lose STAMINA_DAMAGE_DECREASE
	public static float STAMINA_DAMAGE_DECREASE = 50;
	
	public static int STAMINA_HIT_LOSS = 5;
	public static int STAMINA_DAMAGED_LOSS = 5;
	
	public static int MAX_TRACED_BLOCKS = 1000;
	public static long BLOCK_TRACKING_DURATION = 10 * 60 * 1000;
	
	public static int DANGER_DISTANCE = 50;
	
	public static double SIGHT_INCREASE = 0.2D;
	
	public static float MOB_SPEED_INCREASE_RATE = 0.01f;
	
	public static int CACTUS_DAMAGE = 5;
	public static int DROWNING_DAMAGE = 5;
	// 3 damage for every block
	public static int FALL_DAMAGE = 3;
	public static int FIRE_DAMAGE = 5;
	public static int LAVA_DAMAGE = 50;
	public static int LIGHTNING_DAMAGE = 20;
	public static int POTION_DAMAGE = 30;
	public static int MELT_DAMAGE = 5;
	public static int POISON_DAMAGE = 10;
	public static int STARVATION_DAMAGE = 5;
	public static int SUFFOCATION_DAMAGE = 5;
	
}
