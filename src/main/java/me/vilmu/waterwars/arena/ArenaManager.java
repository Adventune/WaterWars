package me.vilmu.waterwars.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;
import me.vilmu.waterwars.particles.FlameFairy;
import me.vilmu.waterwars.utilities.Utils;

public class ArenaManager {

	public static List<Player> queuedPlayers = new ArrayList<Player>();
	public static List<Arena> arenas = new ArrayList<Arena>();
	public static List<PrivateGame> privateGames = new ArrayList<PrivateGame>();
	static Random rand = new Random();
	private static boolean aborted = false;
	static BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	
	
	public static void startNewGame() {
		int id = ArenaUtilities.getNextEmptyId();
		if(!aborted) createNewArena(id);
		if(!aborted) teleportPlayersToArena(id);
		if(!aborted) teleportPlayersToSpawn(id);
		
		scheduler.scheduleSyncDelayedTask(WaterWars.getInstance(), new Runnable() {
			@Override
			public void run() {
				if(!aborted) countdown(id);
				if(aborted) aborted = false;
			}
		}, 5* 20L);
	}
	
	public static void startNewGame(PrivateGame pg) {
		int id = ArenaUtilities.getNextEmptyId();
		privateGames.remove(pg);
		if(!aborted) createNewArena(id, pg.getPlayers());
		if(!aborted) teleportPlayersToArena(id);
		if(!aborted) teleportPlayersToSpawn(id);
		
		scheduler.scheduleSyncDelayedTask(WaterWars.getInstance(), new Runnable() {
			@Override
			public void run() {
				if(!aborted) countdown(id);
				if(aborted) aborted = false;
			}
		}, 5* 20L);
	}



	public static void createNewArena(int id){
		List<Player> players = new ArrayList<Player>();
		
		players = ArenaUtilities.getPlayersFromQueue();
		queuedPlayers.removeAll(players);
		for(Player p : players) {
			PlayerMessages.gameStartingSoon(p);
			Arena a = ArenaUtilities.getArenaWith(p);
			if(a.isValid()) {
				a.players.remove(p);
			}
		}
		
		World world = ArenaUtilities.createWorld(id);
		if (world == null) {
			abortNewGame("There appears to be no world names listed in configuration or all names listed have been confirmed faulty! "
					+ "If worlds listed in configuration actually exist you should be contacting the developer of the plugin. Open an issue in the project github! "
					+ "Worlds that have been named faulty will be removed from the config on disable!");
			return;
		}
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.getWorldBorder().setSize(2* Storage.getArenaSize());
		world.getWorldBorder().setCenter(Storage.getArenaCenter());
		world.setAutoSave(false);
		
		if(!queuedPlayers.isEmpty()) {
			for(Player p : queuedPlayers) {
				PlayerMessages.missedGame(p);
			}
		}
		

		
		Map<String,Object> m  = new HashMap<String, Object>();
		
		m.put("id", id);
		m.put("world", world);
		m.put("players", players);
		
		Arena newArena = new Arena(m);
		
		arenas.add(newArena);
	}
	
	public static void createNewArena(int id, List<Player> players){
		for(Player p : players) {
			PlayerMessages.gameStartingSoon(p);
			Arena a = ArenaUtilities.getArenaWith(p);
			if(a.isValid()) {
				a.players.remove(p);
			}
		}
		
		World world = ArenaUtilities.createWorld(id);
		if (world == null) {
			abortNewGame("There appears to be no world names listed in configuration or all names listed have been confirmed faulty! "
					+ "If worlds listed in configuration actually exist you should be contacting the developer of the plugin. Open an issue in the project github! "
					+ "Worlds that have been named faulty will be removed from the config on disable!");
			return;
		}
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.getWorldBorder().setSize(2* Storage.getArenaSize());
		world.getWorldBorder().setCenter(Storage.getArenaCenter());
		world.setAutoSave(false);
		
		if(!queuedPlayers.isEmpty()) {
			for(Player p : queuedPlayers) {
				PlayerMessages.missedGame(p);
			}
		}
		

		
		Map<String,Object> m  = new HashMap<String, Object>();
		
		m.put("id", id);
		m.put("world", world);
		m.put("players", players);
		
		Arena newArena = new Arena(m);
		
		arenas.add(newArena);
	}

	private static void teleportPlayersToArena(int id) {
		List<Player> players = new ArrayList<Player>();
		
		Arena arena = ArenaUtilities.getArenaWith(id);
		Location loc = Storage.getArenaCenter();
		loc.setWorld(arena.getWorld());
		loc.setY(loc.getWorld().getHighestBlockYAt(loc)+1);
		players = arena.getPlayers();
		
		for(Player p : players) {
			if(!p.isOnline()) {
				arena.addDeadPlayer(p);
				continue;
			}
			p.teleport(loc);
		}
		
		updateArena(arena);
	}
	
