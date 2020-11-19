package io.github.rudeyeti.bteintegration.listeners;

import github.scarsz.discordsrv.dependencies.jda.api.events.guild.member.GuildMemberJoinEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import io.github.rudeyeti.bteintegration.SyncBuilders;
import org.jetbrains.annotations.NotNull;

public class JDAListener extends ListenerAdapter {
    public void guildMemberJoinEvent(@NotNull GuildMemberJoinEvent event) {
        SyncBuilders.sync();
    }
}
