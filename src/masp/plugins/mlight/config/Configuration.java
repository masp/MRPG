package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MRPG;
import masp.plugins.mlight.data.effects.EffectParticipant;
import masp.plugins.mlight.data.effects.types.MEffect;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Configuration {
	
	private MRPG plugin;
	private YamlConfiguration config;
	private String name;
	private File dir;
	
	public Configuration(String name, MRPG plugin, File dir) {
		this.plugin = plugin;
		this.dir = dir;
		this.name = name;
		
		config = YamlConfiguration.loadConfiguration(getFile());
	}
	
	public String getName() {
		return name;
	}
	
	public MRPG getPlugin() {
		return plugin;
	}
	
	public File getFile() {
		File file = new File(dir, name + ".yml");
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			try {
				file.createNewFile();
				config = YamlConfiguration.loadConfiguration(file);
				onCreate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public YamlConfiguration getConfig() {
		return config;
	}
	
	public File getDirectory() {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	public EffectParticipant loadEffects(EffectParticipant participant, String path) {
		if (getConfig().getConfigurationSection(path + ".decimal") != null) {
			for (String effectName : getConfig().getConfigurationSection(path + ".decimal").getKeys(false)) {
				MEffect effect = MRPG.getEffectManager().getEffect(effectName);
				if (effect == null) {
					getPlugin().getLogger().info(
							"Configuration Error: No effect by name " + effectName + " for path " + path + ".decimal." + effect);
					return participant;
				}
				
				participant.setEffectDecimal(effect, getConfig().getDouble(path + ".decimal." + effectName));
			}
		}
		
		if (getConfig().getConfigurationSection(path + ".percent") != null) {
			for (String effectName : getConfig().getConfigurationSection(path + ".percent").getKeys(false)) {
				MEffect effect = MRPG.getEffectManager().getEffect(effectName);
				if (effect == null) {
					getPlugin().getLogger().info(
							"Configuration Error: No effect by name " + effectName + " for path " + path + ".decimal." + effect);
					return participant;
				}
				
				participant.setEffectPercent(effect, getConfig().getDouble(path + ".percent." + effectName));
			}
		}
		return participant;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (!(o instanceof Configuration)) {
			return false;
		}
		return ((Configuration) o).getName().equalsIgnoreCase(getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	public abstract void onCreate();
	
	public abstract void onRead();
	
}
