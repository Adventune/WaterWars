package me.vilmu.waterwars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.commands.WWCommand;
import me.vilmu.waterwars.events.handlers.PlayerStopSpectatingEventHandler;
import me.vilmu.waterwars.events.listeners.ArenaStartEventListener;
import me.vilmu.waterwars.events.listeners.DamageListener;
import me.vilmu.waterwars.events.listeners.DeathListener;
import me.vilmu.waterwars.events.listeners.GameContainerListener;
import me.vilmu.waterwars.events.listeners.JoinMethods;
import me.vilmu.waterwars.events.listeners.MoveListener;
import me.vilmu.waterwars.events.listeners.QuitListener;
import me.vilmu.waterwars.events.listeners.StopSpectateListener;
import me.vilmu.waterwars.utilities.ConfigManager;


public class WaterWars extends JavaPlugin {
	// Variables
	Plugin plugin;
	private static WaterWars instance;
	
	public static List<Integer> modifiedAttributes = new ArrayList<Integer>();

	static ConsoleCommandSender sender;
	
	private static ConfigManager cfgm;

	@SuppressWarnings("unchecked")
	@Override
    public void onEnable() { // Actions when server starts, restarts or reloads! 
		
		
		// Listener classes ------------------------------------------------------
			Bukkit.getPluginManager().registerEvents(new JoinMethods(), this);	
			Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
			Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
			Bukkit.getPluginManager().registerEvents(new PlayerStopSpectatingEventHandler(), this);
			Bukkit.getPluginManager().registerEvents(new StopSpectateListener(), this);
			Bukkit.getPluginManager().registerEvents(new GameContainerListener(), this);
			Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
			Bukkit.getPluginManager().registerEvents(new ArenaStartEventListener(), this);
			Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
			
		// Listener classes ------------------------------------------------------

			
		// Command classes ------------------------------------------------------
			getCommand("ww").setExecutor(new WWCommand());



		// Command classes ------------------------------------------------------
			
			sender = Bukkit.getConsoleSender();
			instance = this;
		

		// Configuration ------------------------------------------------------

			if (!(new File("plugins/WaterWars/config.yml").exists())) {
				this.saveDefaultConfig(); 
				console("Default confiuration saved");
			}
			
			// Save all configuration data
						
			console("Loading configuration data...");		
			
			FileConfiguration config = LoadConfig();
			
			//Common --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving common loot");
			
			List<HashMap<String, Integer>> commonLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.common");

			Storage.setCommonLoot(commonLoot);
			
			// Rare --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving rare loot");
			
			List<HashMap<String, Integer>> rareLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.rare");

			Storage.setRareLoot(rareLoot);
			
			// Epic --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving epic loot");
			
			List<HashMap<String, Integer>> epicLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.epic");

			Storage.setEpicLoot(epicLoot);
			
			// Food --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving food loot");
			
			List<HashMap<String, Integer>> foodLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.food");

			Storage.setFoodLoot(foodLoot);
			
			// Loot weights --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving loot weights");
			
			List<Map<String, Integer>> lootWeights = (List<Map<String, Integer>>) config.getList("loot.lootWeights");
			HashMap<String, Integer> weightHash = new HashMap<String, Integer>();
			
			for(Map<String, Integer> map : lootWeights) {
				weightHash.putAll(map);
			}
			int common = weightHash.get("common");
			int rare = weightHash.get("rare");
			int epic = weightHash.get("epic");
			if(common + rare + epic > 100) {
				int over = common + rare + epic -100;
				if( over % 2 == 0) {
					rare -= over/2;
					epic -= over/2;
				} else {
					rare -= (over-1)/2;
					epic -= (over+1)/2;
				}
				
				weightHash = new HashMap<String, Integer>();
				weightHash.put("common", common);
				weightHash.put("rare", rare);
				weightHash.put("epic", epic);
			}
			
			Storage.setLootWeights(weightHash);
			
			// Loot container types --------------------------------------------------------------------------------------------------------------
			 
			progressConsole("Retrieving container types for loot and food");
			
			String lootContName = config.getString("loot.containers.loot");
			Material lootCont = Material.getMaterial(lootContName);
			
			String foodContName = config.getString("loot.containers.food");
			Material foodCont = Material.getMaterial(foodContName);
			
			if(lootCont == null) {
				errorConsole("Material specified for loot containers is not valid! Defaulting to CHEST");
				config.set("loot.containers.loot", "CHEST");
				SaveConfig();
				lootCont = Material.CHEST;
			}
			
			if(foodCont == null) {
				errorConsole("Material specified for food containers is not valid! Defaulting to BARREL");
				config.set("loot.containers.food", "BARREL");
				SaveConfig();
				foodCont = Material.BARREL;
			}
			
			if(lootCont == foodCont) {
				errorConsole("Material specified for containers is identical! Defaulting to CHEST and BARREL");
				
				config.set("loot.containers.loot", "CHEST");
				config.set("loot.containers.food", "BARREL");
				SaveConfig();
				
				lootCont = Material.CHEST;
				foodCont = Material.BARREL;
			}
			
			Storage.setLootCont(lootCont);
			progressConsole("Container type for loot is: " + lootCont.name());
			
			Storage.setFoodCont(foodCont);
			progressConsole("Container type for food is: " + foodCont.name());
			
			// Map center --------------------------------------------------------------------------------------------------------------
		
			progressConsole("Retrieving arena center");
			
			Map<String, Integer> center = new HashMap<String, Integer>();
			center = (HashMap<String, Integer>) config.getList("arena.arenaCenter").get(0);
			Location loc = new Location(Bukkit.getWorld("waterwars"), center.get("x"), center.get("y"), center.get("z"));	
			Storage.setArenaCenter(loc);
		
			
			// Arena size --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving arena size");
			
			int arenaSize = config.getInt("arena.arenaSize");
			if(arenaSize < 1) arenaSize = 300;
			Storage.setArenaSize(arenaSize);
			
			// Arena names --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving arena names");
			List<String> arenaNames = config.getStringList("arena.arenaNames");
			Storage.setArenaNames(arenaNames);
			progressConsole("All arena names are: " + arenaNames.toString());
			
			// Load arena maps --------------------------------------------------------------------------------------------------------------

			console("Loading or creating all arenas");
			for(String s : arenaNames) {
		        Bukkit.getServer().createWorld(new WorldCreator(s).environment(World.Environment.NORMAL).type(WorldType.FLAT));
			}

			console("All arenas loaded");

			// Hot zone divider --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving hot zone divider");
			
			int divider = config.getInt("arena.hotzone");
			if(divider <= 1) divider = 1;
			Storage.setHotZone(divider);

			// Time before drowning --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving time before drowning");
			
			int timeBeforeDrowning = config.getInt("arena.timeBeforeDrowning");
			if(timeBeforeDrowning <= 0) timeBeforeDrowning = 20;
			Storage.setTimeBeforeDrowning(timeBeforeDrowning);

			// Drowning speed --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving drowning speed");
			
			int drowningSpeed = config.getInt("arena.drowningSpeed");
			if(drowningSpeed <= 0) drowningSpeed = 2;
			Storage.setDrowningSpeed(drowningSpeed);

			// Exponential drowning --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving exponential drowning");
			
			int exponentialDrowning = config.getInt("arena.exponentialDrowning");
			if(exponentialDrowning <= 0) exponentialDrowning = 5;
			Storage.setExponentialDrowning(exponentialDrowning);
			
			// Time before drowned --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving time before drowned spawning");
			
			int timeBeforeDrowned = config.getInt("arena.timeBeforeDrowned");
			if(timeBeforeDrowned <= 0) timeBeforeDrowned = 10;
			Storage.setTimeBeforeDrowned(timeBeforeDrowned);
			
			// Drowned hotzone --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving drowned hotzone divider");
			
			int drownedHotzone = config.getInt("arena.drownedHotzone");
			if(drownedHotzone <= 0) drownedHotzone = 1;
			Storage.setDrownedHotzone(drownedHotzone);
			
			// Drowned count --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving drowned count");
			
			int drownedCount = config.getInt("arena.drownedCount");
			if(drownedCount <= 0) drownedCount = 3;
			Storage.setDrownedCount(drownedCount);
			
			// Border shrink --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving time before border shrinks");
			
			int timeBeforeBorder = config.getInt("arena.timeBeforeBorder");
			if(timeBeforeBorder <= 0) timeBeforeBorder = 15;
			Storage.setTimeBeforeBorder(timeBeforeBorder);
			
			// Border shrink --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving time during which border shrinks");
			
			int timeToShrink = config.getInt("arena.timeToShrink");
			if(timeToShrink <= 0) timeToShrink = 10;
			Storage.setTimeToShrink(timeToShrink);
			
			// Min border size --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving minimum border size");
			
			int minBorder = config.getInt("arena.minBorder");
			if(minBorder <= 0) minBorder = 50;
			Storage.setMinBorder(minBorder);
			
			// Join protocol --------------------------------------------------------------------------------------------------------------
			
			console("Retrieving matchmaking information");
			
			boolean queuedOnJoin = config.getBoolean("queue.queuedOnJoin");
			if(queuedOnJoin) {
				progressConsole("Players will be queued on join");
			}
			else {
				progressConsole("Players will be queued on their will");
			}
			Storage.setQueuedOnJoin(queuedOnJoin);
			
			// Queue item --------------------------------------------------------------------------------------------------------------
			
			String queueItemName = config.getString("queue.queueItem");
			Material queueItem = Material.getMaterial(queueItemName);
			
			if(queueItem == null) {
				errorConsole("Material specified for queue item is not valid! Defaulting to ENCHANTED_GOLDEN_APPLE");
				
				config.set("queue.queueItem", "ENCHANTED_GOLDEN_APPLE");
				SaveConfig();
				
				queueItem = Material.ENCHANTED_GOLDEN_APPLE;
			}
			
			
			Storage.setQueueItem(queueItem);
			progressConsole("Queue item is now set to: " + queueItem.name());
			
			// Spawn locations --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving spawning locations");
			List<HashMap<String, Integer>> spawnLocations = new ArrayList<HashMap<String, Integer>>();
			spawnLocations = (List<HashMap<String, Integer>>) config.getList("locations.spawnLocations");
			List<Location> spawnLoc = new ArrayList<Location>();
			
			if(spawnLocations == null || spawnLocations.isEmpty()) {
				Storage.setSpawnLoc(spawnLoc);
			}
			else {
				for(HashMap<String, Integer> map1 : spawnLocations) {
					if(map1 != null) {
						Location loc1 = new Location(Bukkit.getWorld("world"), map1.get("x"), map1.get("y"), map1.get("z"));
						spawnLoc.add(loc1);
					}
				}
				Storage.setSpawnLoc(spawnLoc);
			}
			
			// Min players --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving minimum player count per game");
			
			int minPlayers = config.getInt("arena.minPlayers");
			Storage.setMinPlayers(minPlayers);
						
			// Max players --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving maximum player count per game");
			
			int maxPlayers = config.getInt("arena.maxPlayers");
			Storage.setMaxPlayers(maxPlayers);
			
			// Max queue time --------------------------------------------------------------------------------------------------------------
			
			progressConsole("Retrieving maximum queue time");
			
			int maxQueueTime = config.getInt("arena.maxQueueTime");
			if(maxQueueTime <= 0) maxQueueTime = 300;
			Storage.setMaxQueueTime(maxQueueTime);
			
			
			
			console("Configuration information has now been retrieved and automatically modified if that was needed!");
			console("Modifications to configuration will be listed in error messages");
			console("All modifications made in-game (with commands or container tool etc.) will be saved on disable");
			console("Any unsaved data will be lost if plugin does not get a chance to disable properly");
			
			console("Starting schedulers");
			startScheduler();
			
			console("Schedulers have been started successfully");	
			
			
		// Instance
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[WaterWars] Enabling successful");
    }
	
