package me.vilmu.waterwars.events.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaUtilities;

public class MoveListener implements Listener{

	@EventHandler
	public void  move (PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Arena a = ArenaUtilities.getArenaWith(p);
		if(a.isValid() && !a.hasStarted()) {
			Location start = e.getFrom();
			Location end = e.getTo();
			if(start.getX() - end.getX() + start.getY() - end.getY() != 0) e.setCancelled(true);
		}
	}
}
