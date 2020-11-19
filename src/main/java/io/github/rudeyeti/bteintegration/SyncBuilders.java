package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class SyncBuilders {
    public static void sync() {
        try {
            List<Member> membersToDemote = guild.getMembersWithRoles(role);

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

                    membersToDemote.remove(member);
                    boolean hasRole = member.getRoles().contains(role);
                    Player player = Bukkit.getPlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(member.getId()));
                    boolean inGroup = player != null && !getPermissions().playerInGroup(player, group);

                    if (!hasRole) {
                        guild.addRoleToMember(member, role).queue();
                    }

                    if (inGroup) {
                        getPermissions().playerAddGroup(player, group);
                    }

                    if (!hasRole && configuration.getBoolean("log-role-changes")) {
                        String message = "The user " + username + " was promoted to " + role.getName();
                        if (inGroup) {
                            logger.info(message + " and " + group + ".");
                        } else {
                            logger.info(message + ".");
                        }
                    }
                }
            }

            for (Member member : membersToDemote) {
                Player player = Bukkit.getPlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(member.getId()));
                boolean inGroup = player != null && getPermissions().playerInGroup(player, group);
                guild.removeRoleFromMember(member, role).queue();

                if (inGroup) {
                    getPermissions().playerRemoveGroup(player, group);
                }

                if (configuration.getBoolean("log-role-changes")) {
                    String message = "The user " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " was demoted from " + role.getName();
                    if (inGroup) {
                        logger.info(message + " and " + group + ".");
                    } else {
                        logger.info(message + ".");
                    }
                }
            }
        } catch (HttpStatusException ignored) {
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
