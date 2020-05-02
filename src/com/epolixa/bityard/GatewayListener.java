package com.epolixa.bityard;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class GatewayListener implements Listener
{
    private final Bityard bityard;

    public GatewayListener(Bityard bityard)
    {
        this.bityard = bityard;
    }


    @EventHandler
    public void onEnderPearlHitEgg(ProjectileHitEvent ev)
    {
        try
        {
            // if ender pearl hits dragon egg in the overworld
            if (ev.getEntity() instanceof EnderPearl && ev.getHitBlock().getType() == Material.DRAGON_EGG && ev.getHitBlock().getWorld().getEnvironment() == World.Environment.NORMAL)
            {
                EnderPearl ep = (EnderPearl) ev.getEntity();
                Player p = (Player) ep.getShooter();
                Block de = ev.getHitBlock();
                Location deLoc = de.getLocation();
                World w = deLoc.getWorld();
                bityard.log(ep.getName() + " by " + p.getName() + " hit " + de.getType().name() + " at " + bityard.locationXYZ(deLoc));

                FileConfiguration c = bityard.getConfig();

                bityard.log("creating End Gateway at dragon egg location");
                deLoc.getBlock().setType(Material.END_GATEWAY);
                EndGateway deeg = (EndGateway) deLoc.getBlock().getState();
                Location sege = new Location(w, c.getInt("SPAWN_GATEWAY_EXIT_X"), c.getInt("SPAWN_GATEWAY_EXIT_Y"), c.getInt("SPAWN_GATEWAY_EXIT_Z"));
                deeg.setExitLocation(sege);
                deeg.setExactTeleport(true);
                deeg.update();

                w.playEffect(deLoc, Effect.END_GATEWAY_SPAWN, 0);

                bityard.log("attempting to update spawn Gateway destination coords to location of player...");
                Location segLoc = new Location(w, c.getInt("SPAWN_GATEWAY_X"), c.getInt("SPAWN_GATEWAY_Y"), c.getInt("SPAWN_GATEWAY_Z"));
                if (segLoc.getBlock().getState() instanceof EndGateway)
                {
                    EndGateway seg = (EndGateway) segLoc.getBlock().getState();
                    seg.setExitLocation(p.getLocation());
                    seg.setExactTeleport(true);
                    seg.update();
                    w.playEffect(segLoc, Effect.END_GATEWAY_SPAWN, 0);
                    bityard.log("found and updated spawn Gateway");
                }
                else
                {
                    bityard.log("cannot find End Gateway at SPAWN_GATEWAY coords");
                }

                bityard.log("attempting to remove old Gateway...");
                Location oegLoc = new Location(w, c.getInt("RETURN_GATEWAY_X"), c.getInt("RETURN_GATEWAY_Y"), c.getInt("RETURN_GATEWAY_Z"));
                if (oegLoc.getBlock().getState() instanceof EndGateway)
                {
                    EndGateway oeg = (EndGateway) oegLoc.getBlock().getState();
                    oegLoc.getBlock().setType(Material.AIR);
                    w.playEffect(oegLoc, Effect.END_GATEWAY_SPAWN, 0);
                    bityard.log("found and removed old Gateway");
                }
                else
                {
                    bityard.log("cannot find old End Gateway at RETURN_GATEWAY coords");
                }

                bityard.log("updating RETURN_GATEWAY coords with new End Gateway location");
                c.set("RETURN_GATEWAY_X", deLoc.getBlockX());
                c.set("RETURN_GATEWAY_Y", deLoc.getBlockY());
                c.set("RETURN_GATEWAY_Z", deLoc.getBlockZ());
                bityard.saveConfig();
            }
        }
        catch (Exception ex)
        {
            bityard.log(ex.toString());
        }
    }


    @EventHandler
    public void onEnderPearlTeleport(PlayerTeleportEvent ev)
    {
        try
        {
            // if ender pearl teleport and location is dragon egg
            if (ev.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                int r = 2;
                Location to = ev.getTo();
                boolean nearDragonEgg = false;
                for (int x = to.getBlockX() - r; x < to.getBlockX() + r; x++) {
                    for (int y = to.getBlockY() - r; y < to.getBlockY() + r; y++) {
                        for (int z = to.getBlockZ() - r; z < to.getBlockZ() + r; z++) {
                            if (to.getWorld().getBlockAt(x,y,z).getType() == Material.DRAGON_EGG) {
                                nearDragonEgg = true;
                                break;
                            }
                        }
                    }
                }
                if (nearDragonEgg) {
                    bityard.log("detected ender pearl teleport onto dragon egg, cancel teleport");
                    ev.setCancelled(true);
                }
            }
        }
        catch (Exception ex)
        {
            bityard.log(ex.toString());
        }
    }
}
