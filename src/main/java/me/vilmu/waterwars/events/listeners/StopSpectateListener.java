package me.vilmu.waterwars.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.events.events.PlayerStopSpectatingEvent;

public class StopSpectateListener implements Listener{

	@EventHandler
	public void stopSpectate(PlayerStopSpectatingEvent e) {
		Player p = e.getPlayer();
		Arena arena = ArenaUtilities.getArenaWith(p);
		if(arena.hasStarted()) return;
		if(arena.isValid())	e.setCancelled(true);
	}
}
