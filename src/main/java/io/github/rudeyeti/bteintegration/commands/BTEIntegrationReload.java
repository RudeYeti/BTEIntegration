package io.github.rudeyeti.bteintegration.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class BTEIntegrationReload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("bteintegration.reload") || sender.isOp()) {
            Configuration oldConfiguration = configuration;
            plugin.reloadConfig();
            configuration = plugin.getConfig();

            if (!validateConfiguration()) {
                configuration = oldConfiguration;
                sender.sendMessage("[BTEIntegration] The configuration was invalid, reverting back to the previous state.");
            } else {
                sender.sendMessage("[BTEIntegration] The plugin has been successfully reloaded.");
            }
            return true;
        }
        sender.sendMessage("[BTEIntegration] You are missing the correct permission to perform this command.");
        return true;
    }
}
