package com.scarabcoder.tag.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.scarabcoder.tag.Main;
import com.scarabcoder.tag.enums.GameMode;
import com.scarabcoder.tag.enums.GameStatus;
import com.scarabcoder.tag.players.PlayerData;

public class Game {
	
	private List<String> players = new ArrayList<String>();
	
	private HashMap<Location, Boolean> spawns = new HashMap<Location, Boolean>();
	
	private HashMap<String, GameMode> gamemodes = new HashMap<String, GameMode>();
	
	private HashMap<String, PlayerData> playerData = new HashMap<String, PlayerData>();
	
	private HashMap<String, String> tagged = new HashMap<String, String>();
	
	private HashMap<String, ArmorStand> tagThing = new HashMap<String, ArmorStand>();
	
	private int max;
	
	@SuppressWarnings("unused")
	private int time = 0;
	
	@SuppressWarnings("unused")
	private int endTime;
	
	private int startTime;
	
	private String id;
	
	private int counter;
	
	private GameStatus status;
	
	private int startPlayers;
	
	public void addSpawn(int x, int y, int z){
		this.spawns.put(new Location(Bukkit.getWorld(id), x, y, z), false);
	}
	
	public void tag(Player tagged, Player tagger){
		this.tagged.put(tagged.getUniqueId().toString(), tagger.getUniqueId().toString());
		Location loc = tagged.getLocation();
		loc.setY(loc.getY() - 0.2);
		ArmorStand stand = (ArmorStand) tagged.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		stand.setGravity(false);
		stand.setVisible(false);
		stand.setCollidable(false);
		stand.setInvulnerable(true);
		stand.setMarker(true);
		stand.setPassenger(tagged);
		this.tagThing.put(tagged.getUniqueId().toString(), stand);
		for(String str : this.tagged.keySet()){
			if(this.tagged.get(str).equals(tagged.getUniqueId().toString())){
				this.untag(Bukkit.getPlayer(UUID.fromString(str)));
			}
		}
	}
	
	public void playerDismountEvent(Player p, Entity vehicle){
		if(this.isTagged(p)){
			vehicle.setPassenger(p);
		}
	}
	
	public boolean isTagged(Player p){
		return this.tagged.containsKey(p.getUniqueId().toString());
	}
	
	public Player getTagger(Player p){
		return Bukkit.getPlayer(UUID.fromString(this.tagged.get(p.getUniqueId().toString())));
	}
	
	public void untag(Player p){
		this.tagged.remove(p.getUniqueId().toString());
		p.getVehicle().remove();
		this.tagThing.remove(p.getUniqueId().toString());
	}
	
	public void playerDamageByPlayerEvent(Player attacker, Player attacked){
		if(this.getGameStatus().equals(GameStatus.INGAME)){
			if(!this.isTagged(attacked)){
				if(attacked.getHealth() - 10 <= 0){
					attacked.setHealth(20);
					this.tag(attacked, attacker);
					attacker.sendMessage(ChatColor.GOLD + "You tagged " + attacked.getName() + "!");
					attacker.setHealth(20.0);
					attacked.sendMessage(ChatColor.GOLD + "You were tagged by " + attacker.getName() + "!");
					this.sendMessage(attacker.getName() + " tagged " + attacked.getName() + "!");
					for(String str : this.tagged.keySet()){
						if(this.tagged.get(str).equals(attacked.getUniqueId().toString())){
							this.untag(Bukkit.getPlayer(UUID.fromString(str)));
							this.sendMessage(Bukkit.getPlayer(UUID.fromString(str)).getName() + " is now up!");
						}
					}
					if(this.isTagged(attacker)){
						this.untag(attacker);
					}
				}else{
					if(this.isTagged(attacker)){
						attacked.damage(5.0);
						
					}else{
						attacked.damage(10.0);
					}
				}
			}
		}
	}
	
