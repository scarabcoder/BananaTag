package com.scarabcoder.tag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.scarabcoder.tag.game.GameManager;

import net.md_5.bungee.api.ChatColor;

public class PlayerTeleportListener implements Listener{
	@EventHandler
	public void playerTeleport(PlayerTeleportEvent e){
		if(GameManager.isPlayerInGame(e.getPlayer())){
			e.getPlayer().sendMessage(ChatColor.RED + "Cannot teleport while ingame!");
			e.setCancelled(true);
		}
	}
}
