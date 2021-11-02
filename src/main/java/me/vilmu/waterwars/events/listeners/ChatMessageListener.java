package me.vilmu.waterwars.events.listeners;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.arena.Arena;
import me.vilmu.waterwars.arena.ArenaUtilities;
import me.vilmu.waterwars.utilities.Ranks;

public class ChatMessageListener implements Listener{

	@EventHandler
	public void onChatMessage(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		String msg = e.getMessage();
		Player sender = e.getPlayer();
		Arena a = ArenaUtilities.getArenaWith(sender);
		World world = sender.getWorld();
		List<Player> players = world.getPlayers();
		
		if(a.isValid() && !a.hasStopped()) {
			if(a.getDeadPlayers().contains(sender)) {
				
				// MS-Rank message ---------------------------------------------------------------------------------------------------------------------
				
		        if (RankHandler.get((Object)sender).equalsIgnoreCase("none")) {
		            PlayerMessages.rankMessage((String)((Object)ChatColor.WHITE + sender.getName() + (Object)ChatColor.BLUE + " > " + (Object)ChatColor.WHITE + msg), a.getDeadPlayers(), world);
		        } else {
		            String rank = RankHandler.get((Object)sender);
		            PlayerMessages.rankMessage((String)(String.valueOf(ChatColor.translateAlternateColorCodes((char)'&', (String)rank)) 
		            		+ (Object)ChatColor.BLUE + " > " 
		            		+ (Object)ChatColor.WHITE + sender.getName() + (Object)ChatColor.BLUE + " > " + (Object)ChatColor.WHITE 
		            		+ ChatColor.translateAlternateColorCodes((char)'&', (String)msg)), a.getDeadPlayers(), world);
		        }
		        
				// MS-Rank message ---------------------------------------------------------------------------------------------------------------------
		        
				return;
			}
		}
		
		// MS-Rank message ---------------------------------------------------------------------------------------------------------------------
		
        if (RankHandler.get((Object)sender).equalsIgnoreCase("none")) {
            PlayerMessages.rankMessage((String)((Object)ChatColor.WHITE + sender.getName() + (Object)ChatColor.BLUE + " > " + (Object)ChatColor.WHITE + msg), players, world);
        } else {
            String rank = RankHandler.get((Object)sender);
            PlayerMessages.rankMessage((String)(String.valueOf(ChatColor.translateAlternateColorCodes((char)'&', (String)rank)) 
            		+ (Object)ChatColor.BLUE + " > " 
            		+ (Object)ChatColor.WHITE + sender.getName() + (Object)ChatColor.BLUE + " > " + (Object)ChatColor.WHITE 
            		+ ChatColor.translateAlternateColorCodes((char)'&', (String)msg)), players, world);
        }

		// MS-Rank message ---------------------------------------------------------------------------------------------------------------------
		
	}
	
	
	
	/*
	 * 
	 * 
	 * MULTISURVIVALS RANKS FUNCTIONS
	 * 
	 * 
	 */
	

    public static HashMap<Player, String> RankHandler = new HashMap<Player, String>();
	
	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.createPlayer(player.getName(), player);
        Rankhandler(player.getName(), player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        RankHandler.remove((Object)player);
    }


    public static void Rankhandler(String string, Player player) {
        try {
            PreparedStatement statement = Ranks.getConnection().prepareStatement("SELECT * FROM player_ranks WHERE name=?");
            statement.setString(1, player.getName());
            ResultSet results = statement.executeQuery();
            results.next();
            RankHandler.put(player, results.getString("RANK"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(Player player) {
        try {
            PreparedStatement statement = Ranks.getConnection().prepareStatement("SELECT * FROM player_ranks WHERE name=?");
            statement.setString(1, player.getName());
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                return false;
            } else {
                return true;
            }
            

            
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayer(String string, Player player) {
        try {
            PreparedStatement statement = Ranks.getConnection().prepareStatement("SELECT * FROM player_ranks WHERE name=?");
            statement.setString(1, player.getName());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!this.playerExists(player)) {
                PreparedStatement insert = Ranks.getConnection().prepareStatement("INSERT INTO player_ranks (name, rank, id) VALUES (?,?,?)");
                insert.setString(1, player.getName());
                insert.setString(2, "none");
                insert.setInt(3, 0);
                insert.executeUpdate();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
