package io.github.rudeyeti.bteintegration.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import io.github.rudeyeti.bteintegration.SyncBuilders;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class EventListener implements Listener {
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        String userId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId());
        Member member = guild.getMemberById(userId);
        boolean hasRole = member.getRoles().contains(role);
        boolean inGroup = getPermissions().playerInGroup(event.getPlayer(), minecraftRoleName);

        if (hasRole && !inGroup) {
            getPermissions().playerAddGroup(event.getPlayer(), minecraftRoleName);
            SyncBuilders.logRoleChange(member, "promoted to", minecraftRoleName, false);
        } else if (!hasRole && inGroup) {
            getPermissions().playerRemoveGroup(event.getPlayer(), minecraftRoleName);
            SyncBuilders.logRoleChange(member, "demoted from", minecraftRoleName, false);
        }
    }
}
