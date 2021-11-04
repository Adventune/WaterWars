package me.vilmu.waterwars.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.bukkit.entity.Player;

import me.vilmu.waterwars.Storage;

public class PrivateGame {
	String joinKey;
	List<Player> players = new ArrayList<Player>();;
	Player owner;
	boolean isValid = false;
	
	PrivateGame(HashMap<String, Object> m) {
	  this.joinKey = (String) m.get("joinKey");
	  this.players.add((Player) m.get("owner"));
	  this.owner = (Player) m.get("owner");
	  this.isValid = true;
	}
	
	PrivateGame(){}
	
	public boolean isValid() {
		return isValid;
	}
	
	public boolean addPlayer(Player p) {
		if(players.size() >= Storage.getMaxPlayers()) return false;
		return players.add(p);
	}

	public String getJoinKey() {
		return joinKey;
	}

	public Player getOwner() {
		return owner;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void removePlayer(Player p) {
		players.remove(p);
	}
	
}
