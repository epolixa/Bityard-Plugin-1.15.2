package com.epolixa.bityard;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ElytraListener implements Listener
{
    private final Bityard bityard;

    public ElytraListener(Bityard bityard)
    {
        this.bityard = bityard;
    }

    // Plays a sound when openning wings
    @EventHandler
    public void onStartGlide(EntityToggleGlideEvent event)
    {
        try
        {
            if (event.getEntityType() == EntityType.PLAYER)
            {
                Player player = (Player)event.getEntity();
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.4f, 2f);
            }
        }
        catch (Exception e)
        {
            bityard.log(e.toString());
        }
    }

    // Stop gliding and play a sound when sneaking while gliding
    @EventHandler
    public void onSneakWhileGliding(PlayerToggleSneakEvent event)
    {
        try
        {
            Player player = event.getPlayer();
            if (player.isGliding())
            {
                player.setGliding(false);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.4f, 1.5f);
            }
        }
        catch (Exception e)
        {
            bityard.log(e.toString());
        }
    }
}
