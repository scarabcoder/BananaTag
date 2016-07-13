package com.scarabcoder.tag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.scarabcoder.tag.game.Game;
import com.scarabcoder.tag.game.GameManager;

import net.md_5.bungee.api.ChatColor;

public class SignPlace implements Listener{
	@EventHandler
	public void signPlace(SignChangeEvent e){
		if(e.getLine(0).equals("[btag]")){
			for(Game game : GameManager.getGames()){
				if(game.getID().equalsIgnoreCase(e.getLine(1))){
					e.setLine(0, ChatColor.RESET + "[" + ChatColor.YELLOW + "Banana Tag" + ChatColor.RESET + "]");
					
				}
			}
		}
	}
}
