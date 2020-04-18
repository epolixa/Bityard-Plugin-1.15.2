package com.epolixa.bityard;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Bityard extends JavaPlugin
{
    private static AFK afk;
    private HashMap<DyeColor, ChatColor> colorMap;
    private String motd;

    // Startup
    @Override
    public void onEnable()
    {
        log("Enabling...");

        // Save config
        saveDefaultConfig();

        colorMap = buildColorMap();

        // Default motd
        motd = pickMOTD();

        // Register orphan event listeners
        PluginManager pluginManager = getServer().getPluginManager();
        //pluginManager.registerEvents(new BeaconListener(this), this); // beacon titles
        pluginManager.registerEvents(new ServerListPingListener(this), this); // change motd from town hall
        pluginManager.registerEvents(new ElytraListener(this), this); // elytra polish
        pluginManager.registerEvents(new WanderingTraderListener(this), this); // random wandering trader

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

    public String pickMOTD() {
        FileConfiguration config =  getConfig();

        try {
            World w = getServer().getWorld("world");
            Location cornerA = new Location(w, config.getInt("MOTD_X_MIN"), config.getInt("MOTD_Y_MIN"), config.getInt("MOTD_Z_MIN"));
            Location cornerB = new Location(w, config.getInt("MOTD_X_MAX"), config.getInt("MOTD_Y_MAX"), config.getInt("MOTD_Z_MAX"));

            ArrayList<Sign> signs = new ArrayList<Sign>();
            for (int x = cornerA.getBlockX(); x <= cornerB.getBlockX(); x++) {
                for (int y = cornerA.getBlockY(); y <= cornerB.getBlockY(); y++) {
                    for (int z = cornerA.getBlockZ(); z <= cornerB.getBlockZ(); z++) {
                        Block b = w.getBlockAt(new Location(w,x,y,z));
                        if (b.getBlockData() instanceof WallSign) {
                            signs.add((Sign) b.getState());
                        }
                    }
                }
            }

            if (!signs.isEmpty()) {
                Random random = new Random();
                Sign sign = signs.get(random.nextInt(signs.size()));

                StringBuilder sb = new StringBuilder();
                DyeColor color = sign.getColor();
                if (color != null) {
                    if (color.name().equals("BLACK")) {
                        sb.append(ChatColor.GRAY);
                    } else {
                        sb.append(colorMap.get(color));
                    }
                } else {
                    sb.append(ChatColor.GRAY);
                }

                StringBuilder lsb = new StringBuilder();
                for (int i = 0; i < sign.getLines().length; i++) {
                    String line = sign.getLine(i);
                    if (line.length() > 0) {
                        if (lsb.toString().length() > 0) {
                            lsb.append(" ");
                        }
                        lsb.append(line);
                    }
                }
                sb.append(lsb.toString());
                sb.append(ChatColor.RESET);

                String signText = sb.toString();

                log("[pickMOTD] Setting new MOTD to \"" + signText + "\"");
                return signText;
            }
        } catch (Exception e) {
            log("[pickMOTD] caught error: ");
            e.printStackTrace();
        }

        return ChatColor.GREEN + config.getString("MOTD_DEFAULT") + ChatColor.RESET;
    }

    private HashMap<DyeColor, ChatColor> buildColorMap() {
        HashMap<DyeColor, ChatColor> map = new HashMap<DyeColor, ChatColor>();
        map.put(DyeColor.BLACK, ChatColor.BLACK);
        map.put(DyeColor.BLUE, ChatColor.DARK_BLUE);
        map.put(DyeColor.BROWN, ChatColor.DARK_RED);
        map.put(DyeColor.CYAN, ChatColor.DARK_AQUA);
        map.put(DyeColor.GRAY, ChatColor.DARK_GRAY);
        map.put(DyeColor.GREEN, ChatColor.DARK_GREEN);
        map.put(DyeColor.LIGHT_BLUE, ChatColor.BLUE);
        map.put(DyeColor.LIGHT_GRAY, ChatColor.GRAY);
        map.put(DyeColor.LIME, ChatColor.GREEN);
        map.put(DyeColor.MAGENTA, ChatColor.AQUA);
        map.put(DyeColor.ORANGE, ChatColor.GOLD);
        map.put(DyeColor.PINK, ChatColor.LIGHT_PURPLE);
        map.put(DyeColor.PURPLE, ChatColor.DARK_PURPLE);
        map.put(DyeColor.RED, ChatColor.RED);
        map.put(DyeColor.WHITE, ChatColor.WHITE);
        map.put(DyeColor.YELLOW, ChatColor.YELLOW);
        return map;
    }

    public String getMOTD() {
        return motd;
    }
}