	public static void console(String s) {
		sender.sendMessage(ChatColor.AQUA + "[WaterWars] " + s);
	}
	
	public static void debug(Object o) {
		sender.sendMessage("Debug: " + String.valueOf(o));
	}
	
	public static void errorConsole(String s) {
		sender.sendMessage(ChatColor.RED + "[WaterWars] ERROR: " + s);
		
	}
	
	public static void progressConsole(String s) {
		sender.sendMessage("...." + s);
		
	}
	
	public static void commandErrorConsole(int i) {
		switch (i) {
		case 0:
			sender.sendMessage("[WaterWars] ERROR: Seems like the sender has too little flesh around their bones. This command can't be executed from console!");
			
		}
	}

	static int taskID;
	
	public static void startScheduler() {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		taskID = scheduler.scheduleSyncRepeatingTask(getInstance(), new Runnable() {
			@Override
			public void run() {
				ArenaUtilities.queuedPlayersActionBar();
				if(ArenaUtilities.testQueueTimer()) scheduler.cancelTask(taskID);
			}
		}, 0L, 20L);
	}
	
	@Override
    public void onDisable() { // Actions when server shuts down
		
		console("Disabling... ");
		
		FileConfiguration config = LoadConfig();
		
		console("Saving all configuration data that has been modified in-game");
		for(int i : modifiedAttributes) {
			switch (i) {
			case 1:
				progressConsole("Saving common loot");
				config.set("loot.tiers.common", Storage.getCommonLoot());
				break;
			case 2:
				progressConsole("Saving rare loot");
				config.set("loot.tiers.rare", Storage.getRareLoot());
				break;
			case 3:
				progressConsole("Saving epic loot");
				config.set("loot.tiers.epic", Storage.getEpicLoot());
				break;
			case 4:
				progressConsole("Saving food loot");
				config.set("loot.tiers.food", Storage.getFoodLoot());
				break;
			case 6:
				progressConsole("Saving loot container type");
				config.set("loot.containers.loot", Storage.getLootCont().name());
				break;
			case 7:
				progressConsole("Saving food container type");
				config.set("loot.containers.food", Storage.getFoodCont().name());
				break;
			case 8:
				progressConsole("Saving arena names");
				config.set("arena.arenaNames", Storage.getArenaNames());
				break;
			case 9:
				
				break;
			case 10:

				break;
				
			case 11:
				progressConsole("Saving map center");
				Location loc = Storage.getArenaCenter();
				Map<String, Integer> center = new HashMap<String, Integer>();
				center.put("x", loc.getBlockX());
				center.put("y", loc.getBlockY());
				center.put("z", loc.getBlockZ());
				config.set("arena.arenaCenter", center);
				break;
				
			case 12:
				progressConsole("Saving spawn locations");
				List<HashMap<String,Integer>> maps2 = new ArrayList<HashMap<String,Integer>>();
				List<Location> spawnLocs = Storage.getSpawnLoc();
				for(Location loc1 : spawnLocs) {
					HashMap<String,Integer> map = new HashMap<String,Integer>();
					map.put("x", loc1.getBlockX());
					map.put("y", loc1.getBlockY());
					map.put("z", loc1.getBlockZ());
					
					maps2.add(map);
				}
				
				config.set("locations.spawnLocations", maps2);
				break;
			}
			SaveConfig();
		}
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[WaterWars] Disabling successful");
    }
	

	public static Plugin getInstance() { // Getter for plugin instance
		return instance;
		
	}
	
	public static FileConfiguration LoadConfig() {
			cfgm = new ConfigManager();
			cfgm.setup();
			return cfgm.loadConfig();
	}
	
	public static void SaveConfig() {
		cfgm.saveConfig();
	}
}
