package me.vilmu.waterwars.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;

public class WWCommandReload {

	@SuppressWarnings("unchecked")
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
		if(sender instanceof Player) PlayerMessages.saving((Player) sender);
			WaterWars.console("Saving and syncing configuration...");
			
			FileConfiguration config = WaterWars.GetConfig();
			
			WaterWars.console("Saving all configuration data that has been modified in-game");
			for(int i : WaterWars.modifiedAttributes) {
				switch (i) {
				case 1:
					WaterWars.progressConsole("Saving common loot");
					config.set("loot.tiers.common", Storage.getCommonLoot());
					break;
				case 2:
					WaterWars.progressConsole("Saving rare loot");
					config.set("loot.tiers.rare", Storage.getRareLoot());
					break;
				case 3:
					WaterWars.progressConsole("Saving epic loot");
					config.set("loot.tiers.epic", Storage.getEpicLoot());
					break;
				case 4:
					WaterWars.progressConsole("Saving food loot");
					config.set("loot.tiers.food", Storage.getFoodLoot());
					break;
				case 6:
					WaterWars.progressConsole("Saving loot container type");
					config.set("loot.containers.loot", Storage.getLootCont().name());
					break;
				case 7:
					WaterWars.progressConsole("Saving food container type");
					config.set("loot.containers.food", Storage.getFoodCont().name());
					break;
				case 8:
					WaterWars.progressConsole("Saving arena names");
					config.set("arena.arenaNames", Storage.getArenaNames());
					break;
				case 9:
					
					break;
				case 10:

					break;
					
				case 11:
					WaterWars.progressConsole("Saving map center");
					Location loc = Storage.getArenaCenter();
					Map<String, Integer> center = new HashMap<String, Integer>();
					center.put("x", loc.getBlockX());
					center.put("y", loc.getBlockY());
					center.put("z", loc.getBlockZ());
					config.set("arena.arenaCenter", center);
					break;
					
				case 12:
					WaterWars.progressConsole("Saving spawn locations");
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
				
			}
			
			WaterWars.SaveConfig();
			
			
			// Save all configuration data
			
			WaterWars.console("Loading configuration data...");		
			
			//Common --------------------------------------------------------------------------------------------------------------
			
			List<HashMap<String, Integer>> commonLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.common");

			Storage.setCommonLoot(commonLoot);
			
			// Rare --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving rare loot");
			
			List<HashMap<String, Integer>> rareLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.rare");

			Storage.setRareLoot(rareLoot);
			
			// Epic --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving epic loot");
			
			List<HashMap<String, Integer>> epicLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.epic");

			Storage.setEpicLoot(epicLoot);
			
			// Food --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving food loot");
			
			List<HashMap<String, Integer>> foodLoot = (List<HashMap<String, Integer>>) config.getList("loot.tiers.food");

			Storage.setFoodLoot(foodLoot);
			
			// Loot weights --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving loot weights");
			
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
			 
			WaterWars.progressConsole("Retrieving container types for loot and food");
			
			String lootContName = config.getString("loot.containers.loot");
			Material lootCont = Material.getMaterial(lootContName);
			
			String foodContName = config.getString("loot.containers.food");
			Material foodCont = Material.getMaterial(foodContName);
			
			if(lootCont == null) {
				WaterWars.errorConsole("Material specified for loot containers is not valid! Defaulting to CHEST");
				config.set("loot.containers.loot", "CHEST");
				WaterWars.SaveConfig();
				lootCont = Material.CHEST;
			}
			
			if(foodCont == null) {
				WaterWars.errorConsole("Material specified for food containers is not valid! Defaulting to BARREL");
				config.set("loot.containers.food", "BARREL");
				WaterWars.SaveConfig();
				foodCont = Material.BARREL;
			}
			
			if(lootCont == foodCont) {
				WaterWars.errorConsole("Material specified for containers is identical! Defaulting to CHEST and BARREL");
				
				config.set("loot.containers.loot", "CHEST");
				config.set("loot.containers.food", "BARREL");
				WaterWars.SaveConfig();
				
				lootCont = Material.CHEST;
				foodCont = Material.BARREL;
			}
			
			Storage.setLootCont(lootCont);
			WaterWars.progressConsole("Container type for loot is: " + lootCont.name());
			
			Storage.setFoodCont(foodCont);
			WaterWars.progressConsole("Container type for food is: " + foodCont.name());
			
			// Map center --------------------------------------------------------------------------------------------------------------
		
			WaterWars.progressConsole("Retrieving arena center");
			
			Map<String, Integer> center = new HashMap<String, Integer>();
			center = (HashMap<String, Integer>) config.getList("arena.arenaCenter").get(0);
			Location loc = new Location(Bukkit.getWorld("waterwars"), center.get("x"), center.get("y"), center.get("z"));	
			Storage.setArenaCenter(loc);
		
			
			// Arena size --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving arena size");
			
			int arenaSize = config.getInt("arena.arenaSize");
			if(arenaSize < 1) arenaSize = 300;
			Storage.setArenaSize(arenaSize);
			
			// Time before drowning --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving time before drowning");
			
			int timeBeforeDrowning = config.getInt("arena.timeBeforeDrowning");
			if(timeBeforeDrowning <= 0) timeBeforeDrowning = 20;
			Storage.setTimeBeforeDrowning(timeBeforeDrowning);

			// Drowning speed --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving drowning speed");
			
			int drowningSpeed = config.getInt("arena.drowningSpeed");
			if(drowningSpeed <= 0) drowningSpeed = 2;
			Storage.setDrowningSpeed(drowningSpeed);

			// Time before drowning --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving exponential drowning");
			
			int exponentialDrowning = config.getInt("arena.exponentialDrowning");
			if(exponentialDrowning <= 0) exponentialDrowning = 5;
			Storage.setExponentialDrowning(exponentialDrowning);
			
			// Time before drowned --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving time before drowned spawning");
			
			int timeBeforeDrowned = config.getInt("arena.timeBeforeDrowned");
			if(timeBeforeDrowned <= 0) timeBeforeDrowned = 10;
			Storage.setTimeBeforeDrowned(timeBeforeDrowned);
			
			// Drowned hotzone --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving drowned hotzone divider");
			
			int drownedHotzone = config.getInt("arena.drownedHotzone");
			if(drownedHotzone <= 0) drownedHotzone = 1;
			Storage.setDrownedHotzone(drownedHotzone);
			
			// Drowned count --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving drowned count");
			
			int drownedCount = config.getInt("arena.drownedCount");
			if(drownedCount <= 0) drownedCount = 3;
			Storage.setDrownedCount(drownedCount);
			
			// Border shrink --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving time before border shrinks");
			
			int timeBeforeBorder = config.getInt("arena.timeBeforeBorder");
			if(timeBeforeBorder <= 0) timeBeforeBorder = 15;
			Storage.setTimeBeforeBorder(timeBeforeBorder);
			
			// Border shrink --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving time during which border shrinks");
			
			int timeToShrink = config.getInt("arena.timeToShrink");
			if(timeToShrink <= 0) timeToShrink = 10;
			Storage.setTimeToShrink(timeToShrink);
			
			// Min border size --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving minimum border size");
			
			int minBorder = config.getInt("arena.minBorder");
			if(minBorder <= 0) minBorder = 50;
			Storage.setMinBorder(minBorder);
			
			// Arena names --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving arena names");
			List<String> arenaNames = config.getStringList("arena.arenaNames");
			Storage.setArenaNames(arenaNames);
			WaterWars.progressConsole("All arena names are: " + arenaNames.toString());
			
			// Load arena maps --------------------------------------------------------------------------------------------------------------

			WaterWars.progressConsole("Loading or creating all arenas");
			for(String s : arenaNames) {
		        Bukkit.getServer().createWorld(new WorldCreator(s).environment(World.Environment.NORMAL).type(WorldType.FLAT));
			}

			WaterWars.progressConsole("All arenas loaded");

			
			// Hot zone divider --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving hot zone divider");
			
			int divider = config.getInt("arena.hotzone");
			if(divider <= 1) divider = 1;
			Storage.setHotZone(divider);
			
			// Join protocol --------------------------------------------------------------------------------------------------------------
			
			WaterWars.console("Retrieving matchmaking information");
			
			boolean queuedOnJoin = config.getBoolean("queue.queuedOnJoin");
			if(queuedOnJoin) {
				WaterWars.progressConsole("Players will be queued on join");
			}
			else {
				WaterWars.progressConsole("Players will be queued on their will");
			}
			Storage.setQueuedOnJoin(queuedOnJoin);
			
			// Queue item --------------------------------------------------------------------------------------------------------------
			
			String queueItemName = config.getString("queue.queueItem");
			Material queueItem = Material.getMaterial(queueItemName);
			
			if(queueItem == null) {
				WaterWars.errorConsole("Material specified for queue item is not valid! Defaulting to ENCHANTED_GOLDEN_APPLE");
				
				config.set("queue.queueItem", "ENCHANTED_GOLDEN_APPLE");
				WaterWars.SaveConfig();
				
				queueItem = Material.ENCHANTED_GOLDEN_APPLE;
			}
			
			
			Storage.setQueueItem(queueItem);
			WaterWars.progressConsole("Queue item is now set to: " + queueItem.name());
			
			// Spawn locations --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving spawning locations");
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
			
			WaterWars.progressConsole("Retrieving minimum player count per game");
			
			int minPlayers = config.getInt("arena.minPlayers");
			Storage.setMinPlayers(minPlayers);
						
			// Max players --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving maximum player count per game");
			
			int maxPlayers = config.getInt("arena.maxPlayers");
			Storage.setMaxPlayers(maxPlayers);
			
			// Max queue time --------------------------------------------------------------------------------------------------------------
			
			WaterWars.progressConsole("Retrieving maximum queue time");
			
			int maxQueueTime = config.getInt("arena.maxQueueTime");
			if(maxQueueTime <= 0) maxQueueTime = 300;
			Storage.setMaxQueueTime(maxQueueTime);
			
			
			WaterWars.console("All data has been saved!");
			if(sender instanceof Player) PlayerMessages.saved((Player) sender);
		}	

}