	public void doSecond(){
		if(this.getGameStatus().equals(GameStatus.WAITING)){
				if(this.counter > this.startTime){
					if(this.getPlayersUUIDs().size() >= this.startPlayers){
						this.sendMessage(ChatColor.GREEN + "Game starting in " + ChatColor.BOLD + this.startTime + ChatColor.RESET.toString() + ChatColor.GREEN + " seconds!");
						this.counter = this.startTime;
					}
				}if(this.counter < this.startTime + 1){
					
					this.counter = this.counter - 1;
					if(this.counter != 0){
						if(this.counter % 10 == 0){
							this.sendMessage(ChatColor.GREEN + "Game starts in " + ChatColor.BOLD + counter + ChatColor.RESET.toString() + ChatColor.GREEN + " seconds!");
							for(Player p : this.getPlayers()){
								p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
								
							}
						}else if(this.counter == 1){
							for(Player p : this.getPlayers()){
								p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
							}
							this.sendMessage(ChatColor.GREEN + "Game starts in " + ChatColor.BOLD + "1" + ChatColor.RESET.toString() + ChatColor.GREEN + " second!");
						}else if(this.counter < 6){
							this.sendMessage(ChatColor.GREEN + "Game starts in " + ChatColor.BOLD + counter + ChatColor.RESET.toString() + ChatColor.GREEN + " seconds!");
							for(Player p : this.getPlayers()){
								p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
								
							}
						}
					}else{
						if(this.getPlayersUUIDs().size() >= this.startPlayers){
							this.setGameStatus(GameStatus.INGAME);
							for(Player p : this.getPlayers()){
								p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
								
							}
							this.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Game started!");
						}else{
							this.sendMessage(ChatColor.RED + "Cancelling countdown, not enough players!");
							this.counter = this.startTime + 1;
						}
					}
				}
			}else{
				if(this.players.size() < 2){
					this.sendMessage("Not enough players, ending game.");
					if(this.players.size() == 1){
						this.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + this.getPlayers().get(0).getName() + ChatColor.RESET + ChatColor.GREEN.toString() + " wins the game by default!");
						Bukkit.broadcastMessage(ChatColor.RESET + "[" + ChatColor.YELLOW + "Banana Tag" + ChatColor.RESET + "] " + ChatColor.GREEN.toString() + ChatColor.BOLD + this.getPlayers().get(0).getName() + ChatColor.RESET + ChatColor.GREEN.toString() + " won Banana Tag on \"" + this.id + "\"!");
					}
					this.endGame();
				}
				if(this.tagged.size() + 1 == this.getPlayersUUIDs().size()){
					List<String> tagged = new ArrayList<>(this.tagged.keySet());
					List<String> players = new ArrayList<>(this.players);
					
					players.removeAll(tagged);
					
					Player p = Bukkit.getPlayer(UUID.fromString(players.get(0)));
					
					this.sendMessage(ChatColor.BOLD + ChatColor.GREEN.toString() + p.getName() + " wins the game with " + (this.players.size() - 1) + " points!");
					Bukkit.broadcastMessage(ChatColor.RESET + "[" + ChatColor.YELLOW + "Banana Tag" + ChatColor.RESET + "] " + ChatColor.GREEN.toString() + ChatColor.BOLD + p.getName() + ChatColor.RESET + ChatColor.GREEN.toString() + " won Banana Tag on \"" + this.id + "\"!");
					this.endGame();
				}
			}
	}
	
	
	
	public Game(String id, int maxPlayers, int endTime, int startPlayers){
		this.max = maxPlayers;
		this.endTime = endTime;
		this.id = id;
		
		this.startTime = 60;
		this.counter = this.startTime + 1;
		this.startPlayers = startPlayers;
		this.status = GameStatus.WAITING;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable(){

			@Override
			public void run() {
				for(String str : tagged.keySet()){
					Player p = Bukkit.getPlayer(UUID.fromString(str));
					if(isTagged(p)){
						if(p.getVehicle() == null){
							tagThing.get(p.getUniqueId().toString()).setPassenger(p);
						}
					}
				}
			}
			
		}, 0L, 1L);
		GameManager.registerGame(this);
	}
	
	public String getID(){
		return this.id;
	}
	
	public void setGameStatus(GameStatus status){
		this.status = status;
	}
	
	public void sendMessage(String str){
		for(Player p : this.getPlayers()){
			p.sendMessage(ChatColor.RESET + "[" + ChatColor.YELLOW + "Banana Tag" + ChatColor.RESET + "] " + str);
		}
	}
	
	public GameStatus getGameStatus(){
		return this.status;
	}
	
	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<Player>();
		for(String str : this.players){
			
			players.add(Bukkit.getPlayer(UUID.fromString(str)));
		}
		return players;
	}
	
	public boolean isPlaying(Player p){
		return players.contains(p.getUniqueId().toString());
	}
	
	public int getMaxPlayers(){
		return this.max;
	}
	
	public List<String> getPlayersUUIDs(){
		return this.players;
	}
	
	public void setGameMode(Player p, GameMode mode){
		gamemodes.put(p.getUniqueId().toString(), mode);
	}
	
	public void quitGame(Player p){
		if(this.isTagged(p)){
			this.tagged.remove(p.getUniqueId().toString());
			this.tagThing.remove(p.getUniqueId().toString());
			p.getVehicle().remove();
		}
		
		p.removePotionEffect(PotionEffectType.GLOWING);
		p.removePotionEffect(PotionEffectType.NIGHT_VISION);
		
		this.players.remove(p.getUniqueId().toString());
		
		this.playerData.get(p.getUniqueId().toString()).applyToPlayer(p);
		
		this.playerData.remove(p.getUniqueId().toString());
		
	}
	
	public void endGame(){
		this.counter = this.startTime;
		this.setGameStatus(GameStatus.WAITING);
		this.sendMessage("Game ending!");
		System.out.println(this.getPlayers().size());
		System.out.println(this.players.size());
		for(Player p : this.getPlayers()){
			this.quitGame(p);
			System.out.println(p.getName());
		}
		
	}
	
	public void addPlayer(Player p){
		if(this.getGameStatus().equals(GameStatus.WAITING)){
			if(this.players.size() + 1 <= this.max){
				if(this.counter < this.startTime){
					p.sendMessage(ChatColor.RESET + "[" + ChatColor.YELLOW + "Banana Tag" + ChatColor.RESET + "] " + ChatColor.GREEN + "Game starts in " + ChatColor.BOLD + counter + ChatColor.RESET + ChatColor.GREEN.toString() + " seconds!");
				}
				this.playerData.put(p.getUniqueId().toString(), new PlayerData(p.getLocation(), p.getTotalExperience(), p.getHealth(), p.getFoodLevel(), p.getInventory(), p.getGameMode()));
				
				if(this.status.equals(GameStatus.WAITING)){
					p.teleport(this.getClaimSpawn());
					gamemodes.put(p.getUniqueId().toString(), GameMode.PLAYER);
					p.setGameMode(org.bukkit.GameMode.ADVENTURE);
					p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 2));
					p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
				}else{
					p.teleport(this.getClaimSpawn());
					p.setGameMode(org.bukkit.GameMode.SPECTATOR);
					gamemodes.put(p.getUniqueId().toString(), GameMode.SPECTATOR);
				}
				this.players.add(p.getUniqueId().toString());
				this.sendMessage(ChatColor.GREEN + p.getName() + " joined Banana Tag.");
				p.getInventory().clear();
				p.updateInventory();
				p.setTotalExperience(0);
				p.setHealth(20);
				p.setFoodLevel(20);
			}else{
				p.sendMessage(ChatColor.RED + "Game full!");
			}
		}else{
			p.sendMessage(ChatColor.RED + "Game in progress!");
		}
	}
	
	private Location getClaimSpawn() {
		for(Location loc : this.spawns.keySet()){
			if(this.spawns.get(loc)){
				this.spawns.put(loc, false);
				return loc;
			}
		}
		Random randomizer = new Random();
		List<Location> spawnlocs = new ArrayList<Location>(this.spawns.keySet());
		return spawnlocs.get(randomizer.nextInt(spawnlocs.size()));
		
	}

	public GameMode getPlayerMode(Player p){
		return this.gamemodes.get(p.getUniqueId().toString());
	}
	
	
	
}
