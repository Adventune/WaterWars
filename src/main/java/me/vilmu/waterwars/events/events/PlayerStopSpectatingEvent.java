package me.vilmu.waterwars.events.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerStopSpectatingEvent extends PlayerEvent implements Cancellable{

    private static final HandlerList HANDLERS = new HandlerList();
    
    private boolean cancelled;
    private final Entity spectatorTarget;
    
    public PlayerStopSpectatingEvent(Player player, Entity spectatorTarget) {
        super(player);
        this.spectatorTarget = spectatorTarget;
      }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean bool) {
		this.cancelled = bool;
	}


	public Entity getSpectatorTarget() {
		return spectatorTarget;
	}


}
