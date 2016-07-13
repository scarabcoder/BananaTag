package com.scarabcoder.tag.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class GameManager {
	
	private static HashMap<String, Game> games = new HashMap<String, Game>();
	
	public static void registerGame(Game game){
		games.put(game.getID(), game);
		System.out.println(game.getID());
	}
	
	public static Game getGame(String id){
		return games.get(id);
	}
	
	public static List<Game> getGames(){
		List<Game> game = new ArrayList<Game>();
		for(String str : games.keySet()){
			game.add(games.get(str));
		}
		return game;
	}
	
	public static boolean isPlayerInGame(Player p){
		for(Game game : getGames()){
			if(game.isPlaying(p)){
				return true;
			}
		}
		return false;
	}
	
	public static Game getPlayerGame(Player p){
		for(Game game : getGames()){
			if(game.isPlaying(p)){
				return game;
			}
		}
		return null;
	}
}
