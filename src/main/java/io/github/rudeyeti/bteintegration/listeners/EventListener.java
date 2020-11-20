package io.github.rudeyeti.bteintegration.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.objects.managers.AccountLinkManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class EventListener implements Listener {
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        AccountLinkManager accountLinkManager = DiscordSRV.getPlugin().getAccountLinkManager();
        String userID = accountLinkManager.getDiscordId(event.getPlayer().getUniqueId());
        String message = "The user " + guild.getMemberById(userID).getUser().getAsTag();
        boolean hasRole = userID != null && guild.getMemberById(userID).getRoles().contains(role);
        boolean inGroup = getPermissions().playerInGroup(event.getPlayer(), group);

        if (hasRole && !inGroup) {
            getPermissions().playerAddGroup(event.getPlayer(), group);
            if (configuration.getBoolean("log-role-changes")) {
                logger.info(message + " was promoted to " + group + ".");
            }
        } else if (!hasRole && inGroup) {
            getPermissions().playerRemoveGroup(event.getPlayer(), group);
            if (configuration.getBoolean("log-role-changes")) {
                logger.info(message + " was demoted from " + group + ".");
            }
        }
    }
}
