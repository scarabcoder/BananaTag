package com.scarabcoder.tag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.scarabcoder.tag.game.GameManager;

import net.md_5.bungee.api.ChatColor;

public class CommandListener implements Listener{
	@EventHandler
	public void commandEvent(PlayerCommandPreprocessEvent e){
		if(GameManager.isPlayerInGame(e.getPlayer())){
			if(!e.getMessage().startsWith("/btag")){
				e.getPlayer().sendMessage(ChatColor.RED + "That command is disabled while ingame!");
				e.setCancelled(true);
				
			}
		}
	}
}
	