	private static void teleportPlayersToSpawn(int id) {
		List<Player> players = new ArrayList<Player>();
		
		Arena arena = ArenaUtilities.getArenaWith(id);
		players = arena.getPlayers();
		List<Location> locs = Storage.getSpawnLoc();
		Location center = Storage.getArenaCenter();
		
		if(locs.size() < players.size()) {
			abortNewGame(arena, "There are not enough spawning spaces! Spawning spaces in config: " + locs.size() + "! Maximum players in config: " + Storage.getMaxPlayers() + "!");
			for(Player p : players) {
				p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			}
			return;
		}
		for(Player p : players) {
			int i = rand.nextInt(locs.size());
			Location loc = locs.get(i);
			locs.remove(loc);
			
			loc.setYaw(Utils.getAngle(new Vector(loc.getBlockX(), 0, loc.getBlockZ()), center.toVector()));
			loc.setWorld(p.getWorld());
			loc.add(0.5, 0, 0.5);
			
			p.teleport(loc);
			p.setFlying(false);
			PlayerMessages.teleportedToArena(p);

			for(PotionEffect ef : p.getActivePotionEffects()) {
				p.removePotionEffect(ef.getType());
			}
    		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*15, 2, false, false));
    		p.getInventory().clear();
    		p.setHealth(20);
    		p.setGameMode(GameMode.SURVIVAL);
		}
	}
	
	public static int countdown = 10;
	static int taskID = -1;

	
	private static void countdown(int id) {
		
		countdown = 10;
    	Arena arena = ArenaUtilities.getArenaWith(id);
		
    	taskID = scheduler.scheduleSyncRepeatingTask(WaterWars.getInstance(), new Runnable() {
			@Override
			public void run() {
				ArenaUtilities.sendTitle(arena.getPlayers(), ChatColor.AQUA + "" + (countdown), "", 1, 20, 20);
				countdown--;
				if(countdown < 0) {
					start(arena);
					scheduler.cancelTask(taskID);
				}
			}
    		
    	}, 0L, 20L);
    	
		
	}
	
	private static void start(Arena arena) {
		if(arena.isValid()) {
			arena.start();
			updateArena(arena);
		}
	}
	
	
	public static void abortNewGame(Arena arena, String reason) {
		aborted  = true;
		List<Player> players = arena.getPlayers();
		arenas.remove(arena);
		for(Player p : players) {
			PlayerMessages.fatalError(p);
		}
		
		ArenaUtilities.deleteWorld(arena.getWorld());
		
		WaterWars.errorConsole("Fatal error has accoured in creation of new game! REASON: " + reason);
		
	}
	
	public static void abortNewGame(String reason) {
		aborted  = true;
		for(Player p : queuedPlayers) {
			PlayerMessages.fatalError(p);
		}
		
		WaterWars.errorConsole("Fatal error has accoured in creation of new game! REASON: " + reason);
		
	}
	
	public static void updateArena (Arena arena) {
		if(arena.isValid()) {
			for(int i = 0; i < arenas.size(); i++) {
				if(arenas.get(i).getId() == arena.getId()) {
					arenas.set(i, arena);
				}
			}
			
			if(arena.getAlivePlayers().size() <= 1 && !arena.hasStopped()) {
				stop(arena);
			}
		}

	}
	
	public static void updatePrivateGame(PrivateGame pg) {		
		if(pg.isValid()) {
			for(int i = 0; i < privateGames.size(); i++) {
				if(privateGames.get(i).getJoinKey() == pg.getJoinKey()) {
					privateGames.set(i, pg);
				}
			}
		}
	}



	private static void stop(Arena a) {
		
		a.stop();
		updateArena(a);

		
		World world = a.getWorld();
		final List<Player> players = world.getPlayers();
		
		Player winner = a.getAlivePlayers().get(0);
		
		List<Player> playersNoWinner = players;
		playersNoWinner.remove(winner);
		
		winner.setGameMode(GameMode.CREATIVE);
		
		ArenaUtilities.sendTitle(winner, ChatColor.GOLD + "Congratulations!", "You won the game of WaterWars!", 20, 5*20, 20);
		PlayerMessages.gameOver(players, winner);
		ArenaUtilities.sendTitle(playersNoWinner, ChatColor.AQUA + "- - Game ended - - ", ChatColor.AQUA + "Winner: " + ChatColor.GOLD + winner.getName(), 20, 5*20, 20);
		
		for(Player p : players) {
			p.setGameMode(GameMode.CREATIVE);
		}
		int particleTask = scheduler.scheduleSyncRepeatingTask(WaterWars.getInstance(), new Runnable() {
			@Override
			public void run() {
				FlameFairy.flames(winner);
			}
			
		}, 0L, 1L);
		
		scheduler.scheduleSyncDelayedTask(WaterWars.getInstance(), new Runnable() {

			@Override
			public void run() {
				WaterWars.console("Arena of ID: " + a.getId() + " is being removed!");
				scheduler.cancelTask(particleTask);
				for(Player p : players) {
					if(p.isOnline() && p.getWorld() == world) {
						PlayerMessages.whoosh(p);
						p.teleport(Storage.getLobby().getSpawnLocation());
						p.setGameMode(GameMode.SURVIVAL);
						p.setFoodLevel(20);
						p.setHealth(20);
						
						Inventory inv = p.getInventory();
						inv.clear();
						ItemStack i = new ItemStack(Storage.getQueueItem());
						ItemMeta im = i.getItemMeta();
						im.setDisplayName(Storage.getQueueItemName());
						i.setItemMeta(im);
						
						inv.addItem(i);
						
					}
				}
				if(winner.isOnline() && winner.getWorld() == world) {
					PlayerMessages.whoosh(winner);
					winner.teleport(Storage.getLobby().getSpawnLocation());
					winner.setGameMode(GameMode.SURVIVAL);
					winner.setFoodLevel(20);
					winner.setHealth(20);
					
					Inventory inv = winner.getInventory();
					inv.clear();
					ItemStack i = new ItemStack(Storage.getQueueItem());
					ItemMeta im = i.getItemMeta();
					im.setDisplayName(Storage.getQueueItemName());
					i.setItemMeta(im);
					
					inv.addItem(i);
					
				}
				
				ArenaUtilities.unloadWorld(world);
				ArenaUtilities.deleteWorld(world);
				
				arenas.remove(a);

				WaterWars.console("Arena of ID: " + a.getId() + " has been removed!");
			}
			
		}, 5* 60* 20L);
		
	}
} 