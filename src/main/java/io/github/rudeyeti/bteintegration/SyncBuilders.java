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
import java.util.ArrayList;
import java.util.List;

import static io.github.rudeyeti.bteintegration.BTEIntegration.*;

public class SyncBuilders {
    public synchronized static List<Member> getWebsiteMembersList() {
        List<Member> members = new ArrayList<>();

        try {
            for (int i = 1; i < lastPage + 1; i++) {
                Document membersPage = Jsoup.connect(buildTeamMembers + "?page=" + i).userAgent("BTEIntegration").get();
                Elements td = membersPage.select("td");

                for (int a = 1; a < td.size(); a += 3) {
                    String username = td.get(a).text();
                    Member member = guild.getMemberByTag(username);
                    members.add(member);
                }
            }

        } catch (HttpStatusException ignored) {
        } catch (IOException error) {
            error.printStackTrace();
        }

        return members;
    }

    public synchronized static void addRole(Member member) {
        Player player = Bukkit.getPlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(member.getId()));
        boolean hasRole = member.getRoles().contains(role);
        boolean inGroup = player != null && !getPermissions().playerInGroup(player, group);

        if (!hasRole) {
            guild.addRoleToMember(member, role).queue();
        }

        if (inGroup) {
            getPermissions().playerAddGroup(player, group);
        }

        if (!hasRole && configuration.getBoolean("log-role-changes")) {
            String message = "The user " + member.getUser().getAsTag() + " was promoted to " + role.getName();
            if (inGroup) {
                logger.info(message + " and " + group + ".");
            } else {
                logger.info(message + ".");
            }
        }
    }

    public synchronized static void removeRole(Member member) {
        Player player = Bukkit.getPlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(member.getId()));
        boolean inGroup = player != null && getPermissions().playerInGroup(player, group);
        guild.removeRoleFromMember(member, role).queue();

        if (inGroup) {
            getPermissions().playerRemoveGroup(player, group);
        }

        if (configuration.getBoolean("log-role-changes")) {
            String message = "The user " + member.getUser().getAsTag() + " was demoted from " + role.getName();
            if (inGroup) {
                logger.info(message + " and " + group + ".");
            } else {
                logger.info(message + ".");
            }
        }
    }

    public synchronized static void syncUser() {
        buildTeamMembersList = getWebsiteMembersList();
        List<Member> members = buildTeamMembersList;

        if (initialBuildTeamMembersList.size() - members.size() > 0) {
            initialBuildTeamMembersList.removeAll(members);
            removeRole(initialBuildTeamMembersList.get(0));
        } else {
            members.removeAll(initialBuildTeamMembersList);
            addRole(members.get(0));
        }
    }

    public synchronized static void syncAllUsers() {
        List<Member> members = getWebsiteMembersList();
        List<Member> membersToDemote = guild.getMembersWithRoles(role);

        for (Member member : members) {
            try {
                member.getId();
            } catch (NullPointerException error) {
                return;
            }

            membersToDemote.remove(member);
            addRole(member);
        }

        for (Member member : membersToDemote) {
            removeRole(member);
        }
    }

    public synchronized static void sync() {
        if (configuration.getBoolean("global-role-changes")) {
            syncAllUsers();
        } else {
            syncUser();
        }
    }
}
