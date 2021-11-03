package me.vilmu.waterwars.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.WaterWars;
import me.vilmu.waterwars.arena.ArenaManager;
import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.arena.PrivateGame;

public class WWCommandPrivateGame {
	public static void create(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return;
		} else {
			Player p = (Player) sender;
			
			HashMap<String, Object> hm = new HashMap<String,Object>();
			String joinKey = ArenaUtilities.getJoinKey();
			hm.put("owner", p);
			hm.put("joinKey", joinKey);
			
			PrivateGame pg = new PrivateGame(hm);
			
			ArenaManager.privateGames.add(pg);
			
			PlayerMessages.privateGameCreated(p, joinKey);
			
		}
	}
	
	public static void start(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return;
		} else {
			Player p = (Player) sender;
			PrivateGame pg = ArenaUtilities.getPrivateGameWith(p);
			if(pg.isValid()) {
				if(pg.getOwner() == p) {
					
					ArenaManager.startNewGame(pg);
					
				} else {
					PlayerMessages.notHostingPrivateGame(p);
				}
			} else {
				PlayerMessages.notHostingPrivateGame(p);
			}
			
		}
	}
	
	
	public static void cancel(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return;
		} else {
			Player p = (Player) sender;
			
			PrivateGame pg = ArenaUtilities.getPrivateGameWith(p);
			if(pg.isValid()) {
				if(pg.getOwner() == p) {
					
					PlayerMessages.privateGameCancelled(pg);
					ArenaManager.privateGames.remove(pg);
					
					
				} else {
					PlayerMessages.notHostingPrivateGame(p);
				}
			} else {
				PlayerMessages.notHostingPrivateGame(p);
			}
			
		}
	}
	
	public static void join(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return;
		} else {
			Player p = (Player) sender;
			
			String joinKey = args[2];
			PrivateGame pg = ArenaUtilities.getPrivateGameWith(joinKey);
			if(pg.isValid()) {
				boolean joined = pg.addPlayer(p);
				if(joined) {
					
					ArenaManager.updatePrivateGame(pg);
					
					PlayerMessages.joinedPrivateGame(p, pg.getOwner());
				} else PlayerMessages.privateGameFull(p);
				
			} else {
				PlayerMessages.privateGameNotValid(p);
			}
		}
	}
}
