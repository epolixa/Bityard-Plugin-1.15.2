package com.epolixa.bityard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener
{
    private final Bityard bityard;

    public ServerListPingListener(Bityard bityard)
    {
        this.bityard = bityard;
    }

    @EventHandler
    public void ServerListPing(ServerListPingEvent event)
    {
        try {
            event.setMotd(bityard.getMOTD());
        } catch (Exception e) {
            bityard.log("Caught error: " + e);
        }
    }
}
