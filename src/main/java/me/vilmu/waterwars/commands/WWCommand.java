package me.vilmu.waterwars.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WWCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> buildAliases = new ArrayList<>();
		buildAliases.add("build");
		buildAliases.add("b");
		
		List<String> spawnPointAliases = new ArrayList<>();
		spawnPointAliases.add("spawnpoint");
		spawnPointAliases.add("sp");
		spawnPointAliases.add("spawn");

		List<String> newArenaAliases = new ArrayList<>();
		newArenaAliases.add("newarena");
		newArenaAliases.add("new");
		newArenaAliases.add("na");
		
		List<String> saveAliases = new ArrayList<>();
		saveAliases.add("save");
		saveAliases.add("reload");
		saveAliases.add("sync");
		saveAliases.add("syncconfig");
		
		List<String> lobbyAliases = new ArrayList<>();
		lobbyAliases.add("lobby");
		lobbyAliases.add("home");

		if(sender.hasPermission("waterwars.commands.admin")) {
			if(args.length != 0) {
				if(buildAliases.contains(args[0].toLowerCase())) {
					WWCommandBuild.onCommand(sender, cmd, label, args);
				}
				if(spawnPointAliases.contains(args[0].toLowerCase())) {
					WWCommandSpawnPoint.onCommand(sender, cmd, label, args);
				}
				if(newArenaAliases.contains(args[0].toLowerCase())) {
					WWCommandNewArena.onCommand(sender, cmd, label, args);
				}	
				if(saveAliases.contains(args[0].toLowerCase())) {
					WWCommandReload.onCommand(sender, cmd, label, args);
				}
			}
		}

		if (sender.hasPermission("waterwars.commands.player")){
			if(args.length != 0) {
				if(lobbyAliases.contains(args[0].toLowerCase())) {
					WWCommandLobby.onCommand(sender, cmd, label, args);
				}
			}
		}
		else {
			sender.sendMessage("You don't have permission!");
		}
		return true;

	}
}
