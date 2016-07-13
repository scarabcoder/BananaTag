package com.scarabcoder.tag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.scarabcoder.tag.game.GameManager;

public class DamageListener implements Listener {
	@EventHandler
	public void playerDamagePlayer(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof Player){
				Player p = (Player) e.getEntity();
				Player p1 = (Player) e.getDamager();
				if(GameManager.isPlayerInGame(p)){
					GameManager.getPlayerGame(p).playerDamageByPlayerEvent(p1, p);
					e.setCancelled(true);
					
				}
			}
		}
	}
	
	@EventHandler
	public void playerDamageEvent(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(GameManager.isPlayerInGame(p)){
				if(e.getCause().equals(DamageCause.FALL) || (e.getCause().equals(DamageCause.SUFFOCATION))){
					e.setCancelled(true);
				}
			}
		}
	}
}
