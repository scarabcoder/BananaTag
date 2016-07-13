package com.scarabcoder.tag.players;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerData {
	
	private Location location;
	
	
	private int exp;
	
	private ItemStack[] inv;
	
	private double health;
	
	private GameMode gamemode;
	
	private int hunger;
	
	public PlayerData(Player p){
		this.location = p.getLocation();
		
		this.exp = p.getTotalExperience();
		
		this.inv = p.getInventory().getContents();
		
		this.health = p.getHealth();
		
		this.hunger = p.getFoodLevel();
	}
	
	public PlayerData(Location loc, int exp, double health, int hunger, Inventory inv, GameMode mode){
		this.location = loc;
		this.exp = exp;
		this.health = health;
		this.hunger = hunger;
		this.inv = inv.getContents();
		this.gamemode = mode;
	}

	public void applyToPlayer(Player p){
		p.teleport(location);
		p.getInventory().setContents(inv);
		p.updateInventory();
		p.setTotalExperience(exp);
		p.setHealth(health);
		p.setFoodLevel(hunger);
		p.setGameMode(gamemode);
	}
	
	
	
	public Location getLocation() {
		return location;
	}

	public ItemStack[] getInventory() {
		return inv;
	}

	public int getExp() {
		return exp;
	}

	public double getHealth() {
		return health;
	}

	public double getHunger() {
		return hunger;
	}
}
