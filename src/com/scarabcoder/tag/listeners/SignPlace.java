package com.scarabcoder.tag.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.scarabcoder.tag.Main;
import com.scarabcoder.tag.game.Game;
import com.scarabcoder.tag.game.GameManager;

import net.md_5.bungee.api.ChatColor;

public class SignPlace implements Listener{
	@SuppressWarnings("unchecked")
	@EventHandler
	public void signPlace(SignChangeEvent e){
		if(e.getLine(0).equals("[btag]")){
			for(Game game : GameManager.getGames()){
				if(game.getID().equalsIgnoreCase(e.getLine(1))){
					e.setLine(0, ChatColor.RESET + "[" + ChatColor.BLUE + "Banana Tag" + ChatColor.RESET + "]");
					List<Location> locs = (List<Location>) Main.getPlugin().getConfig().getList("signs");
					
					locs.add(e.getBlock().getLocation());
					Main.getPlugin().getConfig().set("signs", locs);
					Main.getPlugin().saveConfig();
					
				}
			}
		}
	}
}
