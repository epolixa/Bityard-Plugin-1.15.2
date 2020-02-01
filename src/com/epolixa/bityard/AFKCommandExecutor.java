package com.epolixa.bityard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommandExecutor implements CommandExecutor
{
    private final AFK afk;

    public AFKCommandExecutor(AFK afk)
    {
        this.afk = afk;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("afk"))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage("\"/afk\" can only be used by a player");
                return false;
            }

            String reason = "";
            for (int i = 0; i < args.length; i++)
                reason += (args[i] + (i >= reason.length() - 1 ? "" : " "));
            afk.goAFK((Player)sender, reason);

            return true;
        }
        return false;
    }
}
