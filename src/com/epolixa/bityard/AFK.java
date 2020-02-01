package com.epolixa.bityard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class AFK
{
    private final Bityard bityard;

    private static ScheduledExecutorService executor;
    private static HashMap<UUID, Boolean> afkStatusMap;
    private static HashMap<UUID, AtomicInteger> idleTimeMap;


    public AFK(Bityard bityard)
    {
        this.bityard = bityard;
    }


    public void start()
    {
		/* Register commands */
        bityard.getCommand("afk").setExecutor(new AFKCommandExecutor(this));

		/* Register event listeners */
        bityard.getServer().getPluginManager().registerEvents(new AFKListener(this), bityard);

		/* Initialize idleTimeCheck executor */
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new AFKTimer(this), 0, 1, TimeUnit.SECONDS);

		/* Initialize HashMaps */
        afkStatusMap = new HashMap<UUID, Boolean>();
        for (Player p : bityard.getServer().getOnlinePlayers())
            setAFKstatus(p.getUniqueId(), false);
        idleTimeMap = new HashMap<UUID, AtomicInteger>();
        for (Player p : bityard.getServer().getOnlinePlayers())
            setIdleTime(p.getUniqueId(), 0);

        log("Enabled");
    }


    public void stop()
    {
        executor.shutdown();

        for (Player p : bityard.getServer().getOnlinePlayers())
            p.setPlayerListName(p.getName());

        log("Disabled");
    }

    public void log(String msg) {bityard.log("[AFK] " + msg);}

    public Bityard getPlugin() {return bityard;}

    public boolean getAFKstatus(UUID uuid) {return afkStatusMap.get(uuid);}
    public void setAFKstatus(UUID uuid, boolean status) {afkStatusMap.put(uuid, status);}
    public void removeAFKstatus(UUID uuid) {afkStatusMap.remove(uuid);}

    public int getIdleTime(UUID uuid) {return idleTimeMap.get(uuid).get();}
    public void setIdleTime(UUID uuid, int seconds) {idleTimeMap.put(uuid, new AtomicInteger(seconds));}
    public void removeIdleTime(UUID uuid) {idleTimeMap.remove(uuid);}


    public void goAFK(Player player, String reason)
    {
        if (!afkStatusMap.get(player.getUniqueId()))
        {
            afkStatusMap.put(player.getUniqueId(), true);
            if (!reason.equals(""))
            {Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " is now AFK: \"" + reason + "\"");}
            else {Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " is now AFK");}
            refreshListName(player, reason);
        }
    }

    public void leaveAFK(Player player)
    {
        if (afkStatusMap.get(player.getUniqueId()))
        {
            afkStatusMap.put(player.getUniqueId(), false);
            Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " is no longer AFK");
            refreshListName(player, "");
        }
    }

    public void refreshListName(Player player, String reason)
    {
        if (afkStatusMap.get(player.getUniqueId()))
        {
            if (!reason.equals(""))
            {
                player.setPlayerListName(player.getScoreboard().getEntryTeam(player.getName()).getPrefix() + player.getName() + ChatColor.GRAY + " (AFK: " + reason + ")");
            } else
            {
                player.setPlayerListName(player.getScoreboard().getEntryTeam(player.getName()).getPrefix() + player.getName() + ChatColor.GRAY + " (AFK)");
            }
        } else
        {
            player.setPlayerListName(player.getName());
        }
    }

    public void refreshListNames()
    {
        for (Player p : bityard.getServer().getOnlinePlayers())
            refreshListName(p, "");
    }
}
