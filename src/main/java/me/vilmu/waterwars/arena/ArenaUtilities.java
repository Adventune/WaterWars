package me.vilmu.waterwars.arena;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import me.vilmu.waterwars.PlayerMessages;
import me.vilmu.waterwars.Storage;
import me.vilmu.waterwars.WaterWars;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ArenaUtilities {

	// Helper functions
	// ------------------------------------------------------------------------

	public static void queuePlayer(Player p) {
		ArenaManager.queuedPlayers.add(p);
		PlayerMessages.queuedToGame(p);

		if (ArenaManager.queuedPlayers.size() >= Storage.getMaxPlayers()) {
			ArenaManager.startNewGame();
			stopQueueTimer();
			return;
		}

		if (ArenaManager.queuedPlayers.size() >= Storage.getMinPlayers()) {
			startQueueTimer();
		}

		else {
			startQueueTimer();
			pauseQueueTimer();
		}

	}

	public static void removePlayerFromQueue(Player p) {

		if (ArenaManager.queuedPlayers.contains(p)) {
			ArenaManager.queuedPlayers.remove(p);
			if (p.isOnline())
				PlayerMessages.removedFromQueue(p);
		}

		if (ArenaManager.queuedPlayers.size() < Storage.getMinPlayers()) {
			pauseQueueTimer();
		}
		if (ArenaManager.queuedPlayers.size() == 0) {
			stopQueueTimer();
		}

	}

	public static Arena getArenaWith(int id) {
		for (Arena arena : ArenaManager.arenas) {
			if (arena.getId() == id)
				return arena;
		}
		return new Arena();
	}

	public static Arena getArenaWith(World world) {
		for (Arena arena : ArenaManager.arenas) {
			if (arena.getWorld() == world)
				return arena;
		}
		return new Arena();
	}

	public static Arena getArenaWith(Player p) {
		for (Arena arena : ArenaManager.arenas) {
			if (arena.getPlayers().contains(p))
				return arena;
		}
		return new Arena();
	}

	private static void startQueueTimer() {
		QueueTimer.start(Storage.getMaxQueueTime());
		WaterWars.startScheduler();
	}

	private static void stopQueueTimer() {
		QueueTimer.stop();
	}

	private static void pauseQueueTimer() {
		QueueTimer.pause();
	}

	public static boolean testQueueTimer() {
		if (QueueTimer.test() == 2) {
			QueueTimer.stop();
			ArenaManager.startNewGame();
			return true;
		}
		return false;
	}

	public static void queuedPlayersActionBar() {
		String message = "";
		int queuedPlayers = ArenaManager.queuedPlayers.size();
		int maxPlayers = Storage.getMaxPlayers();
		int test = QueueTimer.test();
		int playersInQueue = Math.min(queuedPlayers, Storage.getMaxPlayers());
		ChatColor color = ChatColor.AQUA;

		if (test == 1) {
			message = color + "Players in queue: " + playersInQueue + "/" + maxPlayers + " Starting the game in: "
					+ QueueTimer.getTimeRemaining();
		} else if (test == 0) {
			message = color + "Players in queue: " + playersInQueue + "/" + maxPlayers;
		} else if (test == 2) {
			message = color + "Players in queue: " + playersInQueue + "/" + maxPlayers + " Game is now starting...";
		}

		sendActionbar(ArenaManager.queuedPlayers, message);
		
		for(PrivateGame pg : ArenaManager.privateGames) {
			List<Player> players = pg.getPlayers();
			message = color + "Players in private game (" + pg.getJoinKey() + "): " + players.size() + "/" + maxPlayers;
			sendActionbar(players, message);
		}

	}

	public static World createWorld(int id) {
		String newWorldName = "waterwars_" + String.valueOf(id);

		List<String> worldNames = Storage.getArenaNames();

		if (worldNames.size() == 0) {
			return null;
		}

		Random rand = new Random();
		String randomWorld = worldNames.get(rand.nextInt(worldNames.size()));
		
		WorldCreator.name(randomWorld).createWorld();

		while (Bukkit.getWorld(randomWorld) == null) {
			worldNames.remove(randomWorld);
			if (worldNames.size() == 0) {
				return null;
			}
			randomWorld = worldNames.get(rand.nextInt(worldNames.size()));
			WorldCreator.name(randomWorld).createWorld();
		}
		Storage.setArenaNames(worldNames);

		copyWorld(Bukkit.getWorld(randomWorld), newWorldName);
		
		Bukkit.getServer().createWorld(new WorldCreator(newWorldName).environment(World.Environment.NORMAL));
		return Bukkit.getServer().getWorld(newWorldName);
	}

    public static void copyWorld(World originalWorld, String newWorldName) {
        File copiedFile = new File(Bukkit.getWorldContainer(), newWorldName);
        copyFileStructure(originalWorld.getWorldFolder(), copiedFile);
        new WorldCreator(newWorldName).createWorld();
    }
	

	private static void copyFileStructure(File source, File target) {
		try {
			ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
		    if (!ignore.contains(source.getName())) {
		        if (source.isDirectory()) {
		            if (!target.exists())
		                if (!target.mkdirs()) throw new IOException("Couldn't create world directory!");
		                String files[] = source.list();
		                for (String file : files) {
		                    File srcFile = new File(source, file);
		                    File destFile = new File(target, file);
		                    copyFileStructure(srcFile, destFile);
		                }
		        } else {
		            InputStream in = new FileInputStream(source);
		            OutputStream out = new FileOutputStream(target);
		            byte[] buffer = new byte[1024];
		            int length;
		            while ((length = in.read(buffer)) > 0)
		                out.write(buffer, 0, length);
		            in.close();
		            out.close();
		        }
	        } 
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	public static void unloadWorld(World world) {
		if (!world.equals(null)) {
			Bukkit.getServer().unloadWorld(world, false);
		}
	}

	public static boolean deleteWorld(World world) {

		File path = world.getWorldFolder();

		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	private static boolean deleteWorld(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	static int getNextEmptyId() {
		int i = 0;
		for (Arena arena : ArenaManager.arenas) {
			if ((int) arena.getId() == i) {
				i++;
				continue;
			} else {
				break;
			}
		}
		return i;
	}

	static List<Player> getPlayersFromQueue() {
		List<Player> players = new ArrayList<Player>();
		List<Player> queuedPlayers = ArenaManager.queuedPlayers;
		int limit = Storage.getMaxPlayers();
		int i = 0;

		for (Player p : queuedPlayers) {
			if (i >= limit)
				break;
			players.add(p);
			i++;
		}
		return players;
	}
	
	public static void sendActionbar(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

	public static void sendActionbar(List<Player> players, String message) {
		for(Player p : players) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
		}
	}
	
	public static void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		p.resetTitle();
		p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	public static void sendTitle(List<Player> players, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		for(Player p : players) {
			p.resetTitle();
			p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		}
	}

	public static PrivateGame getPrivateGameWith(String joinKey) {
		for (PrivateGame pg : ArenaManager.privateGames) {
			if(pg.getJoinKey().equals(joinKey)) {
				return pg;
			}
		}
		return new PrivateGame();
	}

	public static PrivateGame getPrivateGameWith(Player p) {
		for (PrivateGame pg : ArenaManager.privateGames) {
			if(pg.getPlayers().contains(p)) {
				return pg;
			}
		}
		return new PrivateGame();
	}

	public static boolean isPGOwner(Player p) {
		for (PrivateGame pg : ArenaManager.privateGames) {
			if(pg.getOwner() == p) {
				return true;
			}
		}
		return false;
	}

	public static void removePlayerFromPrivateGame(Player p) {
		PrivateGame pg = getPrivateGameWith(p);
		pg.removePlayer(p);
		ArenaManager.updatePrivateGame(pg);
	}

	public static String getJoinKey() {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 5;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    
	    if(getPrivateGameWith(generatedString).isValid()) return getJoinKey();
	    
		return generatedString;
	}
}
