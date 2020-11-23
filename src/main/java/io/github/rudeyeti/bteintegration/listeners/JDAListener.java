package io.github.rudeyeti.bteintegration.listeners;

import github.scarsz.discordsrv.dependencies.jda.api.events.guild.member.GuildMemberJoinEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import io.github.rudeyeti.bteintegration.SyncBuilders;
import org.jetbrains.annotations.NotNull;

import static io.github.rudeyeti.bteintegration.BTEIntegration.globalRoleChanges;
import static io.github.rudeyeti.bteintegration.BTEIntegration.lastRoleChange;

public class JDAListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (globalRoleChanges) {
            SyncBuilders.syncAllUsers();
        } else {
            if (event.getMember() == lastRoleChange) {
                SyncBuilders.addRole(lastRoleChange);
            }
        }
    }
}
