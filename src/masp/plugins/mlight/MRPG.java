package masp.plugins.mlight;

import java.util.ArrayList;
import java.util.List;

import masp.plugins.mlight.config.Configuration;
import masp.plugins.mlight.config.CustomItemConfiguration;
import masp.plugins.mlight.config.DatabaseConfiguration;
import masp.plugins.mlight.config.ItemConfiguration;
import masp.plugins.mlight.config.MobConfiguration;
import masp.plugins.mlight.config.SkillClassConfiguration;
import masp.plugins.mlight.data.effects.EffectManager;
import masp.plugins.mlight.data.effects.types.MEffect;
import masp.plugins.mlight.data.player.MPlayer;
import masp.plugins.mlight.exceptions.EffectNotFoundException;
import masp.plugins.mlight.gui.GuiHandler;
import masp.plugins.mlight.listeners.DamageListener;
import masp.plugins.mlight.listeners.GeneralListener;
import masp.plugins.mlight.listeners.MobListener;
import masp.plugins.mlight.listeners.PlayerReplaceListener;
import masp.plugins.mlight.managers.AttributeManager;
import masp.plugins.mlight.managers.DataManager;
import masp.plugins.mlight.managers.ItemManager;
import masp.plugins.mlight.managers.MobEffectManager;
import masp.plugins.mlight.managers.MobManager;
import masp.plugins.mlight.managers.PlayerManager;
import masp.plugins.mlight.threads.EffectMonitorThread;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.FileManager;

public class MRPG extends JavaPlugin implements Listener {
	
	private DatabaseConfiguration dataConfig;
	
	private static DataManager dManager;
	private static PlayerManager pManager;
	private static ItemManager iManager;
	private static MobManager mManager;
	private static EffectManager eManager;
	private static AttributeManager aManager;
	private static MobEffectManager meManager;
	
	private static GuiHandler gHandler;
	
	private List<Configuration> configs = new ArrayList<Configuration>();
	
	@Override
	public void onLoad() {
		this.dataConfig = new DatabaseConfiguration(this, getDataFolder());
		addConfig(dataConfig);
		
		dManager = new DataManager(this);
		pManager = new PlayerManager();
		iManager = new ItemManager();
		mManager = new MobManager();
		eManager = new EffectManager();
		aManager = new AttributeManager();
		gHandler = new GuiHandler();
		meManager = new MobEffectManager();
	}
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		// Spout
		this.addFilesToCache();
		
		this.registerKeys();
		
		TestCommands commands = new TestCommands();
		
		// Events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new GeneralListener(), this);
		pm.registerEvents(this, this);
		pm.registerEvents(gHandler, this);
		pm.registerEvents(new MobListener(), this);
		pm.registerEvents(new PlayerReplaceListener(), this);
		
		// Register commands (test)
		this.getCommand("mattr").setExecutor(commands);
		pm.registerEvents(commands, this);
		
		addConfig(new ItemConfiguration(this, this.getDataFolder()));
		addConfig(new CustomItemConfiguration(this, this.getDataFolder()));
		addConfig(new MobConfiguration(this, this.getDataFolder()));
		addConfig(new SkillClassConfiguration(this, this.getDataFolder()));
		
		for (Configuration config : configs) {
			config.onRead();
		}
		
		dManager.init();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(
				this, 
				new EffectMonitorThread(), 
				20L, 
				20L);
		
		getLogger().info("Plugin was successfully enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Plugin was successfully disabled!");
	}
	
	public void addFilesToCache() {
		FileManager fm = SpoutManager.getFileManager();
		fm.addToPreLoginCache(this, "https://dl.dropbox.com/u/26497130/label1_1.png");
		fm.addToPreLoginCache(this, "https://dl.dropbox.com/u/26497130/attribute_background.png");
	}
	
	public void registerKeys() {
		SpoutManager.getKeyBindingManager().registerBinding("attribute-menu-button", 
				Keyboard.KEY_K, 
				"Opens the Attribute Menu", 
				gHandler,
				this);
	}
	
	public static MEffect getEffect(String effect) throws EffectNotFoundException {
		try {
			return getEffectManager().getEffect(effect);
		} catch (EffectNotFoundException ex) {
			throw ex;
		}
	}
	
	public static GuiHandler getGuiHandler() {
		return gHandler;
	}
	
	public static AttributeManager getAttributeManager() {
		return aManager;
	}
	
	public static EffectManager getEffectManager() {
		return eManager;
	}
	
	public static DataManager getDataManager() {
		return dManager;
	}
	
	public static MobEffectManager getMobEffectManager() {
		return meManager;
	}
	
	public static PlayerManager getPlayerManager() {
		return pManager;
	}
	
	public static MobManager getMobManager() {
		return mManager;
	}
	
	public static ItemManager getItemManager() {
		return iManager;
	}
	
	public static MPlayer getPlayer(String name) {
		return pManager.getPlayer(name);
	}
	
	public static MPlayer getPlayer(Player player) {
		return pManager.getPlayer(player);
	}
	
	public void addConfig(Configuration config) {
		if (!configs.contains(config)) {
			configs.add(config);
		}
	}
	
	public DatabaseConfiguration getDataConfig() {
		return dataConfig;
	}
	
	private static MRPG instance;
	public static MRPG getInstance() {
		return instance;
	}
	
}
