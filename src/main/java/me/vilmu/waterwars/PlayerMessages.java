package me.vilmu.waterwars;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.arena.PrivateGame;

public class PlayerMessages {
		
	static String watermark = ChatColor.ITALIC + "" + ChatColor.BOLD + "WaterWars > ";

	public static void addedContainer(Player p) { // Message to send when player doesn't have permission for particle
		p.sendMessage(watermark + ChatColor.AQUA + "Added a new container to configuration!");
	}
	public static void containerExists(Player p) { // Message to send when player doesn't have permission for particle
		p.sendMessage(watermark + ChatColor.AQUA + "This container already exists in configuration!");
	}
	public static void containerRemoved(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Removed container from configuration!");
	}
	public static void queuedToGame(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "You are now queued to a new game!");
	}
	public static void removedFromQueue(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "You have been removed from queue!");
	}
	public static void missedGame(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "OH! We are sorry but it seems like you just missed this game. You are now queued to a new game which will begin shortly!");
	}
	public static void fatalError(Player p) {
		p.sendMessage(watermark + ChatColor.RED + "Fatal error has occured! Please notify staff about this if possible!");
	}
	
	public static void gameStartingSoon(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Game is now starting... Please be patient this might take some time!");
	}
	
	public static void teleportedToArena(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "You have been teleported to the arena!");		
	}
	public static void createdSpawnPoint(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "New spawn point created!");		

	}
	public static void saving(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Saving all changed data!");
		
	}
	public static void saved(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Saving completed!");
		
	}
	
	public static void killed(Player p, int place, int max, List<Player> players) {
		p.sendMessage(watermark + ChatColor.AQUA + "You died! You placed " + place + "/" + max +" overall!");
		for(Player p2 : players) {
			if(p2 == p) continue;
			p2.sendMessage(watermark + ChatColor.AQUA + p.getName() + " died! There are " + (place - 1) +" players left!");
		}
	}
	
	public static void backHome(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Use command: " + ChatColor.WHITE + "/ww lobby" + ChatColor.AQUA +" to get back to lobby!");
	}
	public static void whoosh(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Whoooosh! You were teleported!");
	}
	public static void cantBeArena(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "This world cannot be made an arena");
	}
	public static void newArenaUsage(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Usage: /ww newarena <this|create>");
	}
	public static void buildUsage(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Usage: /ww build <world>");
	}
	public static void worldNotFound(Player p) {
		// TODO Auto-generated method stub
		p.sendMessage(watermark + ChatColor.AQUA + "World not found!");
		
	}
	public static void gameOver(List<Player> players, Player winner) {
		for(Player p : players) {
			p.sendMessage(watermark + ChatColor.AQUA + "The game has ended and winner declared!");
			p.sendMessage(watermark + ChatColor.AQUA + "Feel free to smash the arena to oblivion in creative for the next 5 minutes! After that you will be returned to the lobby and the map is deleted!");
			p.sendMessage(watermark + ChatColor.GOLD + "Thanks for playing!");
		}
		winner.sendMessage(watermark + ChatColor.AQUA + "The game has ended and winner declared!");
		winner.sendMessage(watermark + ChatColor.AQUA + "Feel free to smash the arena to oblivion in creative for the next 5 minutes! After that you will be returned to the lobby and the map is deleted!");
		winner.sendMessage(watermark + ChatColor.GOLD + "Thanks for playing!");
		
	}
	public static void drowningBegins(Player p) {
		p.sendMessage(watermark + ChatColor.GOLD + "You are starting to drown! Take the win quickly!");
	}
	public static void drownedBegins(Player p) {
		p.sendMessage(watermark + ChatColor.GOLD + "Drowned zombies are starting to spawn!");
	}
	public static void borderShrinking(Player p) {
		p.sendMessage(watermark + ChatColor.GOLD + "Border is now starting to shrink!");
	}
	public static void borderHasShrunk(Player p) {
		p.sendMessage(watermark + ChatColor.GOLD + "Border has now shrunk!");
	}
	public static void chatMessage(Player sender, List<Player> players, String msg, World w) {
		for(Player p : players) {
			p.sendMessage("<" + sender.getDisplayName()+"> " + msg);
		}
		WaterWars.sender.sendMessage(w.getName() + ": <" + sender.getDisplayName()+"> " + msg);
	}
	public static void rankMessage(String msg, List<Player> players, World w) {
		for(Player p : players) {
			p.sendMessage(msg);
		}
		WaterWars.sender.sendMessage(w.getName() + ": " + msg);
	}
	public static void createdNewArena(Player p, String name) {
		p.sendMessage(watermark + ChatColor.AQUA + "New arena world has been created with name: " + name +"!");
	}
	public static void privateGameNotValid(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "This private game does not exist!");
	}
	public static void joinedPrivateGame(Player p, Player p2) {
		p.sendMessage(watermark + ChatColor.AQUA + "You have been added to a private game hosted by: " + p2.getName() +"!");
		p.sendMessage(ChatColor.GRAY + "To leave right click the queue item!");
	}
	public static void cantUseAsPGOwner(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "You can't join public games when you are hosting a private game!");
	}
	public static void notHostingPrivateGame(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "You are not currently hosting a private game!");
	}
	public static void privateGameCancelled(PrivateGame pg) {
		for(Player p : pg.getPlayers()) {
			p.sendMessage(watermark + ChatColor.AQUA + "Private game has been cancelled!");
		}
	}
	public static void privateGameCreated(Player p, String joinKey) {
		p.sendMessage(watermark + ChatColor.AQUA + "You are now hosting a private game! Anyone can now join using this command: " + ChatColor.GOLD + "/ww privategame join " + joinKey);
	}
	public static void privateGameFull(Player p) {
		p.sendMessage(watermark + ChatColor.AQUA + "Unfortunately this private game is already full! Try again a bit later!");
	}
}
