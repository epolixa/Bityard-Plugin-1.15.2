package com.epolixa.bityard;

import org.bukkit.entity.Player;

import java.util.UUID;

public class AFKTimer implements Runnable
{
    private final AFK afk;
    private final Bityard bityard;

    public AFKTimer(AFK afk)
    {
        this.afk = afk;
        this.bityard = afk.getPlugin();
    }

    public void run()
    {
        UUID uuid;

        for (Player p : bityard.getServer().getOnlinePlayers())
        {
            uuid = p.getUniqueId();
            if (!afk.getAFKstatus(uuid))
            {
                afk.setIdleTime(uuid, afk.getIdleTime(uuid) + 1);
                if (afk.getIdleTime(uuid) >= bityard.getConfig().getInt("IDLE_TIME"))
                {
                    afk.goAFK(p, "");
                }
            }
        }
    }
}
