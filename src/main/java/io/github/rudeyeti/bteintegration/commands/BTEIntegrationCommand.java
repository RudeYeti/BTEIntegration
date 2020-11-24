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
                               "info - Shows details about the author and the version of this plugin.\n" +
                               "reload - Updates any values that were modified in the configuration.\n" +
                               "stats - Lists different information regarding the members.");
        } else if (args[0].matches("i(nfo)?(rmation)?|authors?|ver(sion)?")) {
            InfoSubcommand.execute(sender);
        } else if (args[0].matches("r(e?(load|start|boot))?|(en|dis)able")) {
            ReloadSubcommand.execute(sender);
        } else if (args[0].matches("s(tat)?(istic)?s?")) {
            StatsSubcommand.execute(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /bteintegration <info | reload | stats>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return (args.length <= 1) ? StringUtil.copyPartialMatches(args[0], Arrays.asList("info", "reload", "stats"), new ArrayList<>()) : Collections.singletonList("");
    }
}