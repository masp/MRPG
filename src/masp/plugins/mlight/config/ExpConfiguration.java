package masp.plugins.mlight.config;

import java.io.File;
import java.io.IOException;

import masp.plugins.mlight.MRPG;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ExpConfiguration extends Configuration {

	public ExpConfiguration(MRPG plugin, File dir) {
		super("exp-sources", plugin, dir);
	}
	
	@Override
	public void onCreate() {
		YamlConfiguration config = getConfig();
		config.set("blocks.dirt", 5.2);
		try {
			config.save(this.getFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onRead() {
		YamlConfiguration config = getConfig();
		if (config.getConfigurationSection("blocks") != null) {
			ConfigurationSection blocks = config.getConfigurationSection("blocks");
			for (String block : blocks.getKeys(false)) {
				MRPG.getExpManager().addBlockExpSource(block, blocks.getDouble(block, 0D));
			}
		}
	}

}
