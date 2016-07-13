package com.scarabcoder.tag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.scarabcoder.tag.game.GameManager;

public class EntityDismountListener implements Listener{
	@EventHandler
	public void entityDismountEvent(EntityDismountEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(GameManager.isPlayerInGame(p)){
				e.getEntity().setPassenger(p);
			}
		}
	}
}
