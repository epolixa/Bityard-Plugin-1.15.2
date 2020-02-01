package com.epolixa.bityard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;


public class AFKListener implements Listener
{
    private final AFK afk;
    private final Bityard bityard;

    public AFKListener(AFK afk)
    {
        this.afk = afk;
        this.bityard = afk.getPlugin();
    }

	/*@EventHandler
	public void onPlayerDamaged(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Player player = event.getEntity();
			if (bityard.getAFKstatus(player.getName()))
			{
		  	Bukkit.broadcastMessage(player.getName() + " was hurt while AFK at " + player.getLocation().toString());
			}
		}
	}*/

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        afk.setIdleTime(event.getPlayer().getUniqueId(), 0);
        afk.leaveAFK(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        afk.setIdleTime(event.getPlayer().getUniqueId(), 0);
        afk.leaveAFK(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event)
    {
        afk.setIdleTime(event.getPlayer().getUniqueId(), 0);
        afk.leaveAFK(event.getPlayer());

        String msg = event.getMessage();
        if (msg.length() >= 6)
        {
            if (msg.substring(0, 6).equalsIgnoreCase("/tell "))
            {
                int i = 6;
                for (; i < msg.length() && msg.charAt(i) != ' '; i++); String recipientName = msg.substring(6, i);
                for (Player p : bityard.getServer().getOnlinePlayers())
                    if (p.getName().equalsIgnoreCase(recipientName))
                    {
                        if (i < msg.length() && afk.getAFKstatus(p.getUniqueId()))
                            event.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + recipientName + " is AFK and may not see your message");
                        break;
                    }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        afk.setIdleTime(event.getPlayer().getUniqueId(), 0);
        afk.setAFKstatus(event.getPlayer().getUniqueId(), false);
        afk.refreshListNames();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        afk.removeIdleTime(event.getPlayer().getUniqueId());
        afk.removeAFKstatus(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event)
    {
        afk.removeIdleTime(event.getPlayer().getUniqueId());
        afk.removeAFKstatus(event.getPlayer().getUniqueId());
    }

}
