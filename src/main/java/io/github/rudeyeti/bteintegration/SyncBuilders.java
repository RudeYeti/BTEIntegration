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
    private static int membersSize;

    public synchronized static List<Member> getWebsiteMembersList() {
        List<Member> members = new ArrayList<>();
        membersSize = 0;

        try {
            for (int i = 1; i < lastPage + 1; i++) {
                Document membersPage = Jsoup.connect(buildTeamMembers + "?page=" + i).userAgent("BTEIntegration").get();
                Elements td = membersPage.select("td");

                for (int a = 1; a < td.size(); a += 3) {
                    String username = td.get(a).text();
                    Member member = guild.getMemberByTag(username);
                    membersSize++;

                    try {
                        member.getId();
                    } catch (NullPointerException error) {
                        continue;
                    }

                    members.add(member);
                }
            }
        } catch (HttpStatusException ignored) {
        } catch (IOException error) {
            error.printStackTrace();
        }

        return members;
    }

    public synchronized static void logRoleChange(Member member, String roleChange, String roleName, boolean inGroup) {
        if (logRoleChanges) {
            String message = "The user " + member.getUser().getAsTag() + " was " + roleChange + " " + roleName;
            if (inGroup) {
                logger.info(message + " and " + minecraftRoleName + ".");
            } else {
                logger.info(message + ".");
            }
        }
    }

    public synchronized static void addRole(Member member) {
        Player player = Bukkit.getPlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(member.getId()));
        boolean hasRole = member.getRoles().contains(role);
        boolean inGroup = player != null && !getPermissions().playerInGroup(player, minecraftRoleName);

        if (!hasRole) {
            guild.addRoleToMember(member, role).queue();

            if (inGroup) {
                getPermissions().playerAddGroup(player, minecraftRoleName);
            }

            logRoleChange(member, "promoted to", role.getName(), inGroup);
        }
    }

    public synchronized static void removeRole(Member member) {
        Player player = Bukkit.getPlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(member.getId()));
        boolean inGroup = player != null && getPermissions().playerInGroup(player, minecraftRoleName);
        guild.removeRoleFromMember(member, role).queue();

        if (inGroup) {
            getPermissions().playerRemoveGroup(player, minecraftRoleName);
        }

        logRoleChange(member, "demoted from", role.getName(), inGroup);
    }

    public synchronized static void syncUser() {
        int initialMembersSize = membersSize;
        List<Member> members = getWebsiteMembersList();

        Member member;
        if (initialMembersSize - membersSize > 0) {
            initialBuildTeamMembersList.removeAll(members);
            member = initialBuildTeamMembersList.get(0);
            removeRole(member);
        } else {
            members.removeAll(initialBuildTeamMembersList);
            member = members.get(0);
            addRole(member);
        }

        lastRoleChange = member;
    }

    public synchronized static void syncAllUsers() {
        List<Member> members = getWebsiteMembersList();
        List<Member> membersToDemote = guild.getMembersWithRoles(role);

        for (Member member : members) {
            membersToDemote.remove(member);
            addRole(member);
        }

        for (Member member : membersToDemote) {
            removeRole(member);
        }
    }

    public synchronized static void sync() {
        if (globalRoleChanges) {
            syncAllUsers();
        } else {
            syncUser();
        }
    }
}
