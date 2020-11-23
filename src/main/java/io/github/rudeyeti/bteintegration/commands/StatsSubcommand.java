package io.github.rudeyeti.bteintegration.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class StatsSubcommand {
    public static void execute(CommandSender sender) {
        if (sender.hasPermission("bteintegration.stats") || sender.isOp()) {
            int playersWithGroup = 0;
            String serverIp = plugin.getServer().getIp();

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (getPermissions().playerInGroup(player, minecraftRoleName)) {
                    playersWithGroup ++;
                }
            }

            sender.sendMessage("Various statistics:\n" +
                               "Discord Members - " + guild.getMemberCount() + "\n" +
                               "Discord Builders - " + guild.getMembersWithRoles(role).size() + "\n" +
                               "Minecraft Players - " + plugin.getServer().getOnlinePlayers().size() + "\n" +
                               "Minecraft Builders - " + playersWithGroup);
            return;
        }
        sender.sendMessage(ChatColor.RED + "You are missing the correct permission to perform this command.");
    }
}
