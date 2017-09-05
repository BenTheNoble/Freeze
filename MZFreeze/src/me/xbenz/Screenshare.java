package me.xbenz;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Screenshare
  extends JavaPlugin
  implements Listener
{
  ArrayList<String> frozen = new ArrayList<String>();
  ChatColor red = ChatColor.RED;
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p.getName()))
    {
      e.setTo(e.getFrom());
      p.sendMessage(ChatColor.GRAY + "----------------------------------------------");
      p.sendMessage(ChatColor.RED + "You have been frozen!");
      p.sendMessage(ChatColor.RED + "Join our Discord for a Screenshare!");
      p.sendMessage(ChatColor.RED + "You have 10 minutes! Logout = Ban");
      p.sendMessage(ChatColor.RED + "Discord: https://discord.gg/NuEfq9W");
      p.sendMessage(ChatColor.GRAY + "----------------------------------------------");
    }
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p.getName()))
    {
      e.setCancelled(true);
      p.sendMessage(this.red + "You can not break blocks while frozen!");
    }
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p.getName()))
    {
      e.setCancelled(true);
      p.sendMessage(this.red + "You can not place blocks while frozen!");
    }
  }
  
  @EventHandler
  public void onLogout(PlayerQuitEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p.getName()))
    {
        for (Player po : Bukkit.getServer().getOnlinePlayers()) {
            if (po.hasPermission("MZCore.Staff")) {
                po.sendMessage(ChatColor.RED + p.getName() + " has logged out while frozen!");
              }
        }
    }
  }
  
  public void onEnable()
  {
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable() {}
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("ss"))
    {
      if (!sender.hasPermission("MZCore.Staff")) {
      	 sender.sendMessage(ChatColor.RED + "[Permissions]:" + ChatColor.GOLD + "No permission!");
         return true;
      }
      if (args.length == 0)
      {
        sender.sendMessage(ChatColor.RED + "Incorrect Arguments! /ss <user>");
        return true;
      }
      Player target = Bukkit.getServer().getPlayer(args[0]);
      if (target == null)
      {
        sender.sendMessage(ChatColor.RED + "Error: Player not found!");
        return true;
      }
      if (this.frozen.contains(target.getName()))
      {
        this.frozen.remove(target.getName());
        sender.sendMessage(ChatColor.GREEN + target.getName() + " has been unfrozen!");
        target.sendMessage(ChatColor.GREEN + "You have been unfrozen!");
        target.setAllowFlight(false);
        target.setFlying(false);
        return true;
      }
      this.frozen.add(target.getName());
      target.setAllowFlight(true);
      target.setFlying(true);
      sender.sendMessage(ChatColor.DARK_RED + target.getName() + " has been frozen!");
      target.sendMessage(ChatColor.DARK_RED + "You have been frozen!");
    }
    return true;
  }
}