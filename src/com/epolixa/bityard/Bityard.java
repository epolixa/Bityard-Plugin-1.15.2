package com.epolixa.bityard;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bityard extends JavaPlugin
{
    private static AFK afk;

    // Startup
    @Override
    public void onEnable()
    {
        log("Enabling...");

        // Register orphan event listeners
        PluginManager pluginManager = getServer().getPluginManager();
        //pluginManager.registerEvents(new BeaconListener(this), this); // beacon titles
        pluginManager.registerEvents(new MOTDListener(this), this); // change motd from town hall
        pluginManager.registerEvents(new ElytraListener(this), this); // elytra polish

        // Start child classes
        log("Starting child classes");
        afk = new AFK(this); afk.start(); // AFK

        log("Enabled");
    }

    // Shutdown
    @Override
    public void onDisable()
    {
        log("Disabling...");

        // Stop child classes
        log("Stopping child classes");
        afk.stop(); // AFK

        log("Disabled");
    }

    public void log(String msg) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        getLogger().info("[" + stackTraceElements[2].getClassName() + "] : " + msg);
    }

    public String locationXYZ(Location loc) {
        return loc.getBlockX() + "x, " + loc.getBlockY() + "y, " + loc.getBlockZ() + "z";
    }
}
