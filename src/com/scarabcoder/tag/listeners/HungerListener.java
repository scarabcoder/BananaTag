package com.scarabcoder.tag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.scarabcoder.tag.game.GameManager;

public class HungerListener implements Listener {
	@EventHandler
	public void foodLevelChange(FoodLevelChangeEvent e){
		if(GameManager.isPlayerInGame((Player) e.getEntity())){
			e.setCancelled(true);
			
		}
	}
}
