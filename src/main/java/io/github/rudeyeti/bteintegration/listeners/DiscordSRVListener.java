package io.github.rudeyeti.bteintegration.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.AccountLinkedEvent;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.commons.lang3.ArrayUtils;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.rudeyeti.bteintegration.SyncBuilders;
import org.bukkit.entity.Player;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class DiscordSRVListener {
    @Subscribe
    public void discordReadyEvent(DiscordReadyEvent event) {
        try {
            DiscordUtil.getJda().addEventListener(new JDAListener());
            guild = DiscordSRV.getPlugin().getMainGuild();

            if (guild == null) {
                logger.warning("Your Discord Bot must be in a Discord Server.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            } else if (!validateConfiguration()) {
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            }

            role = guild.getRoleById(discordRoleId);

            if (role == null) {
                logger.warning("The role with the ID " + discordRoleId + " was not found in the Discord Server " + guild.getName() + ".");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            } else if (!ArrayUtils.contains(getPermissions().getGroups(), minecraftRoleName)) {
                logger.warning("The minecraft-role-name value " + minecraftRoleName + "in the configuration was not registered as a group.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            }

            Document membersFirstPage = Jsoup.connect(buildTeamMembers + "?page=1").userAgent("BTEIntegration").get();
            String initialBuilders = membersFirstPage.select("small").text();
            lastPage = Integer.parseInt(membersFirstPage.select("div.pagination").select("a").last().text());

            if (!globalRoleChanges) {
                initialBuildTeamMembersList = SyncBuilders.getWebsiteMembersList();
            }

            while (true) {
                Thread.sleep(1000);

                membersFirstPage = Jsoup.connect(buildTeamMembers + "?page=1").userAgent("BTEIntegration").get();
                String builders = membersFirstPage.select("small").text();

                if (initialBuilders.equals(builders)) {
                    continue;
                }

                SyncBuilders.sync();

                if (!globalRoleChanges) {
                    initialBuildTeamMembersList = SyncBuilders.getWebsiteMembersList();
                }

                initialBuilders = builders;
                lastPage = Integer.parseInt(membersFirstPage.select("div.pagination").select("a").last().text());
            }
        } catch (HttpStatusException ignored) {
        } catch (InterruptedException | IOException error) {
            error.printStackTrace();
        }
    }

    @Subscribe
    public void accountLinkedEvent(AccountLinkedEvent event) {
        Member member = guild.getMember(event.getUser());

        if (member.getRoles().contains(role)) {
            getPermissions().playerAddGroup((Player) event.getPlayer(), minecraftRoleName);
            SyncBuilders.logRoleChange(member, "promoted to", minecraftRoleName, false);
        }
    }
}