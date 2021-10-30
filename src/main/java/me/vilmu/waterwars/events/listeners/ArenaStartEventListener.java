package me.vilmu.waterwars.events.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.events.events.ArenaStartEvent;

public class ArenaStartEventListener implements Listener {
	
	@EventHandler
	public void arenaStart(ArenaStartEvent e) {
		Arena a = e.getArena();
		a.getWorld().strikeLightningEffect(Storage.getArenaCenter());
		ArenaUtilities.sendTitle(a.getAlivePlayers(), ChatColor.GOLD + "GO!", "", 1, 20, 10);
	}
}
