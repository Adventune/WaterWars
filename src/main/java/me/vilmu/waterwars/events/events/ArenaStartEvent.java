package me.vilmu.waterwars.events.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.vilmu.waterwars.arena.Arena;

public class ArenaStartEvent extends Event{

    private static final HandlerList HANDLERS = new HandlerList();
    
    private Arena arena;
    
    public ArenaStartEvent(Arena arena) {
        this.arena = arena;
      }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

	public Arena getArena() {
		return this.arena;
	}
	
	public World getWorld() {
		return this.arena.getWorld();
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}


