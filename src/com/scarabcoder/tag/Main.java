package com.scarabcoder.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.scarabcoder.tag.commands.BTag;
import com.scarabcoder.tag.enums.GameStatus;
import com.scarabcoder.tag.game.Game;
import com.scarabcoder.tag.game.GameManager;
import com.scarabcoder.tag.listeners.CommandListener;
import com.scarabcoder.tag.listeners.DamageListener;
import com.scarabcoder.tag.listeners.EntityDismountListener;
import com.scarabcoder.tag.listeners.HungerListener;
import com.scarabcoder.tag.listeners.PlayerLeaveListener;
import com.scarabcoder.tag.listeners.RightClickSignListener;
import com.scarabcoder.tag.listeners.SignPlace;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	private static Plugin plugin;
	
    public void loadConfiguration(){
    	List<Location> locs = new ArrayList<Location>();
    	
    	this.getConfig().addDefault("signs", locs);
        //See "Creating you're defaults"
        plugin.getConfig().options().copyDefaults(true); // NOTE: You do not have to use "plugin." if the class extends the java plugin
        //Save the config whenever you manipulate it
        plugin.saveConfig();
    }
	
	@Override
	public void onEnable(){
		this.getLogger().log(Level.FINE, "Enabled Banana Tag by ScarabCoder.");
		Main.plugin = this;
		this.loadConfiguration();
		
		this.getCommand("btag").setExecutor(new BTag());
		this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
		this.getServer().getPluginManager().registerEvents(new DamageListener(), this);
		this.getServer().getPluginManager().registerEvents(new HungerListener(), this);
		//this.getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
		this.getServer().getPluginManager().registerEvents(new EntityDismountListener(), this);
		this.getServer().getPluginManager().registerEvents(new RightClickSignListener(), this);
		this.getServer().getPluginManager().registerEvents(new SignPlace(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
		Game game = new Game("tag", 6, 500,3);
		game.addSpawn(280, 4, 1073);
		game.addSpawn(295, 4, 1095);
		game.addSpawn(273, 5, 1107);
		game.addSpawn(252, 4, 1107);
		game.addSpawn(249, 5, 1097);
		Game arena = new Game("orc_arena", 6, 500, 3);
		arena.addSpawn(1007, 41, 61);
		arena.addSpawn(10015, 41, 52);
		arena.addSpawn(10015, 41, 71);
		arena.addSpawn(997, 41, 67);
		arena.addSpawn(999, 41, 52);
		Game house = new Game("house", 6, 500, 3);
		house.addSpawn(-473, 6, 645);
		house.addSpawn(-470, 12, 657);
		house.addSpawn(-484, 6, 653);
		house.addSpawn(-473, 6, 672);
		house.addSpawn(-472, 6, 660);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				for(Game game : GameManager.getGames()){
					game.doSecond();
				}
				@SuppressWarnings("unchecked")
				List<Location> locs = new CopyOnWriteArrayList<Location>((Collection<? extends Location>) Main.getPlugin().getConfig().getList("signs"));
				for(Location loc : locs){
					if(loc.getBlock().getType().equals(Material.WALL_SIGN)){
						Sign sign = (Sign) loc.getBlock().getState();
						if(GameManager.getGame(sign.getLine(1)) != null){
							Game game = GameManager.getGame(sign.getLine(1));
							String str;
							if(game.getGameStatus().equals(GameStatus.WAITING)){
								str = ChatColor.GREEN + "Open";
							}else{
								str = ChatColor.RED + "Ingame";
							}
							sign.setLine(2, ChatColor.AQUA + game.getGameStatus().toString());
							sign.setLine(3, game.getPlayersUUIDs().size() + "/" + game.getMaxPlayers());
							sign.update();
						}else{
							locs.remove(loc);
							loc.getBlock().breakNaturally();
						}
					}else{
						locs.remove(loc);
						loc.getBlock().breakNaturally();
					}
				}
				Main.getPlugin().getConfig().set("signs", locs);
				Main.getPlugin().saveConfig();
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
