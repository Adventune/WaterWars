package me.vilmu.waterwars.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.vilmu.waterwars.WaterWars;

public class ConfigManager {
	
	private WaterWars plugin = WaterWars.getPlugin(WaterWars.class);

	// Files & File Configs Here
	public FileConfiguration cfg;
	public File configfile;
	// --------------------------

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		configfile = new File(plugin.getDataFolder(), "config.yml");

		if (!configfile.exists()) {
			try {
				configfile.createNewFile();
				WaterWars.progressConsole(ChatColor.AQUA + "The config.yml file has been created");
			} catch (IOException e) {
				WaterWars.errorConsole("Could not create the config.yml file");
			}
		}

		cfg = YamlConfiguration.loadConfiguration(configfile);
	}

	public FileConfiguration loadConfig() {
		return cfg;
	}

	public void saveConfig() {
		if(!configfile.exists()) {
			WaterWars.errorConsole("The config.yml file does not exist! If you have moved it, please return it to its original folder. Else please restart the server to regenerate the default config.yml file!");
			return;
		}
		
		try {
			cfg.save(configfile);
			ConfigUpdater.update(plugin, "config.yml", configfile, Arrays.asList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WaterWars.console("The config.yml file has been saved");
	}
}
