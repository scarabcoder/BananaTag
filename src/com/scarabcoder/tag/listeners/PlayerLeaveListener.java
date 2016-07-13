package com.scarabcoder.tag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.scarabcoder.tag.game.GameManager;

public class PlayerLeaveListener implements Listener{
	@EventHandler
	public void playerQuit(PlayerQuitEvent e){
		if(GameManager.isPlayerInGame(e.getPlayer())){
			GameManager.getPlayerGame(e.getPlayer()).quitGame(e.getPlayer());
		}
	}
}
