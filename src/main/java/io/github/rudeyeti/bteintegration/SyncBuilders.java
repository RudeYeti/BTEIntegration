package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SyncBuilders {
    public static void sync(Guild guild) {
        try {
            Role role = guild.getRoleById(BTEIntegration.roleID);
            String group = BTEIntegration.configuration.getString("minecraft-role-name");

            if (role == null) {
                BTEIntegration.logger.warning("The role with the ID " + BTEIntegration.roleID + " was not found in the Discord Server " + guild.getName() + ".");
                return;
            }

            for (int i = 1; i < BTEIntegration.lastPage + 1; i++) {
                Document membersPage = Jsoup.connect(BTEIntegration.buildTeamMembers + "?page=" + i).userAgent("BTEIntegration").get();
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

                    if (player != null && !BTEIntegration.permission.playerInGroup(player, group)) {
                        BTEIntegration.permission.playerAddGroup(player, group);
                    }

                    if (BTEIntegration.configuration.getBoolean("log-role-changes")) {
                        BTEIntegration.logger.info("The user " + username + " was promoted to " + role.getName() + " and " + group + ".");
                    }
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
