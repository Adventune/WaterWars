package me.vilmu.waterwars.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaManager;
import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.arena.PrivateGame;

public class WWCommandPrivateGame implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 0) {
			if(args.length >= 1) {
				String s = args[0];
				if(s.equalsIgnoreCase("create")) {
					if(sender.hasPermission("waterwars.commands.privategame.host")) {
						create(sender, cmd, s, args);
						return true;
					} else sender.sendMessage("You don't have permission!"); return true;
					
				}
				else if(s.equalsIgnoreCase("start")) {
					if(sender.hasPermission("waterwars.commands.privategame.host")) {
						start(sender, cmd, s, args);
						return true;
					} else sender.sendMessage("You don't have permission!"); return true;
					
				}
				else if(s.equalsIgnoreCase("cancel")) {
					if(sender.hasPermission("waterwars.commands.privategame.host")) {
						cancel(sender, cmd, s, args);
						return true;
					} else sender.sendMessage("You don't have permission!"); return true;
					
				}
				else if(s.equalsIgnoreCase("join")) {
					if(sender.hasPermission("waterwars.commands.privategame.join")) {
						join(sender, cmd, s, args);
						return true;
					} else sender.sendMessage("You don't have permission!"); return true;
					
				}
			} else return true;

		} else return true;
		
		return true;
	}
	
	public static void create(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return;
		} else {
			Player p = (Player) sender;
			
			if(ArenaUtilities.isPGOwner(p)) {
				PlayerMessages.alreadyHostingPG(p);
				return;
			}
			if(ArenaUtilities.getPrivateGameWith(p).isValid()) {
				PlayerMessages.alreadyInPG(p);
				return;
			}
			if(ArenaUtilities.getArenaWith(p).isValid()) {
				PlayerMessages.alreadyInGame(p);
				return;
			}
			HashMap<String, Object> hm = new HashMap<String,Object>();
			String joinKey = ArenaUtilities.getJoinKey();
			hm.put("owner", p);
			hm.put("joinKey", joinKey);
			
			PrivateGame pg = ArenaManager.newPg(hm);
			
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
					if(pg.getPlayers().size() >= Storage.getMinPlayers()) {
						ArenaManager.startNewGame(pg);
					} else {
						PlayerMessages.notEnoughPlayes(p);
					}
					
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
			if(args.length >= 2) {
				String joinKey = args[1];
				PrivateGame pg = ArenaUtilities.getPrivateGameWith(joinKey);
				
				PrivateGame pg2 = ArenaUtilities.getPrivateGameWith(p);
				if(pg2.isValid()) {
					PlayerMessages.alreadyInPG(p);
					return;
				}
				
				if(pg.isValid()) {
					
					Arena a = ArenaUtilities.getArenaWith(p);
					if(a.isValid() && a.getAlivePlayers().contains(p)) {
						PlayerMessages.alreadyInGame(p);
					} else {
						if(a.isValid()) {
							a.removePlayer(p);
							ArenaManager.updateArena(a);
						}
						if(ArenaManager.queuedPlayers.contains(p)) {
							ArenaManager.queuedPlayers.remove(p);
							PlayerMessages.removedFromQueue(p);
						}
						boolean joined = pg.addPlayer(p);
						if(joined) {
							ArenaManager.updatePrivateGame(pg);
							PlayerMessages.joinedPrivateGame(p, pg.getOwner());
						} else PlayerMessages.privateGameFull(p);
					}

					
				} else PlayerMessages.privateGameNotValid(p);
			} else PlayerMessages.privateGameNotValid(p);

		}
	}
}
