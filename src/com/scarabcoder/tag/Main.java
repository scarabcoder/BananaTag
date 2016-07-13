package com.scarabcoder.tag;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.scarabcoder.tag.commands.BTag;
import com.scarabcoder.tag.game.Game;
import com.scarabcoder.tag.game.GameManager;
import com.scarabcoder.tag.listeners.CommandListener;
import com.scarabcoder.tag.listeners.DamageListener;
import com.scarabcoder.tag.listeners.EntityDismountListener;
import com.scarabcoder.tag.listeners.HungerListener;

public class Main extends JavaPlugin {
	
	private static Plugin plugin;
	
	@Override
	public void onEnable(){
		this.getLogger().log(Level.FINE, "Enabled Banana Tag by ScarabCoder.");
		this.plugin = this;
		this.getCommand("btag").setExecutor(new BTag());
		this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
		this.getServer().getPluginManager().registerEvents(new DamageListener(), this);
		this.getServer().getPluginManager().registerEvents(new HungerListener(), this);
		//this.getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
		this.getServer().getPluginManager().registerEvents(new EntityDismountListener(), this);
		Game game = new Game("tag", 3, 500,3);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				for(Game game : GameManager.getGames()){
					game.doSecond();
				}
			}
			
		}, 0L, 20L);
	}
	@Override
	public void onDisable(){
		for(Game game : GameManager.getGames()){
			for(Player p : game.getPlayers()){
				game.sendMessage("Server reloading!");
				game.quitGame(p);
			}
		}
	}
	public static Plugin getPlugin(){
		return plugin;
	}
}
