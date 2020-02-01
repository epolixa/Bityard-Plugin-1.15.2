package com.epolixa.bityard;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import com.sun.prism.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BeaconListener implements Listener
{

    private final Bityard bityard;

    public BeaconListener(Bityard bityard)
    {
        this.bityard = bityard;
    }

    @EventHandler
    public void onBeaconApplyEffect(BeaconEffectEvent event)
    {
        try
        {
            if (event.getBlock().getType() == Material.BEACON)
            {
                Player player = event.getPlayer();
                Beacon beacon = (Beacon)event.getBlock().getState();

                // test for signs around beacon
                if (!player.hasPotionEffect(beacon.getPrimaryEffect().getType()))
                {
                    Location loc = beacon.getLocation();
                    World world = beacon.getWorld();
                    Sign sign = null;
                    if      (world.getBlockAt(loc.getBlockX() + 1, loc.getBlockY(), loc.getBlockZ()).getType() == Material.WALL_SIGN)
                        sign = (Sign)world.getBlockAt(loc.getBlockX() + 1, loc.getBlockY(), loc.getBlockZ()).getState();
                    else if (world.getBlockAt(loc.getBlockX() - 1, loc.getBlockY(), loc.getBlockZ()).getType() == Material.WALL_SIGN)
                        sign = (Sign)world.getBlockAt(loc.getBlockX() - 1, loc.getBlockY(), loc.getBlockZ()).getState();
                    else if (world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 1).getType() == Material.WALL_SIGN)
                        sign = (Sign)world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 1).getState();
                    else if (world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 1).getType() == Material.WALL_SIGN)
                        sign = (Sign)world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 1).getState();

                    if (sign != null)
                    {
                        ChatColor[] colorPrefixes = {ChatColor.WHITE, ChatColor.WHITE, ChatColor.WHITE, ChatColor.WHITE};
                        Location l = loc.add(0,1,0);
                        for (int i = 0; i < 4; i++)
                        {
                            for (; l.getBlockY() <= 255; l = l.add(0,1,0))
                            {
                                if (l.getBlock().getType() == Material.STAINED_GLASS || l.getBlock().getType() == Material.STAINED_GLASS_PANE)
                                {
                                    switch (l.getBlock().getData())
                                    {
                                        case 0: colorPrefixes[i] = ChatColor.WHITE; break;
                                        case 1: colorPrefixes[i] = ChatColor.GOLD; break;
                                        case 2: colorPrefixes[i] = ChatColor.LIGHT_PURPLE; break;
                                        case 3: colorPrefixes[i] = ChatColor.AQUA; break;
                                        case 4: colorPrefixes[i] = ChatColor.YELLOW; break;
                                        case 5: colorPrefixes[i] = ChatColor.GREEN; break;
                                        case 6: colorPrefixes[i] = ChatColor.LIGHT_PURPLE; break;
                                        case 7: colorPrefixes[i] = ChatColor.DARK_GRAY; break;
                                        case 8: colorPrefixes[i] = ChatColor.GRAY; break;
                                        case 9: colorPrefixes[i] = ChatColor.DARK_AQUA; break;
                                        case 10: colorPrefixes[i] = ChatColor.DARK_PURPLE; break;
                                        case 11: colorPrefixes[i] = ChatColor.BLUE; break;
                                        case 12: colorPrefixes[i] = ChatColor.DARK_RED; break;
                                        case 13: colorPrefixes[i] = ChatColor.DARK_GREEN; break;
                                        case 14: colorPrefixes[i] = ChatColor.RED; break;
                                        case 15: colorPrefixes[i] = ChatColor.BLACK; break;
                                        default: colorPrefixes[i] = ChatColor.WHITE;
                                    }

                                    l = l.add(0,1,0);
                                    break;
                                }
                            }
                        }

                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < sign.getLines().length; i++)
                            stringBuilder.append(colorPrefixes[i] + sign.getLine(i));
                        String signText = stringBuilder.toString();
                        player.sendTitle(" ", signText, 10, 70, 20);
                    }
                }
            }
        }
        catch (Exception e) {bityard.log(e.toString());}
    }



}
