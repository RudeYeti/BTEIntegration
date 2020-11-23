package io.github.rudeyeti.bteintegration.commands;

import org.bukkit.command.CommandSender;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class InfoSubcommand {
    public static void execute(CommandSender sender) {
        sender.sendMessage("General information:\n" +
                           "Author - Rude Yeti, Incorporated\n" +
                           "Version - " + plugin.getDescription().getVersion());
    }
}
