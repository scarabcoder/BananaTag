package com.scarabcoder.tag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scarabcoder.tag.game.Game;
import com.scarabcoder.tag.game.GameManager;

import net.md_5.bungee.api.ChatColor;

public class BTag implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("join")){
				if(args.length > 1){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(!GameManager.isPlayerInGame(p)){
						if(GameManager.getGame(args[1]) != null){
							GameManager.getGame(args[1]).addPlayer(p);
							System.out.println("Done");
							return true;
						}else{
							p.sendMessage(ChatColor.RED + "Game doesn't exist!");
						}
					}else{
						p.sendMessage(ChatColor.RED + "Already ingame!");
					}
				}else{
					sender.sendMessage(ChatColor.RED + "You must be player to use this command!");
				}
				}else{
					sender.sendMessage(ChatColor.RED + "Please specify a game to join!");
				}
			}else if(args[0].equalsIgnoreCase("leave")){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(GameManager.isPlayerInGame(p)){
						for(Game game : GameManager.getGames()){
							if(game.isPlaying(p)){
								game.quitGame(p);
								p.sendMessage(ChatColor.GREEN + "Left game \"" + game.getID() + "\".");
								return true;
							}
						}
					}else{
						sender.sendMessage(ChatColor.RED + "Not in a game!");
					}
				}else{
					sender.sendMessage(ChatColor.RED + "Player-only command!");
				}
			}
		}else{
			sender.sendMessage(ChatColor.RED + "Please specify an argument!!");
		}
		return true;
	}

}
