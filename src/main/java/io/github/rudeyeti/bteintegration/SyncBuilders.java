package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class SyncBuilders {
    public static void sync() {
        try {
            for (int i = 1; i < lastPage + 1; i++) {
                Document membersPage = Jsoup.connect(buildTeamMembers + "?page=" + i).userAgent("BTEIntegration").get();
                Elements td = membersPage.select("td");

                for (int a = 1; a < td.size(); a += 3) {
                    String username = td.get(a).text();
                    Member member = guild.getMemberByTag(username);

                    try {
                        member.getId();
                    } catch (NullPointerException error) {
                        continue;
                    }

                    Player player = Bukkit.getPlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(member.getId()));

                    if (!member.getRoles().contains(role)) {
                        guild.addRoleToMember(member, role).queue();
                    }

                    if (player != null && !getPermissions().playerInGroup(player, group)) {
                        getPermissions().playerAddGroup(player, group);
                    }

                    if (configuration.getBoolean("log-role-changes")) {
                        String message = "The user " + username + " was promoted to " + role.getName();
                        if (player != null) {
                            logger.info(message + " and " + group + ".");
                        } else {
                            logger.info(message + ".");
                        }
                    }
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
