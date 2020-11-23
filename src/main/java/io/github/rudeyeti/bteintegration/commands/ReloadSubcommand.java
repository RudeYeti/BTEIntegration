package io.github.rudeyeti.bteintegration.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class ReloadSubcommand {
    public static void execute(CommandSender sender) {
        if (sender.hasPermission("bteintegration.reload") || sender.isOp()) {
            Configuration oldConfiguration = configuration;
            plugin.reloadConfig();
            configuration = plugin.getConfig();

            if (!validateConfiguration()) {
                configuration = oldConfiguration;
                updateConfiguration();
                sender.sendMessage("The configuration was invalid, reverting back to the previous state.");
            } else {
                sender.sendMessage("The plugin has been successfully reloaded.");
            }
            return;
        }
        sender.sendMessage(ChatColor.RED + "You are missing the correct permission to perform this command.");
    }
}
