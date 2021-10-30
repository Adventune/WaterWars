package me.vilmu.waterwars.events.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaManager;
import me.vilmu.waterwars.arena.ArenaUtilities;

public class QuitListener implements Listener{
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
	
		Player p = e.getPlayer();
		Inventory inv = p.getInventory();

		ArenaUtilities.removePlayerFromQueue(p);
		if(inv.contains(Storage.getQueueItem())) inv.remove(Storage.getQueueItem());
		
		Arena a = ArenaUtilities.getArenaWith(p);
		if(a.isValid()) {
			a.addDeadPlayer(p);
			ArenaManager.updateArena(a);
		}
		p.setGameMode(GameMode.SURVIVAL);
	}
}
