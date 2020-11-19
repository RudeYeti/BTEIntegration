package io.github.rudeyeti.bteintegration.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.commons.lang3.ArrayUtils;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.rudeyeti.bteintegration.SyncBuilders;
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

            buildTeamMembers = configuration.getString("build-team-members");
            guild = DiscordSRV.getPlugin().getMainGuild();
            group = configuration.getString("minecraft-role-name");

            if (guild == null) {
                logger.warning("Your Discord Bot must be in a Discord Server.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            }

            validateConfiguration();
            role = guild.getRoleById(roleID);

            if (role == null) {
                logger.warning("The role with the ID " + roleID + " was not found in the Discord Server " + guild.getName() + ".");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            } else if (!ArrayUtils.contains(getPermissions().getGroups(), group)) {
                logger.warning("The minecraft-role-name value " + group + "in the configuration was not registered as a group.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            }

            Document membersFirstPage = Jsoup.connect(buildTeamMembers + "?page=1").userAgent("BTEIntegration").get();
            initialBuilders = membersFirstPage.select("small").text();
            lastPage = Integer.parseInt(membersFirstPage.select("div.pagination").select("a").last().text());
            String builders;

            while (true) {
                Thread.sleep(1000);

                membersFirstPage = Jsoup.connect(buildTeamMembers + "?page=1").userAgent("BTEIntegration").get();
                builders = membersFirstPage.select("small").text();

                if (initialBuilders.equals(builders)) {
                    continue;
                }

                initialBuilders = builders;
                lastPage = Integer.parseInt(membersFirstPage.select("div.pagination").select("a").last().text());

                SyncBuilders.sync();
            }
        } catch (HttpStatusException error) {
        } catch (InterruptedException | IOException error) {
            error.printStackTrace();
        }
    }
}