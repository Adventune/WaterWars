package me.vilmu.waterwars.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaManager;
import me.vilmu.waterwars.arena.ArenaUtilities;

public class WWCommandLobby {

	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			// Console sent the command
			WaterWars.commandErrorConsole(0);
			return;
		} else {
			Player p = (Player) sender;
			Arena a = ArenaUtilities.getArenaWith(p);
			if(a.isValid()) {
				if(!a.getDeadPlayers().contains(p) && !a.hasStopped()) {
					a.addDeadPlayer(p);
					a.getWorld().strikeLightningEffect(p.getLocation());

					int place = a.getAlivePlayers().size() + 1;
					
					PlayerMessages.playerLeftGame(p, place, a.getAlivePlayers().size() + a.getDeadPlayers().size(), a.getPlayers());
					
					ArenaManager.updateArena(a);
				}
			}
			
			PlayerMessages.whoosh(p);
			Location loc = Storage.getLobby().getSpawnLocation();
			loc.add(0.5, 0, 0.5);
			p.teleport(loc);
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

}
