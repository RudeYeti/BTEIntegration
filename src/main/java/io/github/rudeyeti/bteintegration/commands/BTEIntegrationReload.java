package io.github.rudeyeti.bteintegration.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class BTEIntegrationReload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("bteintegration.reload") || sender.isOp()) {
            plugin.reloadConfig();
            configuration = plugin.getConfig();
            validateConfiguration();
            sender.sendMessage("[BTEIntegration] The plugin has been successfully reloaded.");
            return true;
        }
        sender.sendMessage("[BTEIntegration] You are missing the correct permission to perform this command.");
        return false;
    }
}
