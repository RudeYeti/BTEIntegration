package io.github.rudeyeti.bteintegration.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BTEIntegrationCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("List of available subcommands:\n" +
                               "info - Details about the author and version of the plugin.\n" +
                               "reload - Updates any values that were modified in the configuration.\n" +
                               "stats - Lists different information regarding the members.");
        } else if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /bteintegration <info | reload | stats>");
        } else if (args[0].equals("info")) {
            InfoSubcommand.execute(sender);
        } else if (args[0].equals("reload")) {
            ReloadSubcommand.execute(sender);
        } else if (args[0].equals("stats")) {
            StatsSubcommand.execute(sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return (args.length <= 1) ? StringUtil.copyPartialMatches(args[0], Arrays.asList("info", "reload", "stats"), new ArrayList<>()) : Collections.singletonList("");
    }
}