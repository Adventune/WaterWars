package me.vilmu.waterwars.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;
import me.vilmu.waterwars.events.events.ArenaStartEvent;

public class Arena {
	
	int id = -1;
	World world;
	List<Player> players = new ArrayList<Player>();
	List<Player> deadPlayers = new ArrayList<Player>();
	List<Player> alivePlayers = new ArrayList<Player>();
	List<Location> invalidLoot = new ArrayList<Location>();
	Boolean started = false;
	Boolean stopped = false;
	Plugin instance = WaterWars.getInstance();
	BukkitScheduler scheduler = Bukkit.getScheduler();
	
	@SuppressWarnings("unchecked")
	Arena(Map<String, Object> m) {
	  id = (int) m.get("id");
	  world = (World) m.get("world");
	  players = (List<Player>) m.get("players");
	  alivePlayers = (List<Player>) m.get("players");
	}
	
	Arena(){}
	
	public boolean isValid() {
		if(getId() < 0) return false;
		return true;
	}
	
	public boolean hasStarted() {
		return this.started;
	}
	
	public void start() {
		this.started = true;
		handleStartEvent(this);
		startSchedulers();
	}
	
	private static void handleStartEvent(Arena arena) {
		ArenaStartEvent event = new ArenaStartEvent(arena);
		Bukkit.getPluginManager().callEvent(event);
	}

	public void addDeadPlayer(Player p) {
		deadPlayers.add(p);
		getAlivePlayers().remove(p);
	}
	
	public void addInvalidLoot(Location loc) {
		invalidLoot.add(loc);
	}
	
	public boolean isValidLoot(Location loc) {
		if(invalidLoot.contains(loc)) return false;
		return true;
	}

	public World getWorld() {
		return world;
	}

	public List<Player> getDeadPlayers() {
		return deadPlayers;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Player> getAlivePlayers() {
		return alivePlayers;
	}

	public boolean hasStopped() {
		return stopped;
	}

	public void stop() {
		stopped = true;
		scheduler.cancelTask(border);
		scheduler.cancelTask(borderDone);
		scheduler.cancelTask(drownedDelay);
		scheduler.cancelTask(drownedTask);
		scheduler.cancelTask(drowningDelay);
		scheduler.cancelTask(drowningTask);
	}
	
	float drowningMultiplier = 0;
	float drownedMultiplier = 0;
	float timePassed = 0;
	float timePassed2 = 0;
	float drowningSpeed = 10/((float) Storage.getDrowningSpeed() * 60);
	int expDrowning = Storage.getExponentialDrowning();
	int drowningTask;
	int drownedTask;
	int spawnTask = 0;
	int timeBeforeDrowned = Storage.getTimeBeforeDrowned();
	int drownedHotzone = Storage.getDrownedHotzone();
	int drownedCount = Storage.getDrownedCount();
	int timeBeforeBorder = Storage.getTimeBeforeBorder();
	int timeToShrink = Storage.getTimeToShrink();
	int minBorder = Storage.getMinBorder();
	Random rand = new Random();
	
	int drowningDelay;
	int drownedDelay;
	int border;
	int borderDone;
	
	public void startSchedulers() {
		drowningDelay = scheduler.scheduleSyncDelayedTask(instance, new Runnable(){

			@Override
			public void run() {
				if(hasStopped()) return;
				for(Player p : getAlivePlayers()) {
					PlayerMessages.drowningBegins(p);
				}
				drowningTask = scheduler.scheduleSyncRepeatingTask(instance, new Runnable() {
					@Override
					public void run() {
						if(hasStopped()) scheduler.cancelTask(drowningTask);
						List<Player> ps = getAlivePlayers();
						if(!(ps == null || ps.isEmpty())) {
							for(Player p : ps) {
								p.damage(drowningMultiplier);
							}
						}
						if(timePassed / 60 <= expDrowning) drowningMultiplier += drowningSpeed;
						else drowningMultiplier *= 1+drowningSpeed;
						
						timePassed+=10;
						
					}
					
				}, 0L, 10*20L);
			}
			
		}, Storage.getTimeBeforeDrowning() *60*20L);
		
		drownedDelay = scheduler.scheduleSyncDelayedTask(instance, new Runnable(){

			@Override
			public void run() {
				if(hasStopped()) return;
				for(Player p : getPlayers()) {
					PlayerMessages.drownedBegins(p);
				}
				drownedTask = scheduler.scheduleSyncRepeatingTask(instance, new Runnable() {
					@Override
					public void run() {
						if(hasStopped()) scheduler.cancelTask(drownedTask);
						else {
							int max = (int) world.getWorldBorder().getSize() / drownedHotzone;
							int x = rand.nextInt(max);
							int z = rand.nextInt(max);
							if(rand.nextInt(2) -1 <=0) x*=-1;
							if(rand.nextInt(2) -1 <=0) z*=-1;
							
							Location loc = new Location(world, x, world.getHighestBlockYAt(x, z)+1,z);
							if(spawnTask != 0) scheduler.cancelTask(spawnTask);
							
							spawnTask = scheduler.scheduleSyncRepeatingTask(instance, new Runnable() {
								@Override
								public void run() {
									world.spawnEntity(loc, EntityType.DROWNED);
								}
								
							}, 0L, 10*20L / drownedCount);
						}
					}
					
				}, 0L, 10*20L);
			}
			
		}, timeBeforeDrowned *60*20L);
		
		border = scheduler.scheduleSyncDelayedTask(instance, new Runnable(){

			@Override
			public void run() {
				if(hasStopped()) return;
				for(Player p : getPlayers()) {
					PlayerMessages.borderShrinking(p);
				}
				world.getWorldBorder().setSize(minBorder*2, timeToShrink*60);
			}
			
		}, Storage.getTimeBeforeBorder() *60*20L);
		
		borderDone = scheduler.scheduleSyncDelayedTask(instance, new Runnable(){

			@Override
			public void run() {
				if(hasStopped()) return;
				for(Player p : getPlayers()) {
					PlayerMessages.borderHasShrunk(p);
				}
			}
			
		}, timeToShrink + timeBeforeBorder *60*20L);
		
		
	}

	public int getId() {
		return id;
	}
	
	
}
