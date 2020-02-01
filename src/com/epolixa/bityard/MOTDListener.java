package com.epolixa.bityard;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Random;

public class MOTDListener implements Listener
{
    private final Bityard bityard;
    private Location cornerA;
    private Location cornerB;

    public MOTDListener(Bityard bityard)
    {
        this.bityard = bityard;
        FileConfiguration config =  bityard.getConfig();
        this.cornerA = new Location(bityard.getServer().getWorld("world"), config.getInt("MOTD_X_MIN"), config.getInt("MOTD_Y_MIN"), config.getInt("MOTD_Z_MIN"));
        this.cornerB = new Location(bityard.getServer().getWorld("world"), config.getInt("MOTD_X_MAX"), config.getInt("MOTD_Y_MAX"), config.getInt("MOTD_Z_MAX"));
    }


    @EventHandler
    public void onMOTDUpdate(ServerListPingEvent event)
    {
        Sign sign = null;
        Random random = new Random();

        // pick random sign location between corners
        Location signLoc = new Location(
            bityard.getServer().getWorld("world"),
            cornerA.getBlockX() == cornerB.getBlockX() ? cornerA.getBlockX() : cornerA.getBlockX() + random.nextInt(cornerB.getBlockX() - cornerA.getBlockX() + 1),
            cornerA.getBlockY() == cornerB.getBlockY() ? cornerA.getBlockY() : cornerA.getBlockY() + random.nextInt(cornerB.getBlockY() - cornerA.getBlockY() + 1),
            cornerA.getBlockZ() == cornerB.getBlockZ() ? cornerA.getBlockZ() : cornerA.getBlockZ() + random.nextInt(cornerB.getBlockZ() - cornerA.getBlockZ() + 1)
        );

        if (bityard.getServer().getWorld("world").getBlockAt(signLoc).getType() == Material.WALL_SIGN)
        {
            sign = (Sign)bityard.getServer().getWorld("world").getBlockAt(signLoc).getState();
        }
        if (sign != null)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sign.getLine(0));
            for (int i = 1; i < sign.getLines().length; i++)
                stringBuilder.append(" " + sign.getLine(i));
            String signText = stringBuilder.toString();
            event.setMotd(signText);
        }
    }
}
